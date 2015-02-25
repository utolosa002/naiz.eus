package com.naiz.eus;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.widget.AdapterView.OnItemClickListener;

import com.naiz.eus.R;
import com.naiz.eus.adapter.BerriaListAdapter;
import com.naiz.eus.db.DatabaseHandler;
import com.naiz.eus.model.Berria;
import com.naiz.eus.model.NavDrawerItem;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.Bitmap.CompressFormat;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class AlbisteFragment extends ListFragment implements
		OnItemClickListener {
	private String searchURL = "http://www.naiz.eus/";
	public ArrayList<Berria> berriLista;
	public ArrayList<Berria> berriLista2;
	public ArrayList<String> LinkLista;
	private String titularra = "";
	private ImageView logoa;
	private TextView TitView;
	private DatabaseHandler db;
	private ProgressBar dialog;
	private String saila="azala";

	public AlbisteFragment() {
	}

	public AlbisteFragment(String tit, String link) {
		titularra = tit;
		searchURL = link;
		saila = tit;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		MainActivity.hasieran = true;
		View rootView;
		rootView = inflater
				.inflate(R.layout.fragment_albiste, container, false);
		TitView = (TextView) rootView.findViewById(R.id.txtLabela);
		logoa = (ImageView) rootView.findViewById(R.id.img_logo);
		dialog = (ProgressBar) rootView.findViewById(R.id.progressBar1);
		berriLista = new ArrayList<Berria>();
		berriLista2 = new ArrayList<Berria>();
		LinkLista = new ArrayList<String>();
		db = new DatabaseHandler(getActivity());
		try {
			db.createDataBase();
			db.close();
		} catch (IOException e1) {
			e1.printStackTrace();
			System.out.println("gaizki db");
		}
		AsinkTask thread = new AsinkTask();
		URL url = null;
		thread.execute(url);
		return rootView;
	}

	private class AsinkTask extends AsyncTask<URL, Integer, Long> {
		Bitmap img;
		Document doc = null;
		BerriaListAdapter adapter;
		String naizEguneratzea;
		String dbEguneratzea;
		Boolean eguneratuBeharra=false;

		@Override
		protected void onPostExecute(Long result) {
			super.onPostExecute(result);
			if (AlbisteFragment.this.getActivity() == null)
				return;
			if (dialog.isShown()) {
				dialog.setVisibility(View.INVISIBLE);
			}
			if(eguneratuBeharra){
			berriLista=berriLista2;
			LinkLista.clear();
			for (int i = 0; i < berriLista.size(); i++) {
				String link = berriLista.get(i).getLink();
				LinkLista.add(i, link);
			}
			adapter = new BerriaListAdapter(getActivity(),berriLista);
			setListAdapter(adapter);
			
			if (AlbisteFragment.this.isVisible()) {
				getListView().setOnItemClickListener(AlbisteFragment.this);
			}
			}
			FragmentManager fm = getFragmentManager();
			if (AlbisteFragment.this.isVisible()) {
				AlbisteFragment fragment = (AlbisteFragment) fm
						.findFragmentById(R.id.frame_container);
				if (fragment.isVisible()) {
					fragment.setTextInTextView(img, titularra);
				}
			}
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if(MainActivity.hasieran){
			berriLista=new ArrayList<Berria>();
			try {
				LortuBerriakdb();
			} catch (ParseException e) {
				e.printStackTrace();
			}
			adapter = new BerriaListAdapter(getActivity(), berriLista);
			setListAdapter(adapter);}
			dialog.setProgress(0);
			dialog.setMax(100);
			
		}

		protected Long doInBackground(URL... urls) {
			if(MainActivity.hasieran){
				for (int i = 0; i < berriLista.size(); i++) {
					String link = berriLista.get(i).getLink();
					LinkLista.add(i, link);
				}
				if (AlbisteFragment.this.isAdded()) {
					getListView().setOnItemClickListener(AlbisteFragment.this);
				}
			// ///////////////////////////////////////////
			}
			lortuBerriakInternet();
			dialog.setProgress(100);
			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... progress) {
			super.onProgressUpdate(progress);
			if (dialog != null) {
				dialog.setProgress(progress[0]);
			}
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
				albisteak = db.getBerriak(saila);
				dbEguneratzea = db.getEguneratzeData(saila);
				//System.out.println("dbEguneratzea1 "+dbEguneratzea );
				
				Date today=new Date();
				SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd hh.mm");
				int aurreratua = dbEguneratzea.compareTo(ft.format(today));
			//	System.out.println("aurreratua1 "+aurreratua  );
				if (aurreratua>0){//ondo>
					String sEguna = dbEguneratzea.substring(8, 10);
				//	System.out.println("sEguna1 "+sEguna  );
					Integer e = Integer.valueOf(sEguna);
					e--;
					String se=e.toString();
					if(e<10){
						se="0"+se;
					}
				//	System.out.println("e1 "+e  );
					dbEguneratzea=dbEguneratzea.substring(0, 8)+se+dbEguneratzea.substring(10,dbEguneratzea.length());
				//	System.out.println("dbEguneratzea2 "+dbEguneratzea);
				}
				
			} catch (SQLiteException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(dbEguneratzea==null){
				db.setEguneratzeData("2015-01-01 00.00",saila);
				dbEguneratzea="2015-01-01 00.00";
			}
			if (albisteak != null) {
				MainActivity.albisteKop = albisteak.size();
				for (int i = 0; i < albisteak.size(); i++) {
					Berria p = albisteak.get(i);
					berriLista.add(p);
					System.out.println("LortuBerriakdb: "+i);
				}
			} else {
				System.out.println("albisteak hutsak");
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
				db.berriakHustu(saila);
			} catch (SQLiteException e) {
				e.printStackTrace();
			}
		}

		private void lortuBerriakInternet() {
			URL imageUrl = null;
			HttpURLConnection conn = null;
			try {
				imageUrl = new URL("http://www.naiz.eus/images/logo.png");
				conn = (HttpURLConnection) imageUrl.openConnection();
				conn.connect();
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 2; // el factor de escala a minimizar la
											// imagen, siempre es potencia de 2
				img = BitmapFactory.decodeStream(conn.getInputStream(),
						new Rect(0, 0, 0, 0), options);

			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				int k = 0;
				while (k < 30 && doc == null) {
					doc = Jsoup.connect(searchURL).get();
					k++;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (doc != null) {
				// Connect to the web site
				//if (titularra == "") {
					Elements izenburua = doc.select("ul[id*=nav-menu-logo]");
					Elements titu = izenburua.select("li");
					titularra = titu.get(0).text();// lista osoaren titulua
													// ezarri
				//}
				String[] titularZatiak = titularra.split(" ");
				naizEguneratzea = titularZatiak[titularZatiak.length-1];
				
				System.out.println("naizEguneratzea "+naizEguneratzea);
				System.out.println("dbEguneratzea "+ dbEguneratzea);
				Date today=new Date();
				SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd");
				
				naizEguneratzea = ft.format(today)+" "+naizEguneratzea;
				//System.out.println("Current Date: " + ft.format(today));
				int s = dbEguneratzea.compareTo(naizEguneratzea);
				System.out.println("s: "+s+" dbEguneratzea:"+dbEguneratzea+" naizEguneratzea"+naizEguneratzea);
				
				if(s<0||(saila!="azala")){
					System.out.println("sBARRU: "+s+" dbEguneratzea:"+dbEguneratzea+" naizEguneratzea"+naizEguneratzea);
					eguneratuBeharra=true;
					db.setEguneratzeData(naizEguneratzea,saila);
					if (MainActivity.hasieran){
					berriTaulaHustu();
					}
				Elements albisteak = doc.select("div.article");
				// menuan kopurua ezarri
				MainActivity.albisteKop = albisteak.size();
				if (AlbisteFragment.this.isVisible()) {
					String[] navMenuTitles = getResources().getStringArray(
							R.array.nav_drawer_items);
					MainActivity.navDrawerItems.set(0, new NavDrawerItem(
							navMenuTitles[0], R.drawable.ic_home, true,
							(Integer.toString(albisteak.size()))));
				}

				for (int i = 0; i < albisteak.size(); i++) {
					publishProgress((Integer) ((100 / albisteak.size()) * i));
					Elements albiste_izenb = albisteak.get(i).select(
							"div[class*=title]");
					Elements albiste_desk = albisteak.get(i).select(
							"div[class*=abstract]");
					Elements albiste_info = albisteak.get(i).select(
							"div[class*=extra-info]");
					Elements albiste_saila = albisteak.get(i).select(
							"span[class*=section]");
					Elements albiste_irudiak = albisteak.get(i).select("img");
					Berria p = new Berria();
					String text_a_izena = "";
					if (albiste_izenb.first() != null) {
						text_a_izena = albiste_izenb.first().text();
					}
					p.setTitle(text_a_izena.trim());
					String albiste_linka = "";
					String weba = "";
					Elements albiste_linkak = albiste_izenb.select("a");
					if (albiste_izenb.first() != null) {
						albiste_linka = albiste_linkak.first().attr("href");
						if (albiste_linka.length() > 1) {
							if (albiste_linka.startsWith("/")) {
								weba = "http://www.naiz.eus";
							}
						}
						p.setLink(weba + albiste_linka);
					}

					String text_albiste_desk = "";
					if (albiste_desk.first() != null) {
						text_albiste_desk = albiste_desk.first().text();
					}
					p.setSubtitle(text_albiste_desk.trim());
					String albiste_irudia = "";
					if (albiste_irudiak.first() != null) {

						albiste_irudia = albiste_irudiak.first().attr("src");
						if (albiste_irudia.startsWith("/")) {
							albiste_irudia = "http://www.naiz.eus"
									+ albiste_irudia;
						}
						Bitmap bm = MainActivity.getBitmapFromURL(albiste_irudia);
						p.setImage(bm);
					}
					Bitmap bm = MainActivity.getBitmapFromURL(albiste_irudia);
					p.setImage(bm);

					String Info = albiste_info.text();
					p.setExtraInfo(Info);

					String text_albiste_saila = saila;
					if (albiste_saila.first() != null) {
						text_albiste_saila = albiste_saila.first().text();
					}
					p.setSaila(text_albiste_saila);
					String albiste_sail_link = "";
					p.setSailLinka(searchURL);
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
						p.setSailLinka(weba + albiste_linka);
					}
					berriLista2.add(p);
					 ByteArrayOutputStream stream = new ByteArrayOutputStream();
					 if (bm!=null){
					 bm.compress(CompressFormat.PNG, 0, stream);}
					try {
						System.out.println("db.SartuBerriaOK: "+p.getTitle());
						db.SartuBerria(saila,text_a_izena, text_albiste_desk, Info, text_albiste_saila, "", stream.toByteArray(), p.getLink(),p.getSailLinka());
					} catch (SQLiteException e) {
						System.out.println("db.SartuBerria1: "+e.getMessage());
						e.printStackTrace();
					} catch (IOException e) {
						System.out.println("db.SartuBerria2: "+e.getMessage());
						e.printStackTrace();
					}
					// System.out.println("linka: "+p.getLink()+" desk: "+p.getSubtitle()+" irudia: "+albiste_irudia+" saila:"+p.getSaila());
					// Toast.makeText(getActivity().getApplicationContext(),"partido "+spartido,Toast.LENGTH_SHORT).show();
				}
				}
			}
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// System.out.println("AlbisteFragment - onItemClick:linklista.size="+LinkLista.size());
		MainActivity.hasieran = false;
		String link = berriLista.get(position).getLink();
		if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1) {
			FragmentManager fragmentManager = getActivity()
					.getSupportFragmentManager();
			fragmentManager
					.beginTransaction()
					.replace(R.id.frame_container,
							AlbisteBatFragment.newInstance(link)).commit();
		} else {
			Sample s = new Sample(R.string.titulua, ScreenSlideActivity.class);
			Intent i = new Intent(getActivity(), s.activityClass);
			i.putStringArrayListExtra("Linkak", LinkLista);
			i.putExtra("pos", position);
			startActivity(i);
		}
	}

	public static Fragment newInstance(String tit, String link) {
		AlbisteFragment fragment = new AlbisteFragment(tit, link);
		return fragment;
	}

	public void setTextInTextView(Bitmap img, String titular) {
		logoa.setImageBitmap(img);
		TitView.setText(titular);
	}

	/**
	 * This class describes an individual sample (the sample title, and the
	 * activity class that demonstrates this sample).
	 */
	private class Sample {
		private CharSequence title;
		private Class<? extends Activity> activityClass;

		public Sample(int titleResId, Class<? extends Activity> activityClass) {
			this.activityClass = activityClass;
			this.title = getResources().getString(titleResId);
		}

		@Override
		public String toString() {
			return title.toString();
		}
	}

}
