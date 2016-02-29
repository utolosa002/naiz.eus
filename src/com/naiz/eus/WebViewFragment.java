package com.naiz.eus;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.naiz.eus.R;

import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.AsyncTask;
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
	public TextView titView;
	public WebView webView;

	public WebViewFragment(){}
	
	public WebViewFragment(String link) {
	// TODO Auto-generated constructor stub
		 searchURL=link;
}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_webview, container, false);
		webView = (WebView) rootView.findViewById(R.id.webView1);
		titView = (TextView) rootView.findViewById(R.id.txtLabel);
		AsinkTask thread = new AsinkTask();
		URL url = null;
		thread.execute(url);
        return rootView;
    }


	public static Fragment newInstance() {
		WebViewFragment fragment = new WebViewFragment();
        return fragment;  
	}

	public static Fragment newInstance(String link) {
		WebViewFragment fragment = new WebViewFragment(link);
        return fragment;  
	}
	private class AsinkTask extends AsyncTask<URL, Integer, Long> {
		 private ProgressDialog dialog = new ProgressDialog(WebViewFragment.this.getActivity());
		 Bitmap img;
		    @Override
		    protected void onPostExecute(Long result) {            
		        super.onPostExecute(result);
		        if (WebViewFragment.this.getActivity() == null)
	                return;
		        if (dialog.isShowing()){
		        	dialog.dismiss();
		        }
		        titView.setText(titulua);
				WebSettings settings = webView.getSettings();
		        settings.setSupportZoom(true);
		        settings.setBuiltInZoomControls(true);
				settings.setDefaultTextEncodingName("utf-8");
			//	webView.loadData(html, "text/html", "utf-8");
		        webView.getSettings().setJavaScriptEnabled(true);
				webView.setWebViewClient(new WebViewClient());

				html=html.replaceAll("=\"/", "=\"http://www.naiz.eus/");
				webView.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);
		    }
		
		    @Override
		    protected void onPreExecute() {
		        super.onPreExecute();
		        dialog.setMessage("Orria lortzen...");
		    	dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
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
	    		System.out.println("searchURL: "+searchURL);
	    		try {
	    	   		int k=0;
	    			while (k<100 && doc==null){
	    				doc = Jsoup.connect(searchURL).get();
	    				k++;
	    			}
	    		
	    			// Connect to the web site
	    			Elements izenburua = doc.select("title");
	    			titulua=izenburua.text();//lista osoaren titulua ezarri
	    			System.out.println("titulua: "+titulua);
	    			//sekzioak bakarrik hautatzeko:
	    			// Elements contain = doc.select("div[id*=content-section]");
	    			// html=contain.outerHtml();
	    			html=doc.html();
	    			System.out.println("html: "+html);
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
	}
}