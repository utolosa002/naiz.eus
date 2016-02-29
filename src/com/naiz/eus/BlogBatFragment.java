package com.naiz.eus;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.naiz.eus.R;
import com.naiz.eus.model.Blog;

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

public class BlogBatFragment extends Fragment {

	private static String searchURL;
	private Blog b=new Blog();
	static WebView postTxt;
	public BlogBatFragment(){}
	
	public BlogBatFragment(String link) {
	// TODO Auto-generated constructor stub
		 searchURL=link;
	}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.blog_bat, container, false);
        final ImageView egileIrudia = (ImageView) rootView.findViewById(R.id.blog_egile_image);
        final TextView egilea = (TextView) rootView.findViewById(R.id.blog_egilea);
        final TextView blogIzena= (TextView) rootView.findViewById(R.id.blog_izena);
        final TextView postTit = (TextView) rootView.findViewById(R.id.blog_post_tit);
        final TextView postData = (TextView) rootView.findViewById(R.id.blog_post_data);
        postTxt = (WebView) rootView.findViewById(R.id.blog_post_txt);
        final ImageView postIrudia = (ImageView) rootView.findViewById(R.id.blog_post_irudia);
		
		ThreadClass thread = new ThreadClass(this);
		thread.start(); 
		//wait for thread to finish
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		egileIrudia.setImageBitmap(b.getEgileImage());
        egilea.setText(b.getEgilea());
        blogIzena.setText(b.getTit());
        postTit.setText(b.getPostTit());
        postData.setText(b.getPostData());

		WebSettings settings = postTxt.getSettings();
        settings.setSupportZoom(false);
        settings.setBuiltInZoomControls(false);
        settings.setDefaultFontSize(MainActivity.testutamaina);
		settings.setDefaultTextEncodingName("utf-8");
		postTxt.getSettings().setJavaScriptEnabled(true);
//		postTxt.setWebViewClient(new WebViewClient());
//		postTxt.setWebChromeClient(new WebChromeClient());
		postTxt.setBackgroundColor(Color.TRANSPARENT);
		if (b.getPostText()!=null){
			b.setPostText(b.getPostText().replaceAll("=\"/", "=\"http://www.naiz.eus/"));
		}else{
			b.setPostText(b.getPostText());
		}
		//TODO INTENT BERRIA SORTU BERRIAREKIN
		String html = "<html><body style='text-align:justify;'>"+ b.getPostText()+"</body></html>";
		postTxt.loadDataWithBaseURL(null,html, "text/html", "utf-8", null);
		
      //  postIrudia     
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
    			while (i<5 && doc==null){
    			doc = Jsoup.connect(searchURL).get();
    			i++;
    			}
    		// Connect to the web site

            Elements egilea =doc.select("div[class^=author]");
            Elements tituluak = doc.select("div[class^=title]");
            Elements extra = doc.select("div[class^=date]");
            Elements header = doc.select("div[class*=blogs-header]");
            Elements egile_irudiak = header.select("img");
            Elements post_osoa = doc.select("div[class=content]");
            //Elements post_irudia = post_osoa.first().select("img");
            
          //blog izena
    		String blog_izena="";
    		if(tituluak.first()!=null){
    			blog_izena = tituluak.first().text();
    			}
    		b.setTit(blog_izena);
    		
    		//blog extra
    		String blog_extra="";
    		if(extra.first()!=null){
    			blog_extra= extra.first().text();
    			}
    		b.setPostData(blog_extra);
    		
    		//post izena
    		String post_izena="";
    		if(tituluak.get(1)!=null){
    			post_izena = tituluak.get(1).text();
    		}
    		b.setPostTit(post_izena);
    		
    		//post testu osoa
    		String text_post_osoa="";
    		if(post_osoa.first()!=null){
    			text_post_osoa = post_osoa.first().outerHtml();
    		}
    		b.setPostText(text_post_osoa);
    		
    		//egile irudia
    		String egile_irudia="";
    		if(egile_irudiak.first()!=null){
    			egile_irudia = egile_irudiak.first().attr("src");
    			if (egile_irudia.startsWith("/")){
    				egile_irudia="http://www.naiz.eus"+egile_irudia;
    			}
    		}
    		Bitmap bm = MainActivity.getBitmapFromURL(egile_irudia);
    		b.setEgileImage(bm);
    		
    		//egile izena
    		b.setEgilea(egilea.first().text());
    		} catch (IOException e1) {
    			e1.printStackTrace();
    		}
    	}
 	}

	public static Fragment newInstance() {
		BlogBatFragment fragment = new BlogBatFragment();
        return fragment;  
	}

	public static Fragment newInstance(String link) {
		BlogBatFragment fragment = new BlogBatFragment(link);
        return fragment;  
	}
}
