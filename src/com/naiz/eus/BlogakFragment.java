package com.naiz.eus;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.naiz.eus.R;
import com.naiz.eus.adapter.BlogListAdapter;
import com.naiz.eus.model.Blog;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class BlogakFragment extends ListFragment implements OnItemClickListener {
	public static ImageView img = null;
	private String searchURL;
	private ArrayList<Blog> blogLista;
	private String titularra;
	private TextView TitView;

	public BlogakFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_blogak, container, false);
		searchURL = "http://www.naiz.eus/eu/iritzia/blogs";
		TitView = (TextView) rootView.findViewById(R.id.txtblogak);
		TitView.setText("blogak kargatzen...");
		blogLista=new ArrayList<Blog>();
		AsinkTask thread = new AsinkTask();
		URL url = null;
		thread.execute(url);
		
        return rootView;
        
    }
	
	private class AsinkTask extends AsyncTask<URL, Integer, Long> {
		 private ProgressDialog dialog = new ProgressDialog(BlogakFragment.this.getActivity());
		    @Override
		    protected void onPostExecute(Long result) {            
		        super.onPostExecute(result);
		        dialog.dismiss();
		        BlogListAdapter adapter = new BlogListAdapter(getActivity(), blogLista);
		        setListAdapter(adapter);
		        if(BlogakFragment.this.isVisible()){
		        	getListView().setOnItemClickListener(BlogakFragment.this);
		        }
		        FragmentManager fm = getFragmentManager();
		        if(BlogakFragment.this.isVisible()){
		        	BlogakFragment fragment = (BlogakFragment)fm.findFragmentById(R.id.frame_container);
		        	if(fragment.isVisible()){
		        		fragment.setTitle(titularra);
		        	}
	            }    
		    }
		
		    @Override
		    protected void onPreExecute() {
		        super.onPreExecute();
		        dialog.setMessage("Blogak lortzen...");
		    	dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				dialog.setProgress(0);
				dialog.setMax(100);
		        dialog.show();
		    }
	     protected Long doInBackground(URL... urls) {
  		Document doc = null;
  		try {
  			int i=0;
			while (i<30 && doc==null){
			doc = Jsoup.connect(searchURL).get();
			i++;
			}
			
			// Connect to the web site
  			Elements izenburua = doc.select("a[class*=section]");
    		
    		if(izenburua!=null){
    			titularra=izenburua.text();//lista osoaren titulua ezarri
    		}
    		//}
    		String weba="";
            Elements blogak = doc.select("div[class^=description]");
            Element specialBlog=doc.select("div[class^=special-blog]").first();
            Blog bl=new Blog();
            Elements irudia = specialBlog.select("img");
            Elements egilea = specialBlog.select("div[class*=author]");
            Elements bloga = specialBlog.select("div[class^=blog]");
            Elements post = specialBlog.select("div[class=content]");
            if(bloga.select("a").attr("href").startsWith("/")){
					weba="http://www.naiz.eus";
            }
           bl.setBlogLink(weba+bloga.select("a").attr("href"));
           weba="";
           bl.setTit(bloga.text());
           bl.setEgilea( egilea.text());
           if(irudia.attr("src").startsWith("/")){
				weba="http://www.naiz.eus";
           }
           bl.setEgileImage(MainActivity.getBitmapFromURL(weba+irudia.attr("src")));
           weba="";
           if(post.select("a").attr("href").startsWith("/")){
					weba="http://www.naiz.eus";
           }
           bl.setPostLink(weba+post.select("a").attr("href"));
           weba="";
           bl.setPostTit(post.text());
            blogLista.add(bl);
            
        	for (i=0;i<blogak.size();i++) {
        		publishProgress((Integer)((100/blogak.size())*i));
        		 Blog b=new Blog();
                 Elements irudia2 = blogak.get(i).select("img");
                 Elements egilea2 = blogak.get(i).select("div[class*=author]");
                 Elements bloga2 = blogak.get(i).select("div[class*=blog]");
                 Elements post2 = blogak.get(i).select("div[class=content]");
                 if(bloga2.select("a").attr("href").startsWith("/")){
     					weba="http://www.naiz.eus";
                 }
                b.setBlogLink(weba+bloga2.select("a").attr("href"));
                weba="";
                b.setTit(bloga2.text());
                b.setEgilea( egilea2.text());
                if(irudia2.attr("src").startsWith("/")){
 					weba="http://www.naiz.eus";
            }
                b.setEgileImage(MainActivity.getBitmapFromURL(weba+irudia2.attr("src")));
                weba="";
                if(post2.select("a").attr("href").startsWith("/")){
     					weba="http://www.naiz.eus";
                }
                b.setPostLink(weba+post2.select("a").attr("href"));
                weba="";
                b.setPostTit(post2.text());;
        		blogLista.add(b);
            }
  		} catch (IOException e1) {
  			e1.printStackTrace();
  	//		setTitle("Errorea blogak lortzerakoan (konpontzeke.)");
//   			try {
//				doInBackground(new URL(null));
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				System.out.println("Errorea blogak lortzerakoan (konpontzeke.)");
//				e.printStackTrace();
//			}
///////
//  			AsinkTask thread = new AsinkTask();
//  			dialog.dismiss();
//  			URL url = null;
//  			thread.execute(url);
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
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		String link = blogLista.get(position).getPostLink();
		  FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
  	 fragmentManager.beginTransaction()
		     .replace(R.id.frame_container, BlogBatFragment.newInstance(link))
		     .commit();
	}
	public static Fragment newInstance() {
		BlogakFragment fragment = new BlogakFragment();
       return fragment;  
	}
	public void setTitle(String titular) {
		TitView.setText(titular);
	}
}
