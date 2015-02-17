package com.naiz.eus;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.webkit.WebSettings;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView.OnItemClickListener;

import com.naiz.eus.R;
import com.naiz.eus.adapter.BerriaListAdapter;
import com.naiz.eus.adapter.SailListAdapter;
import com.naiz.eus.model.Berria;
import com.naiz.eus.model.NavDrawerItem;
import com.naiz.eus.model.Saila;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.annotation.SuppressLint;
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
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class EguraldiaFragment extends ListFragment implements OnItemClickListener {
	private String searchURL = "http://www.naiz.eus/eu/eguraldia/euskal-herria";
	private ArrayList<String> herrialdeLista;
	private ArrayList<Saila> herriLista;
	private String herria="anana", herrialdea;
	private String html;
	private String titularra = "";
	private ImageView logoa; 
	private TextView TitView;
	private Spinner s;
	private Integer pos=0;
	private WebView webView;
	private static SailListAdapter listadapter;
	Document doc = null;
	Document doc2 = null;
	Document docarab = null;
	Document docbizk = null;
	Document doclapu = null;
	Document docgip = null;
	Document docnafa = null;
	Document docnafb = null;
	Document doczub = null;
	
	public EguraldiaFragment(){}
    
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_eguraldi_orok, container, false);
		webView = (WebView) rootView.findViewById(R.id.webVieweguraldiorok);
        TitView = (TextView) rootView.findViewById(R.id.txtLabela);
		logoa = (ImageView) rootView.findViewById(R.id.img_logo);
		s = (Spinner) rootView.findViewById(R.id.spinnerlurraldea);
		herrialdea = "araba";
    	System.out.println("hasieran item: "+herrialdea+" selected:"+s.getSelectedItemId());
		herrialdeLista=new ArrayList<String>();
		AsinkTask thread = new AsinkTask();
		URL url = null;
		thread.execute(url);
		return rootView;
    }
	 private class AsinkTask extends AsyncTask<URL, Integer, Long> {
		 private ProgressDialog dialog = new ProgressDialog(EguraldiaFragment.this.getActivity());
		    @SuppressLint("NewApi")
			@Override
		    protected void onPostExecute(Long result) {            
		        super.onPostExecute(result);
		        if (EguraldiaFragment.this.getActivity() == null)
	                return;
		        if (dialog.isShowing()){
		        dialog.dismiss();
		        }
		      
    	       kargatuSpinnerEntzulea();

		        System.out.println("POST: Spinner kargatua");
		        
				WebSettings settings = webView.getSettings();
		        settings.setSupportZoom(true);
		        
		        //NEW APIIIII
		        settings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
		        settings.setBuiltInZoomControls(true);
				settings.setDefaultTextEncodingName("utf-8");
		        webView.getSettings().setJavaScriptEnabled(true);
				webView.setWebViewClient(new WebViewClient());
				html=html.replaceAll("=\"/", "=\"http://www.naiz.eus/");
				webView.loadDataWithBaseURL("file:///android_asset/", html, "text/html", "utf-8", null);

				//System.out.println("POST: Webview kargatua "+ html);
//			        
				 listadapter = new SailListAdapter(getActivity(), herriLista);
				 listadapter.notifyDataSetChanged();
			     setListAdapter(listadapter);
			     System.out.println("POST: ListAdapter");
			     
//				 ArrayAdapter<String> adapterra = new ArrayAdapter<String>(getActivity(), R.array.herrialdespinnerItems,herrialdeLista);// herriLista);
//				 setListAdapter(adapterra);
		        
		        if(EguraldiaFragment.this.isVisible()){
		        	getListView().setOnItemClickListener(EguraldiaFragment.this);
		        }
		    }
		
		    @Override
		    protected void onPreExecute() {
		        super.onPreExecute();
		        
		        herrialdeLista.add("Araba");
		        herrialdeLista.add("Bizkaia");
		        herrialdeLista.add("Gipuzkoa");
		        herrialdeLista.add("Lapurdi");
		        herrialdeLista.add("Nafarroa");
		        herrialdeLista.add("Nafarroa Behera");
		        herrialdeLista.add("Zuberoa");

		       System.out.println("POST: herrialdelista kargatua");
//		        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity().getBaseContext(), R.array.herrialdespinnerItems, herrialdeLista);
    	        ArrayAdapter<?> spinneradapter = ArrayAdapter.createFromResource(getActivity(), R.array.herrialdespinnerItems, android.R.layout.simple_spinner_item);
    	        spinneradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	        s.setAdapter(spinneradapter);
    	        s.setSelection(pos);
		        dialog.setMessage("herriak lortzen...");
		    	dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				dialog.setProgress(0);
				dialog.setMax(100);
				dialog.show();
		    }
	     protected Long doInBackground(URL... urls) {
			doc=null;
    		try {
    			int k=0;
    			while (k<20 && (doc==null||doc2==null)){
        			doc = Jsoup.connect(searchURL+"/araba").get();
        			//doc = Jsoup.connect(searchURL+"/"+herrialdea).get();
        			doc2 = Jsoup.connect(searchURL).get();
    			k++;
    			}
    		// Connect to the web site
    		if (titularra==""){
    		Elements izenburua = doc.select("ul[id*=nav-menu-logo]");
    		Elements titu = izenburua.select("li");
    		titularra=titu.get(0).text();//lista osoaren titulua ezarri
    		}
    		Elements herrilista = null;
    		System.out.println("doc herrialdea:"+herrialdea);
    		switch (pos) {
    		case (0):
    			if (docarab!=null){
    			herrilista= docarab.select("select[id*=weather_location_selector]");
          		System.out.println("docarab");
    			}else{
        		herrilista= doc.select("select[id*=weather_location_selector]");
        		System.out.println("doc");
    			}
    		break;	
    		case (1):
    			System.out.println("docarab");
    			herrilista= docbizk.select("select[id*=weather_location_selector]");
    		break;
    		case (2): 
    			herrilista= docgip.select("select[id*=weather_location_selector]");
    		break;
    		case (3): 
    			herrilista= doclapu.select("select[id*=weather_location_selector]");
    		break;
    		case (4): 
    			herrilista= docnafa.select("select[id*=weather_location_selector]");
    		break;
    		case (5): 
    			herrilista= docnafb.select("select[id*=weather_location_selector]");
    		break;
    		case (6): 
    			herrilista= doczub.select("select[id*=weather_location_selector]");
    		break;
    		default:
    			herrilista= doc.select("select[id*=weather_location_selector]");
    		break;
    		}
            Elements herriak = herrilista.select("option");
            herriLista=new ArrayList<Saila>();
            for (int i=0;i<herriak.size();i++) {
        		publishProgress((Integer)((100/herriak.size())*i));
      		Saila sail =new Saila();
      		sail.setTit(herriak.get(i).text());
      		sail.setLink(herriak.get(i).attr("data-url"));
      		herriLista.add(sail);
      		System.out.println("herriLista: "+i+" "+herriLista.get(i).getTit());
    		
       	}
      	System.out.println("for kanpoan ");
		
//  
        	// Connect to the web site
	    		Elements izenburua = doc2.select("div[class*=map map-col-euskal-herria]");
//	    		titulua=izenburua.text();//lista osoaren titulua ezarri
//	    		System.out.println("titulua: "+titulua);
	    		//sekzioak bakarrik hautatzeko:
	            Elements headerra = doc2.select("head");
	            headerra.append("<LINK href=\"styles.css\" type=\"text/css\" rel=\"stylesheet\"/>");
	            String header=headerra.outerHtml();
	            html=izenburua.outerHtml();
	            
	           // html="<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"eu\" lang=\"eu\">"+header+"<body class=\"especial-page\"><div id=\"content-section\"><div class=\"container\"><div id=\"main-content\"><div class=\"span-18 special tiempo\"><div class=\"body list\"><div class=\"span-14 content\"><div class=\"widget outstanding_article\"><div class=\"tiempo-interior\">"+html+"</div></div></div></div></div></div></div></div></body></html>";
	            html="<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"eu\" lang=\"eu\">"+header+"<body><div class=\"tiempo\"><div class=\"tiempo-interior\">"+html+"</div></div></body></html>";
	    	//html=doc.html();
            // System.out.println("linka: "+p.getLink()+" desk: "+p.getSubtitle()+" irudia: "+albiste_irudia+" saila:"+p.getSaila());
            //  Toast.makeText(getActivity().getApplicationContext(),"partido "+spartido,Toast.LENGTH_SHORT).show();

	    		try {
	    			int l=0;
	    			while (l<20 && (docarab==null||docgip==null||docbizk==null||doclapu==null||docnafa==null||docnafb==null||doczub==null)){
	        			docarab = Jsoup.connect(searchURL+"/araba").get();
	        			docgip = Jsoup.connect(searchURL+"/gipuzkoa").get();
	        			docbizk = Jsoup.connect(searchURL+"/bizkaia").get();
	        			doclapu = Jsoup.connect(searchURL+"/lapurdi").get();
	        			docnafa = Jsoup.connect(searchURL+"/nafarroa").get();
	        			docnafb = Jsoup.connect(searchURL+"/nafarroa-beherea").get();
	        			doczub = Jsoup.connect(searchURL+"/zuberoa").get();
	    			k++;
	    			}
	    		}finally{
	    			
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
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			//String link = searchURL+"/"+herrialdea+"/"+herria;
			String link = herriLista.get(position).getLink();
			MainActivity.hasieran=false;
		  		  FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
		        	 fragmentManager.beginTransaction()
		  		     .replace(R.id.frame_container, EguraldiBatFragment.newInstance(link))
		  		     .commit();
	}

	public void kargatuSpinnerEntzulea() {
		// TODO Auto-generated method stub
		 s.setOnItemSelectedListener(new OnItemSelectedListener() {
	            @SuppressLint("NewApi")
				@Override
	            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
	            	if (herrialdea!=parentView.getItemAtPosition(position).toString()){
	            	herrialdea=parentView.getItemAtPosition(position).toString();
	            	pos =position;
	            	System.out.println("item selected: "+herrialdea);
	            	AsinkTask thread = new AsinkTask();
	        		URL url = null;
	        		thread.execute(url);
	        		}
	            }

	            @Override
	            public void onNothingSelected(AdapterView<?> parentView) {
	                // your code here
	            }

	        });
	}
}
