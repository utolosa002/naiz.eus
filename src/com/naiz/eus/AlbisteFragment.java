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
import com.naiz.eus.model.NavDrawerItem;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

public class AlbisteFragment extends ListFragment implements OnItemClickListener {
	private String searchURL = "http://www.naiz.eus/";
	public ArrayList<Berria> berriLista;
	public ArrayList<String> LinkLista;
	private String titularra = "";
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
		LinkLista= new ArrayList<String>();
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
		        if (AlbisteFragment.this.getActivity() == null)
	                return;
		        if (dialog.isShowing()){
		        dialog.dismiss();
		        }
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
				
				for(int i=0;i<berriLista.size();i++){
					String link = berriLista.get(i).getLink();
					LinkLista.add(i, link);
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
    			while (k<30 && doc==null){
    			doc = Jsoup.connect(searchURL).get();
    			k++;
    			}
    		// Connect to the web site
    		if (titularra==""){
    		Elements izenburua = doc.select("ul[id*=nav-menu-logo]");
    		Elements titu = izenburua.select("li");
    		titularra=titu.get(0).text();//lista osoaren titulua ezarri
    		}
            Elements albisteak = doc.select("div.article");
            //menuan kopurua ezarri
            if(AlbisteFragment.this.isVisible()){
            String[] navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
            MainActivity.navDrawerItems.set(0, new NavDrawerItem(navMenuTitles[0], R.drawable.ic_home, true, (Integer.toString(albisteak.size()))));
            }
            MainActivity.albisteKop=albisteak.size();
            
        	for (int i=0;i<albisteak.size();i++) {
        		publishProgress((Integer)((100/albisteak.size())*i));
                Elements albiste_izenb =albisteak.get(i).select("div[class*=title]");
                Elements albiste_desk = albisteak.get(i).select("div[class*=abstract]");
                Elements albiste_info = albisteak.get(i).select("div[class*=extra-info]");
                Elements albiste_saila = albisteak.get(i).select("span[class*=section]");
                Elements albiste_irudiak = albisteak.get(i).select("img");
                Berria p=new Berria();
        		String text_a_izena="";
        		if(albiste_izenb.first()!=null){
        			text_a_izena = albiste_izenb.first().text();
        			}
        		p.setTitle(text_a_izena);
        		String albiste_linka="";
        		String weba="";
        		Elements albiste_linkak=albiste_izenb.select("a");
        		if(albiste_izenb.first()!=null){
        			albiste_linka = albiste_linkak.first().attr("href");
        			if(albiste_linka.length()>1){
        				if(albiste_linka.startsWith("/")){
        					weba="http://www.naiz.eus";
        				}
        			}
					p.setLink(weba+albiste_linka);
        		}
 
        		String text_albiste_desk="";
        		if(albiste_desk.first()!=null){
        		text_albiste_desk = albiste_desk.first().text();
        		}
        		p.setSubtitle(text_albiste_desk);
        		String albiste_irudia="";
        		if(albiste_irudiak.first()!=null){
        			
        			albiste_irudia	= albiste_irudiak.first().attr("src");
        			if (albiste_irudia.startsWith("/")){
        				albiste_irudia="http://www.naiz.eus"+albiste_irudia;
        			}
        			Bitmap bm = MainActivity.getBitmapFromURL(albiste_irudia);
            		p.setImage(bm);
        		}
        		Bitmap bm = MainActivity.getBitmapFromURL(albiste_irudia);
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
					p.setSailLinka(weba+albiste_linka);
        		}
        		berriLista.add(p);
            //  System.out.println("linka: "+p.getLink()+" desk: "+p.getSubtitle()+" irudia: "+albiste_irudia+" saila:"+p.getSaila());
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
		//System.out.println("AlbisteFragment - onItemClick:linklista.size="+LinkLista.size());
		MainActivity.hasieran=false;
		String link = berriLista.get(position).getLink();
		if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1) {
			FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
			fragmentManager.beginTransaction()
	  		     .replace(R.id.frame_container, AlbisteBatFragment.newInstance(link))
	  		     .commit();
		}else{
			Sample s= new Sample(R.string.titulua, ScreenSlideActivity.class);
			Intent i=new Intent(getActivity(),s.activityClass);
			i.putStringArrayListExtra("Linkak", LinkLista);
			i.putExtra("pos", position);
			startActivity(i);
		}
	}

	public static Fragment newInstance(String tit,String link) {
			AlbisteFragment fragment = new AlbisteFragment(tit, link);
	        return fragment;  
	}
	public void setTextInTextView(Bitmap img, String titular) {
		logoa.setImageBitmap(img);
		TitView.setText(titular);
	}
    /**
     * This class describes an individual sample (the sample title, and the activity class that
     * demonstrates this sample).
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
