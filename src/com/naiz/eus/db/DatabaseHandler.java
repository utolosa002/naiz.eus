package com.naiz.eus.db;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper{
	@SuppressLint("SdCardPath")
	private static String DB_PATH = "/data/data/com.naiz.eus/databases/";
    private static String DB_NAME = "naiz.db";
    private SQLiteDatabase myDataBase;
    private final Context myContext;
	// hondakin taulak
	private static final String TABLE_HONDAKINAK_EU = "hondakineu";
	private static final String TABLE_HONDAKINAK_ES = "hondakines";
    private static final int DB_VERSION = 5;
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
    		this.getReadableDatabase();
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
    		checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
 
    	}catch(SQLiteException e){
    		System.out.println("datu basea ez da existitzen oraindik");
    	}
    	if(checkDB != null){
    		checkDB.close();
    	}
    	return checkDB != null ? true : false;
    }
 
    private void copyDataBase() throws IOException{
    	InputStream myInput = myContext.getAssets().open("hondakinak.db");
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
    	myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
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
//	public ArrayList<Hondakina> getHondakinak(String str,String zutabe)  throws IOException, SQLiteException {
//		ArrayList<Hondakina> hondakinList = new ArrayList<Hondakina>();
//		SQLiteDatabase db = this.getReadableDatabase();
//		String pattern = "[a-zA-Z]*[- ()]*[a-zA-Z]*";
//		if (!str.matches(pattern)&& (zutabe!="non")) {
//			if(str==""||str==null){
//		}else{str = " ";}
//		}
//		String selectQuery = "SELECT  * FROM " + TABLE_HONDAKINAK_EU
//				+ " WHERE "+zutabe+" LIKE '" + str + "%' ORDER BY "+ KEY_NAME +"";
//		System.out.println(selectQuery);
//		Cursor cursor = db.rawQuery(selectQuery, null);
//		if (cursor.moveToFirst()) {
//			do {
//				Hondakina hondakin = new Hondakina();
//				hondakin.setID(Integer.parseInt(cursor.getString(0)));
//				hondakin.setName(cursor.getString(1));
//				hondakin.setNon(cursor.getString(2));
//				hondakin.setInfo(cursor.getString(3));
//				// Adding hondakin to list
//				hondakinList.add(hondakin);
//			} while (cursor.moveToNext());
//		}
//		
//		// return hondakin list
//		return hondakinList;
//	}
//	public ArrayList<Hondakina> getResiduos(String str,String zutabe)  throws IOException,SQLiteException {
//		ArrayList<Hondakina> hondakinList = new ArrayList<Hondakina>();
//		SQLiteDatabase db = this.getReadableDatabase();
//		String pattern = "[a-zA-Z]*[- ()]*[a-zA-Z]*";
//		if (!str.matches(pattern)&&(zutabe!="non")) {
//			if(str==""||str==null){
//		}else{str = " ";}
//		}
//		String selectQuery = null;
//			selectQuery = "SELECT  * FROM " + TABLE_HONDAKINAK_ES
//					+ " WHERE "+zutabe+" LIKE '" + str + "%' ORDER BY "+ KEY_NAME +"";
//
//		Cursor cursor = db.rawQuery(selectQuery, null);
//		if (cursor.moveToFirst()) {
//			do {
//				Hondakina hondakin = new Hondakina();
//				hondakin.setID(Integer.parseInt(cursor.getString(0)));
//				hondakin.setName(cursor.getString(1));
//				hondakin.setNon(cursor.getString(2));
//				hondakin.setInfo(cursor.getString(3));
//				// Adding hondakin to list
//				hondakinList.add(hondakin);
//			} while (cursor.moveToNext());
//		}
//
//		// return hondakin list
//		return hondakinList;
//	}
	public ArrayList<String> getHondakinIzenak(String bilatzekoa,String where) throws IOException,SQLiteException {
		ArrayList<String> hondakinList = new ArrayList<String>();
 		SQLiteDatabase db = this.getReadableDatabase();
		String pattern = "[a-zA-Z]*[- ()]*[a-zA-Z]*";
		if (!bilatzekoa.matches(pattern)) {
			if(bilatzekoa==""||bilatzekoa==null){
		}else{bilatzekoa = " ";}
		}
		String selectQuery = "SELECT " + KEY_NAME + " FROM " + TABLE_HONDAKINAK_EU
					+ " WHERE " + where + " LIKE '" + bilatzekoa + "%' ORDER BY "+ KEY_NAME +"";
		
		String Hizena;
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				Hizena = cursor.getString(0);
				// Adding hondakin to list
				hondakinList.add(Hizena);
			} while (cursor.moveToNext());
		}
		// return hondakin list
		return hondakinList;
	}
	public ArrayList<String> getNombresResiduos(String bilatzekoa,String where) throws IOException,SQLiteException {
		ArrayList<String> hondakinList = new ArrayList<String>();
		SQLiteDatabase db = this.getReadableDatabase();
		String pattern = "[a-zA-Z]*[- ()]*[a-zA-Z]*";
		if (!bilatzekoa.matches(pattern)) {
			if(bilatzekoa==""||bilatzekoa==null){
		}else{bilatzekoa = " ";}
		}
		
		String selectQuery;
			selectQuery = "SELECT " + KEY_NAME + " FROM " + TABLE_HONDAKINAK_ES
					+ " WHERE " + where + " LIKE '" + bilatzekoa + "%' ORDER BY "+ KEY_NAME +"";
		String Hizena;
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				Hizena = cursor.getString(0);
				hondakinList.add(Hizena);
			} while (cursor.moveToNext());
		}
		return hondakinList;
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
}
