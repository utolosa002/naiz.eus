package com.naiz.eus.widget;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.naiz.eus.MainActivity;
import com.naiz.eus.R;
import com.naiz.eus.model.Berria;

import android.annotation.SuppressLint;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
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

	class ThreadClass extends Thread {
		public ThreadClass() {
		}

		public void run() {
			Document doc = null;
			try {
				doc = Jsoup.connect(searchURL).get();

				// Connect to the web site
				if (titularra == "") {
					Elements izenburua = doc.select("ul[id*=nav-menu-logo]");
					Elements titu = izenburua.select("li");
					titularra = titu.get(0).text();// lista osoaren titulua ezarri
				}
				Elements produktuak = doc.select("div.article");

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

					String text_produktu_desk = "";
					if (produktu_desk.first() != null) {
						text_produktu_desk = produktu_desk.first().text();
					}
					p.setSubtitle(text_produktu_desk);
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
					System.out.println("linka: " + p.getLink() + " desk: "
							+ p.getSubtitle() + " irudia: " + produktu_irudia
							+ " saila:" + p.getSaila());
					// Toast.makeText(getActivity().getApplicationContext(),"partido "+spartido,Toast.LENGTH_SHORT).show();
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
}