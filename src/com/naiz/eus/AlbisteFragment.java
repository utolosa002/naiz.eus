package com.naiz.eus;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.widget.AdapterView.OnItemClickListener;

import com.naiz.eus.R;
import com.naiz.eus.adapter.BerriaListAdapter;
import com.naiz.eus.model.Berria;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

public class AlbisteFragment extends ListFragment implements OnItemClickListener {
	private String searchURL="http://www.naiz.eus/";
	private ArrayList<Berria> berriLista;
	private String titularra="";
	private ImageView logoa; 
	private TextView TitView;
	
	public AlbisteFragment(){}
	
	public AlbisteFragment(String tit,String link) {
		titularra=tit;
		searchURL=link;
	}
    
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		MainActivity.hasieran=true;
		View rootView;
        rootView = inflater.inflate(R.layout.fragment_albiste, container, false);
        TitView = (TextView) rootView.findViewById(R.id.txtLabela);
		logoa = (ImageView) rootView.findViewById(R.id.img_logo);
		berriLista=new ArrayList<Berria>();
		AsinkTask thread = new AsinkTask();
		URL url = null;
		thread.execute(url);
		return rootView;
    }
	 private class AsinkTask extends AsyncTask<URL, Integer, Long> {
		 private ProgressDialog dialog = new ProgressDialog(AlbisteFragment.this.getActivity());
		 Bitmap img;
		    @Override
		    protected void onPostExecute(Long result) {            
		        super.onPostExecute(result);
		        dialog.dismiss();
		       
		        BerriaListAdapter adapter = new BerriaListAdapter(getActivity(), berriLista);
		        setListAdapter(adapter);
		        if(AlbisteFragment.this.isVisible()){
		        	getListView().setOnItemClickListener(AlbisteFragment.this);
		        }
		        FragmentManager fm = getFragmentManager();
		        if(AlbisteFragment.this.isVisible()){
		        	AlbisteFragment fragment = (AlbisteFragment)fm.findFragmentById(R.id.frame_container);
		        	if(fragment.isVisible()){
		        		fragment.setTextInTextView(img,titularra);
		        	}
	            }
		    }
		
		    @Override
		    protected void onPreExecute() {
		        super.onPreExecute();
		        dialog.setMessage("Albisteak lortzen...");
		    	dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				dialog.setProgress(0);
				dialog.setMax(100);
				dialog.show();
		    }
	     protected Long doInBackground(URL... urls) {
	    	 URL imageUrl = null;
			 HttpURLConnection conn = null;		 
			 try {
			 imageUrl = new URL("http://www.naiz.eus/images/logo.png");
			 conn = (HttpURLConnection) imageUrl.openConnection();
			 conn.connect();
			 BitmapFactory.Options options = new BitmapFactory.Options();
			 options.inSampleSize = 2; // el factor de escala a minimizar la imagen, siempre es potencia de 2
		   	 img = BitmapFactory.decodeStream(conn.getInputStream(), new Rect(0, 0, 0, 0), options);
			 
			 } catch (IOException e) {
			 e.printStackTrace();
			 }
    		Document doc = null;
    		try {
    			int k=0;
    			while (k<5 && doc==null){
    			doc = Jsoup.connect(searchURL).get();
    			k++;
    			}
    		// Connect to the web site
    		if (titularra==""){
    		Elements izenburua = doc.select("ul[id*=nav-menu-logo]");
    		Elements titu = izenburua.select("li");
    		titularra=titu.get(0).text();//lista osoaren titulua ezarri
    		}
            Elements produktuak = doc.select("div.article");
            
            
        	for (int i=0;i<produktuak.size();i++) {
        		publishProgress((Integer)((100/produktuak.size())*i));
                Elements produktu_izenb = produktuak.get(i).select("div[class*=title]");
                Elements produktu_desk = produktuak.get(i).select("div[class*=abstract]");
                Elements albiste_info = produktuak.get(i).select("div[class*=extra-info]");
                Elements albiste_saila = produktuak.get(i).select("span[class*=section]");
                Elements produktu_irudiak = produktuak.get(i).select("img");
                Berria p=new Berria();
        		String text_p_izena="";
        		if(produktu_izenb.first()!=null){
        			text_p_izena = produktu_izenb.first().text();
        			}
        		p.setTitle(text_p_izena);
        		String produktu_linka="";
        		String weba="";
        		Elements produktu_linkak=produktu_izenb.select("a");
        		if(produktu_izenb.first()!=null){
        			produktu_linka = produktu_linkak.first().attr("href");
        			if(produktu_linka.length()>1){
        				if(produktu_linka.startsWith("/")){
        					weba="http://www.naiz.eus";
        				}
        			}
					p.setLink(weba+produktu_linka);
        		}
 
        		String text_produktu_desk="";
        		if(produktu_desk.first()!=null){
        		text_produktu_desk = produktu_desk.first().text();
        		}
        		p.setSubtitle(text_produktu_desk);
        		String produktu_irudia="";
        		if(produktu_irudiak.first()!=null){
        			
        			produktu_irudia	= produktu_irudiak.first().attr("src");
        			if (produktu_irudia.startsWith("/")){
        				produktu_irudia="http://www.naiz.eus"+produktu_irudia;
        			}
        			Bitmap bm = MainActivity.getBitmapFromURL(produktu_irudia);
            		p.setImage(bm);
        		}
        		Bitmap bm = MainActivity.getBitmapFromURL(produktu_irudia);
        		p.setImage(bm);
        		
        		String Info= albiste_info.text();
        		p.setExtraInfo(Info);
        		
        		String text_albiste_saila="";
        		if(albiste_saila.first()!=null){
        			text_albiste_saila= albiste_saila.first().text();
        		}
        		p.setSaila(text_albiste_saila);
        		String albiste_sail_link="";
        		weba="";
        		Elements albiste_sail_linkE=albiste_saila.select("a");
        		if(albiste_sail_linkE.first()!=null){
        			albiste_sail_link = albiste_sail_linkE.first().attr("href");
        			if(albiste_sail_link.length()>1){
        				if(albiste_sail_link.startsWith("/")){
        					weba="http://www.naiz.eus";
        				}
        			}
					p.setSailLinka(weba+produktu_linka);
        		}
        		berriLista.add(p);
              System.out.println("linka: "+p.getLink()+" desk: "+p.getSubtitle()+" irudia: "+produktu_irudia+" saila:"+p.getSaila());
            //  Toast.makeText(getActivity().getApplicationContext(),"partido "+spartido,Toast.LENGTH_SHORT).show();
            }
    		} catch (IOException e1) {
    			e1.printStackTrace();
       			System.out.println("errorea albisteak lortzean");
       			try {
    				doInBackground(new URL(null));
    			} catch (IOException e) {
    				// TODO Auto-generated catch block
    				System.out.println("errorea albisteak lortzean2");
    				e.printStackTrace();
    			}
    		}
			return null;
	     }
	     @Override
	     protected void onProgressUpdate(Integer... progress) {
	    	 super.onProgressUpdate(progress);
	         if (dialog != null) {
	            dialog.setProgress(progress[0]);
	         }
	     }
	}

	@Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			String link = berriLista.get(position).getLink();
			MainActivity.hasieran=false;
		  		  FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
		        	 fragmentManager.beginTransaction()
		  		     .replace(R.id.frame_container, AlbisteBatFragment.newInstance(link))
		  		     .commit();
	}

	public static Fragment newInstance(String tit,String link) {
			AlbisteFragment fragment = new AlbisteFragment(tit, link);
	        return fragment;  
	}
	public void setTextInTextView(Bitmap img, String titular) {
		logoa.setImageBitmap(img);
		TitView.setText(titular);
	}
}
