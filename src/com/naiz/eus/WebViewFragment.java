package com.naiz.eus;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.naiz.eus.R;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class WebViewFragment extends Fragment {

	private static String searchURL;
	private String titulua;
	private String html;

	public WebViewFragment(){}
	
	public WebViewFragment(String link) {
	// TODO Auto-generated constructor stub
		 searchURL=link;
}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_webview, container, false);
		final WebView webView = (WebView) rootView.findViewById(R.id.webView1);
		final TextView titView = (TextView) rootView.findViewById(R.id.txtLabel);
		
		ThreadClass thread = new ThreadClass(this);
		thread.start(); 
		//wait for thread to finish
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		titView.setText(titulua);
		WebSettings settings = webView.getSettings();
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
		settings.setDefaultTextEncodingName("utf-8");
	//	webView.loadData(html, "text/html", "utf-8");
        webView.getSettings().setJavaScriptEnabled(true);
		webView.setWebViewClient(new WebViewClient());
		webView.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);

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
    			doc = Jsoup.connect(searchURL).get();
    		
    		// Connect to the web site
    		Elements izenburua = doc.select("title");
    		titulua=izenburua.text();//lista osoaren titulua ezarri
    		System.out.println("titulua: "+titulua);
            Elements contain = doc.select("div[id*=content-section]");
            html=contain.outerHtml();
            System.out.println("html: "+html);
            } catch (IOException e1) {
    			e1.printStackTrace();
    		}
        }
 	}

	public static Fragment newInstance() {
		WebViewFragment fragment = new WebViewFragment();
        return fragment;  
	}

	public static Fragment newInstance(String link) {
		WebViewFragment fragment = new WebViewFragment(link);
        return fragment;  
	}
}
