package com.naiz.eus.widget;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.naiz.eus.MainActivity;
import com.naiz.eus.R;
import com.naiz.eus.db.DatabaseHandler;
import com.naiz.eus.model.Berria;

import android.annotation.SuppressLint;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

@SuppressLint("NewApi")
public class LoremViewsFactory implements RemoteViewsService.RemoteViewsFactory {

	private Context ctxt = null;
	private int appWidgetId;
	private String searchURL = "http://www.naiz.eus/";
	private ArrayList<Berria> berriLista;
	private String titularra = "";
	Boolean eguneratuBeharra=false;
	DatabaseHandler db;
	String dbEguneratzea;
	String naizEguneratzea;

	public LoremViewsFactory(Context ctxt, Intent intent) {
		Log.i("LoremViewsFactory", "LOG");
		this.ctxt = ctxt;
		setAppWidgetId(intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
				AppWidgetManager.INVALID_APPWIDGET_ID));
	}

	@Override
	public void onCreate() {

		Log.i("LoremViewsFactory oncreate", "LOG");
		berriLista = new ArrayList<Berria>();
				//
		db = new DatabaseHandler(this.ctxt);
		try {
			db.createDataBase();
			db.close();
		} catch (IOException e1) {
			e1.printStackTrace();
			System.out.println("widget: gaizki db");
		}
		//
		ThreadClass thread = new ThreadClass();
		thread.start();
		// wait for thread to finish
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onDestroy() {
		// no-op
	}

	@Override
	public int getCount() {
		return berriLista.size();
		// return(items.length);
	}

	@Override
	public RemoteViews getViewAt(int position) {
		Log.i("Lorem getViewAt", "LOG");
		RemoteViews row = new RemoteViews(ctxt.getPackageName(), R.layout.berria_list_item);

		// row.setTextViewText(R.id.rowprice, items[position]);
		row.setTextViewText(R.id.rowsection, berriLista.get(position).getSaila());
		row.setTextColor(R.id.rowtit, Color.parseColor("#303030"));
		row.setTextViewTextSize(R.id.rowtit, TypedValue.COMPLEX_UNIT_DIP, 14);
		row.setTextViewText(R.id.rowtit, berriLista.get(position).getTitle());
		row.setTextColor(R.id.rowdata, Color.parseColor("#303030"));
		row.setTextViewTextSize(R.id.rowdata, TypedValue.COMPLEX_UNIT_DIP, 11);
		row.setTextViewText(R.id.rowdata, berriLista.get(position).getExtraInfo());
		row.setImageViewBitmap(R.id.rowimage, berriLista.get(position).getImage());
		// ImageView imgIcon = (ImageView)
		// convertView.findViewById(R.id.rowimage);
		// TextView txtDesk = (TextView) convertView.findViewById(R.id.rowsubtitle);
		Intent i = new Intent();
		Bundle extras = new Bundle();

		extras.putString(WidgetProvider.EXTRA_WORD, berriLista.get(position).toString());
		i.putExtras(extras);
		row.setOnClickFillInIntent(R.id.berrilistaw, i);

		return (row);
	}

	@Override
	public RemoteViews getLoadingView() {
		return (null);
	}

	@Override
	public int getViewTypeCount() {
		return (1);
	}

	@Override
	public long getItemId(int position) {
		return (position);
	}

	@Override
	public boolean hasStableIds() {
		return (true);
	}

	@Override
	public void onDataSetChanged() {

		Log.i("Lorem onDataSetChanged", "LOG");
		berriLista = new ArrayList<Berria>();
		ThreadClass thread = new ThreadClass();
		thread.start();
		// wait for thread to finish
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int getAppWidgetId() {
		return appWidgetId;
	}

	public void setAppWidgetId(int appWidgetId) {
		this.appWidgetId = appWidgetId;
	}
	private void LortuBerriakdb() throws ParseException {
		db.close();
		if (db.checkDataBase() == false) {
			try {
				db.createDataBase();
				db.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		ArrayList<Berria> albisteak = null;
		try {
			albisteak = db.getBerriak("azala");
			dbEguneratzea = db.getEguneratzeData("azala");
			System.out.println("widget: dbEguneratzea1 "+dbEguneratzea );
			if(dbEguneratzea!=null){
				Date today=new Date();
				SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd HH.mm");
				
				int aurreratua = dbEguneratzea.compareTo(ft.format(today));
				System.out.println("widget: ft.format(today) "+ft.format(today) );
				System.out.println("widget: aurreratua1 "+aurreratua  );
				if (aurreratua>0){//ondo>
					String sEguna = dbEguneratzea.substring(8, 10);
					//System.out.println("sEguna1 "+sEguna  );
					Integer e = Integer.valueOf(sEguna);
					e--;
					String se=e.toString();
					if(e<10){
						se="0"+se;
					}
					//System.out.println("e1 "+e  );
					dbEguneratzea=dbEguneratzea.substring(0, 8)+se+dbEguneratzea.substring(10,dbEguneratzea.length());
					//	System.out.println("dbEguneratzea2 "+dbEguneratzea);
				}
			}else{
				//db.setEguneratzeData("2015-01-01 00.00",saila);
				dbEguneratzea="2015-01-01 00.00";
			}
		} catch (SQLiteException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (albisteak != null) {
			MainActivity.albisteKop = albisteak.size();
			for (int i = 0; i < albisteak.size(); i++) {
				Berria p = albisteak.get(i);
				berriLista.add(p);
				System.out.println("widget: LortuBerriakdb: "+i);
			}
		} else {
			System.out.println("widget: albisteak hutsak");
		}
	}		
	private void berriTaulaHustu() {
		db.close();
		if (db.checkDataBase() == false) {
			try {
				db.createDataBase();
				db.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			db.berriakHustu("azala");
		} catch (SQLiteException e) {
			e.printStackTrace();
		}
	}

	class ThreadClass extends Thread {
		public ThreadClass() {
		}

		public void run() {
				berriLista=new ArrayList<Berria>();
				try {
					LortuBerriakdb();
				} catch (ParseException e) {
					e.printStackTrace();
				}
			Document doc = null;
			try {
				doc = Jsoup.connect(searchURL).get();

				if (doc != null) {
					// Connect to the web site
					if (titularra == "") {
						Elements izenburua = doc.select("ul[id*=nav-menu-logo]");
						Elements titu = izenburua.select("li");
						titularra = titu.get(0).text();// lista osoaren titulua ezarri
					}
					//}
					String[] titularZatiak = titularra.split(" ");
					naizEguneratzea = titularZatiak[titularZatiak.length-1];
					
					System.out.println("widget: naizEguneratzea "+naizEguneratzea);
					System.out.println("widget: dbEguneratzea "+ dbEguneratzea);
					Date today=new Date();
					SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd");
					if (naizEguneratzea == "" || naizEguneratzea == null){
						naizEguneratzea = "2015-01-01 00.01";
					}else{
						naizEguneratzea = ft.format(today) + " " + naizEguneratzea;
					}
					//System.out.println("Current Date: " + ft.format(today));
					int s = dbEguneratzea.compareTo(naizEguneratzea);
					System.out.println("s: "+s+" dbEguneratzea:"+dbEguneratzea+" naizEguneratzea"+naizEguneratzea);
					
					if(s<0){
						System.out.println("widget: sBARRU: "+s+" dbEguneratzea:"+dbEguneratzea+" naizEguneratzea"+naizEguneratzea);
						berriTaulaHustu();
				Elements produktuak = doc.select("div.article");
				MainActivity.albisteKop=(produktuak.size());
				db.setEguneratzeData(naizEguneratzea,"azala",produktuak.size());
				for (int i = 0; i < produktuak.size(); i++) {
					Elements produktu_izenb = produktuak.get(i).select(
							"div[class*=title]");
					Elements produktu_desk = produktuak.get(i).select(
							"div[class*=abstract]");
					Elements albiste_info = produktuak.get(i).select(
							"div[class*=extra-info]");
					Elements albiste_saila = produktuak.get(i).select(
							"span[class*=section]");
					Elements produktu_irudiak = produktuak.get(i).select("img");
					Berria p = new Berria();
					String text_p_izena = "";
					if (produktu_izenb.first() != null) {
						text_p_izena = produktu_izenb.first().text();
					}
					p.setTitle(text_p_izena);
					String produktu_linka = "";
					String weba = "";
					Elements produktu_linkak = produktu_izenb.select("a");
					if (produktu_izenb.first() != null) {
						produktu_linka = produktu_linkak.first().attr("href");
						if (produktu_linka.length() > 1) {
							if (produktu_linka.startsWith("/")) {
								weba = "http://www.naiz.eus";
							}
						}
						p.setLink(weba + produktu_linka);
					}

					String text_albiste_desk = "";
					if (produktu_desk.first() != null) {
						text_albiste_desk = produktu_desk.first().text();
					}
					p.setSubtitle(text_albiste_desk);
					String produktu_irudia = "";
					if (produktu_irudiak.first() != null) {

						produktu_irudia = produktu_irudiak.first().attr("src");
						if (produktu_irudia.startsWith("/")) {
							produktu_irudia = "http://www.naiz.eus"
									+ produktu_irudia;
						}
						Bitmap bm = MainActivity
								.getBitmapFromURL(produktu_irudia);
						p.setImage(bm);
					}
					Bitmap bm = MainActivity.getBitmapFromURL(produktu_irudia);
					p.setImage(bm);

					String Info = albiste_info.text();
					p.setExtraInfo(Info);
					/////////////
					DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
					Date now = new Date();
					//////////////
					String text_albiste_saila = "";
					if (albiste_saila.first() != null) {
						text_albiste_saila = albiste_saila.first().text()+" "+dateFormat.format(now);
					}
					p.setSaila(text_albiste_saila);
					String albiste_sail_link = "";
					weba = "";
					Elements albiste_sail_linkE = albiste_saila.select("a");
					if (albiste_sail_linkE.first() != null) {
						albiste_sail_link = albiste_sail_linkE.first().attr(
								"href");
						if (albiste_sail_link.length() > 1) {
							if (albiste_sail_link.startsWith("/")) {
								weba = "http://www.naiz.eus";
							}
						}
						p.setSailLinka(weba + produktu_linka);
					}
					berriLista.add(p);
//					System.out.println("linka: " + p.getLink() + " desk: "
//							+ p.getSubtitle() + " irudia: " + produktu_irudia
//							+ " saila:" + p.getSaila());
					// Toast.makeText(getActivity().getApplicationContext(),"partido "+spartido,Toast.LENGTH_SHORT).show();
					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					 if (bm!=null){
					 bm.compress(CompressFormat.PNG, 0, stream);
					 }
					try {
						System.out.println("widget: db.SartuBerriaOK: "+p.getTitle());
						db.SartuBerria("azala",text_p_izena, text_albiste_desk, Info, text_albiste_saila, "", stream.toByteArray(), p.getLink(),p.getSailLinka());
					} catch (SQLiteException e) {
						System.out.println("db.SartuBerria1: "+e.getMessage());
						e.printStackTrace();
					} catch (IOException e) {
						System.out.println("db.SartuBerria2: "+e.getMessage());
						e.printStackTrace();
					}
				}
					}
					}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
}