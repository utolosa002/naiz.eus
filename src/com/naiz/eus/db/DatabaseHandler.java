package com.naiz.eus.db;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import com.naiz.eus.model.Berria;
import com.naiz.eus.model.Saila;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.BitmapFactory;

public class DatabaseHandler extends SQLiteOpenHelper{
	@SuppressLint("SdCardPath")
	private static String DB_PATH = "/data/data/com.naiz.eus/databases/";
    private static String DB_NAME = "naiz.db";
    private SQLiteDatabase myDataBase;
    private final Context myContext;
	// hondakin taulak
    private static final String TABLE_BERRIAK = "berriak";
    private static final String TABLE_HERRIAK = "herriak";
    private static final String TABLE_EGUNERAKETA = "eguneraketa";
	private static final String TABLE_BLOG = "blogak";
    private static final int DB_VERSION = 1;
	private static final String KEY_NAME = "izena";

    public DatabaseHandler(Context context) {
    	super(context, DB_NAME, null, DB_VERSION);
        this.myContext = context;
    }	
 
    public void createDataBase() throws IOException{
    	boolean dbExist = checkDataBase();
    	if(dbExist){
    		System.out.println("ez egin ezer, dagoeneko existitzen da");
    	}else{
    		this.getWritableDatabase();
        	System.out.println("ez da existitzen");
        	try {
    			copyDataBase();
    		} catch (IOException e) {
        		throw new Error("Error copying database");
        	}
    	}
    }
 
    public boolean checkDataBase(){
    	SQLiteDatabase checkDB = null;
    	try{
    		String myPath = DB_PATH + DB_NAME;
    		System.out.println("checking "+myPath);
    		checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
 
    	}catch(SQLiteException e){
    		System.out.println("datu basea ez da existitzen oraindik");
    	}
    	if(checkDB != null){
    		checkDB.close();
    	}
    	return checkDB != null ? true : false;
    }
 
    private void copyDataBase() throws IOException{
    	InputStream myInput = myContext.getAssets().open("naiz.db");
    	String outFileName = DB_PATH + DB_NAME;
    	OutputStream myOutput = new FileOutputStream(outFileName);
    	//transfer bytes from the inputfile to the outputfile
    	byte[] buffer = new byte[1024];
    	int length;
    	while ((length = myInput.read(buffer))>0){
    		myOutput.write(buffer, 0, length);
    	}
    	myOutput.flush();
    	myOutput.close();
    	myInput.close();
    }
 
    public void openDataBase() throws SQLException{
        String myPath = DB_PATH + DB_NAME;
    	myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }
 
    @Override
	public synchronized void close() {
    	    if(myDataBase != null)
    		    myDataBase.close();
    	    super.close();
	}
 
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if ( newVersion > oldVersion)
        {
        	 System.out.println("New database version exists for upgrade.");         
            try {
               System.out.println("droping upgrade database..."+newVersion+" old:"+oldVersion); 
               dropDB();
               System.out.println("Copying upgrade database...");
               copyDataBase();
            } catch (IOException e) {
                // TODO Auto-generated catch block
            	System.out.println("upgrade database catch..."); 
                e.printStackTrace();
            }       
        }
    }
	public ArrayList<Berria> getBerriak(String saila)  throws IOException, SQLiteException {
		ArrayList<Berria> BerriList = new ArrayList<Berria>();
		SQLiteDatabase db = this.getReadableDatabase();
		String selectQuery = "SELECT  * FROM " + TABLE_BERRIAK +" WHERE saila='"+saila+"'";
		//System.out.println(selectQuery);
		Cursor cursor = db.rawQuery(selectQuery, null);
		int kont=0;
		if (cursor.moveToFirst()) {
			do {
				Berria Berria = new Berria();
				Berria.setTitle(cursor.getString(0));
				Berria.setSubtitle(cursor.getString(1));
				Berria.setExtraInfo(cursor.getString(2));
				Berria.setSaila(cursor.getString(3));
				Berria.setBerria(cursor.getString(4));
				Berria.setImage(BitmapFactory.decodeByteArray(cursor.getBlob(5), 0, cursor.getBlob(5).length));
				Berria.setLink(cursor.getString(6));
				Berria.setSailLinka(cursor.getString(7));
				// Adding hondakin to list
				BerriList.add(Berria);
				kont++;
				System.out.println("kont"+kont);
			} while (cursor.moveToNext());
		}
		System.out.println("berrilist"+BerriList.size());
		return BerriList;
	}
	public Berria getBerria(String link)  throws IOException, SQLiteException {
		ArrayList<Berria> BerriList = new ArrayList<Berria>();
		SQLiteDatabase db = this.getReadableDatabase();
		String selectQuery = "SELECT  * FROM " + TABLE_BERRIAK +" WHERE link='"+link+"'";
		//System.out.println(selectQuery);
		Cursor cursor = db.rawQuery(selectQuery, null);
		Berria Berria = new Berria();
		if (cursor.moveToFirst()) {
			do {
				Berria.setTitle(cursor.getString(0));
				Berria.setSubtitle(cursor.getString(1));
				Berria.setExtraInfo(cursor.getString(2));
				Berria.setSaila(cursor.getString(3));
				Berria.setBerria(cursor.getString(4));
				Berria.setImage(BitmapFactory.decodeByteArray(cursor.getBlob(5), 0, cursor.getBlob(5).length));
				Berria.setLink(cursor.getString(6));
				Berria.setSailLinka(cursor.getString(7));
			} while (cursor.moveToNext());
		}
		System.out.println("berrilist"+BerriList.size());
		return Berria;
	}
//	public ArrayList<String> getHondakinIzenak(String bilatzekoa,String where) throws IOException,SQLiteException {
//		ArrayList<String> hondakinList = new ArrayList<String>();
// 		SQLiteDatabase db = this.getReadableDatabase();
//		String pattern = "[a-zA-Z]*[- ()]*[a-zA-Z]*";
//		if (!bilatzekoa.matches(pattern)) {
//			if(bilatzekoa==""||bilatzekoa==null){
//		}else{bilatzekoa = " ";}
//		}
//		String selectQuery = "SELECT " + KEY_NAME + " FROM " + TABLE_BLOG
//					+ " WHERE " + where + " LIKE '" + bilatzekoa + "%' ORDER BY "+ KEY_NAME +"";
//		
//		String Hizena;
//		Cursor cursor = db.rawQuery(selectQuery, null);
//		if (cursor.moveToFirst()) {
//			do {
//				Hizena = cursor.getString(0);
//				// Adding hondakin to list
//				hondakinList.add(Hizena);
//			} while (cursor.moveToNext());
//		}
//		// return hondakin list
//		return hondakinList;
//	}
	
	public void SartuBerria(String saila,String tit,String subtit,String extra,String section,String text,byte[] media,String link,String sectionlink) throws IOException,SQLiteException {
		
 		SQLiteDatabase db = this.getWritableDatabase();
		//String selectQuery = "INSERT INTO `berriak` (`tit`, `subtit`, `extra`, `section`, `text`, `media`, `link`, `sectionlink`) VALUES"
		//+ " ('" + tit +"', '"+subtit+"', '"+extra+"', '"+section+"', '"+text+"', '"+media+"', '"+link+"', '"+sectionlink+"')";
		ContentValues values=new ContentValues();
		values.put("tit", tit);
		values.put("subtit",subtit);
		values.put("extra", extra);
		values.put("section",section);
		values.put("text", text);
		values.put("media",media);
		values.put("link",link);
		values.put("sectionlink",sectionlink);
		values.put("saila",saila);
		
		db.insert("berriak", null,values);

	}
	public void dropDB() {
		File dbFile = new File(DB_PATH + DB_NAME);
		if( dbFile.exists()){
			dbFile.delete();
		}
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		
	}

	public void berriakHustu(String saila) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "DELETE FROM "+TABLE_BERRIAK+" WHERE saila='"+saila+"' ";
		db.execSQL(sql);
	}

	public String getEguneratzeData(String saila) {
		SQLiteDatabase db = this.getReadableDatabase();
		String selectQuery = "SELECT date FROM '" + TABLE_EGUNERAKETA +"' WHERE saila='"+saila+"' LIMIT 1";

		System.out.println("getEguneratzeData"+selectQuery);
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			return cursor.getString(0);
		}
		return null;
	}

	public void setEguneratzeData(String naizEguneratzea,String saila) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		String sql = "DELETE FROM "+TABLE_EGUNERAKETA+" WHERE saila='"+saila+"'";
		db.execSQL(sql);
		
 		ContentValues values=new ContentValues();
		values.put("saila", saila);
		values.put("date", naizEguneratzea);
		db.insert("eguneraketa", null,values);
	}

	public ArrayList<Saila> selectHerriak(String herrialdea) {
		ArrayList<Saila> HerriList = new ArrayList<Saila>();
		SQLiteDatabase db = this.getReadableDatabase();
		String selectQuery = "SELECT herria FROM '" + TABLE_HERRIAK +"' WHERE herrialdea='"+herrialdea+"'";
		System.out.println(selectQuery);
		Cursor cursor = db.rawQuery(selectQuery, null);
		int kont=0;
		if (cursor.moveToFirst()) {
			do {
				Saila s=new Saila();
				String Hizena = cursor.getString(0);
				s.setTit(Hizena);
				String HLink = "http://www.naiz.eus/eu/eguraldia/euskal-herria/"+herrialdea+"/"+Hizena;
				s.setLink(HLink);
				HerriList.add(s);
				kont++;
				System.out.println("SELECT"+herrialdea+"kont"+kont);
			} while (cursor.moveToNext());
		}
		System.out.println("herriList"+HerriList.size());
		return HerriList;
	}
	public Integer insertHerriak(String herrial,ArrayList<Saila> herriLi) {
		SQLiteDatabase db = this.getReadableDatabase();
		int j=0;
			for (j=0;j<herriLi.size();j++){
				ContentValues values=new ContentValues();
				values.put("herria",herriLi.get(j).getTit());
				values.put("herrialdea",herrial);
				db.insert("herriak", null,values);
				System.out.println("Datu basean herrialde herriak sartzen"+herriLi.get(j).getTit()+" ,"+herriLi.get(j).getTit());
			}

		return j;
	}
}
