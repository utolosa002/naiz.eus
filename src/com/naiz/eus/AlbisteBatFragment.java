package com.naiz.eus;

import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import com.naiz.eus.R;
import com.naiz.eus.model.Berria;
import android.support.v4.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

public class AlbisteBatFragment extends Fragment{

	private static String searchURL;
	private Berria b=new Berria();
	public static WebView Berriatxt;
	public AlbisteBatFragment(){}
	
	public AlbisteBatFragment(String link) {
	// TODO Auto-generated constructor stub
		 searchURL=link;
	}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.berri_bat, container, false);
        final TextView Saila = (TextView) rootView.findViewById(R.id.textSaila);
        final TextView Azpitit = (TextView) rootView.findViewById(R.id.textAzpitit);
        Berriatxt= (WebView) rootView.findViewById(R.id.berriatxt);
        final TextView ExtraInfo = (TextView) rootView.findViewById(R.id.textnaiz);
        final TextView Titularra = (TextView) rootView.findViewById(R.id.textTitularra);
        final ImageView Irudia = (ImageView) rootView.findViewById(R.id.berrirudia);
    
        
		ThreadClass thread = new ThreadClass(this);
		thread.start(); 
		//wait for thread to finish
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Saila.setText(b.getSaila());
		Azpitit.setText(b.getSubtitle());
		Titularra.setText(b.getTitle());
		ExtraInfo.setText(b.getExtraInfo().replaceAll("||", " | "));
		
		WebSettings settings = Berriatxt.getSettings();
        settings.setSupportZoom(false);
        settings.setBuiltInZoomControls(false);
		settings.setDefaultTextEncodingName("utf-8");
		settings.setDefaultFontSize(MainActivity.testutamaina);
		Berriatxt.getSettings().setJavaScriptEnabled(true);
		Berriatxt.setBackgroundColor(Color.TRANSPARENT);
//		Berriatxt.setWebViewClient(new WebViewClient());
//		Berriatxt.setWebChromeClient(new WebChromeClient());
		b.setBerria(b.getBerria().replaceAll("href=\"/", "href=\"http://www.naiz.eus/"));
		b.setBerria(b.getBerria().replaceAll("src=\"/", "src=\"http://www.naiz.eus/"));
		//TODO INTENT BERRIA SORTU BERRIAREKIN
		String html = "<html><body style='text-align:justify;'>"+ b.getBerria()+"</body></html>";
		Berriatxt.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);
		
		//Berria.setText(b.getBerria());
		Irudia.setImageBitmap(b.getImage());

        return rootView;
        
    }
	class ThreadClass extends Thread {
		Fragment  cl;
	    public ThreadClass(Fragment cl){
	       this.cl = cl;
	    }
		public void run() {
    		Document doc = null;
    		 System.out.println("searchURL: "+searchURL);
    		try {
    			int i=0;
    			while (i<50 && doc==null){
    			doc = Jsoup.connect(searchURL).get();
    			i++;
    			}
    		// Connect to the web site
    	if (doc!=null){
            Elements produktu_izenb =doc.select("div[class^=title]");
            Elements produktu_desk = doc.select("div[class*=abstract]");
            Elements albiste_info = doc.select("div[class*=extra-info]");
            Elements albiste_saila = doc.select("span[class=section]");
            Elements irudidiv = doc.select("div[class*=big-photo]");
            Elements berriaNaiz = doc.select("div[class*=report-text]");
            Elements berriaGara = doc.select("div[class*=ART_BODY]");
            Elements produktu_irudiak = irudidiv.select("img");
            
    		String text_p_izena="";
    		if(produktu_izenb.first()!=null){
    			text_p_izena = produktu_izenb.first().text();
    			}
    		b.setTitle(text_p_izena);
    		String produktu_linka="";
    		String weba="";
    		Elements produktu_linkak=produktu_izenb.select("a");
    		if(produktu_linkak.first()!=null){
    			produktu_linka = produktu_linkak.first().attr("href");
    			if(produktu_linka.length()>1){
    				if(produktu_linka.startsWith("/")){
    					weba="http://www.naiz.eus";
    				}
    			}
				b.setLink(weba+produktu_linka);
    		}

    		String text_produktu_desk="";
    		if(produktu_desk.first()!=null){
    		text_produktu_desk = produktu_desk.first().text();
    		}
    		b.setSubtitle(text_produktu_desk);
    		String produktu_irudia="";
    		if(produktu_irudiak.first()!=null){	
    			produktu_irudia	= produktu_irudiak.first().attr("src");
    			if (produktu_irudia.startsWith("/")){
    				produktu_irudia="http://www.naiz.eus"+produktu_irudia;
    			}
    			Bitmap bm = MainActivity.getBitmapFromURL(produktu_irudia);
    			b.setImage(bm);
    		}
    		
    		String Info= albiste_info.text();
    		b.setExtraInfo(Info);
    		
    		String text_albiste_saila="";
    		if(albiste_saila.first()!=null){
    			text_albiste_saila= albiste_saila.first().text();
    		}
    		b.setSaila(text_albiste_saila);
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
				b.setSailLinka(weba+produktu_linka);
    		}
    		String html_berria="";
    		if(berriaNaiz.first()!=null){
    			html_berria = berriaNaiz.first().outerHtml();
    		}else if(berriaGara.first()!=null){
    			html_berria = berriaGara.first().outerHtml();
    		}
    		if (html_berria==null||html_berria==""){
                Elements erosi = doc.select("div[class*=span-6 last]");
                html_berria = erosi.first().outerHtml();
    		}
    		b.setBerria(html_berria);
    		}
	
    		} catch (IOException e1) {
			e1.printStackTrace();
		}	
    	}
 	}

	public static Fragment newInstance() {
		AlbisteBatFragment fragment = new AlbisteBatFragment();
        return fragment;  
	}

	public static Fragment newInstance(String link) {
		AlbisteBatFragment fragment = new AlbisteBatFragment(link);
        return fragment;
	}
}
