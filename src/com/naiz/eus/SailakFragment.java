package com.naiz.eus;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.naiz.eus.R;
import com.naiz.eus.adapter.SailListAdapter;
import com.naiz.eus.model.Saila;

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
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class SailakFragment extends ListFragment implements OnItemClickListener {

	private static String searchURL;
    private List<Saila> sailLista;
    Bundle savedInstanceState1;
    
	public SailakFragment(){}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_sailak, container, false);
		final TextView titView = (TextView) rootView.findViewById(R.id.txt_fg_sailak);
		savedInstanceState1= savedInstanceState;
		searchURL="http://www.naiz.eus/eu/";
		sailLista= new ArrayList<Saila>();
		titView.setText("Sailak");

		AsinkTask thread = new AsinkTask();
		URL url = null;
		thread.execute(url);
		
        return rootView;
    }
	
	
	
	
	
	private class AsinkTask extends AsyncTask<URL, Integer, Long> {
		 private ProgressDialog dialog = new ProgressDialog(SailakFragment.this.getActivity());
		    @Override
		    protected void onPostExecute(Long result) {            
		        super.onPostExecute(result);
		        dialog.dismiss();
		        SailListAdapter adapter = new SailListAdapter(getActivity(), sailLista);
		        setListAdapter(adapter);
		        if(SailakFragment.this.isVisible()){
		        	getListView().setOnItemClickListener(SailakFragment.this);
		        }
		    }
		
		    @Override
		    protected void onPreExecute() {
		        super.onPreExecute();
		        dialog.setMessage("Sailak lortzen...");
		        dialog.show();
		    }
	     protected Long doInBackground(URL... urls) {
   		Document doc = null;
   		try {	
   			int k=0;
		while (k<5 && doc==null){
		doc = Jsoup.connect(searchURL).get();
		k++;
		}
   		// Connect to the web site
    		Elements kategoria = doc.select("div[id*=sections-menu]");
    		Elements kategoriak = kategoria.select("li");
                  
        	for (int i=0;i<kategoriak.size();i++) {
        		String Kategoria_izenb = kategoriak.get(i).text();
        		Elements Kategoria_link = kategoriak.get(i).select("a");
                Saila s=new Saila();
        		String text_k_link = Kategoria_link.attr("href");
        		s.setTit(Kategoria_izenb);
        		if(text_k_link.length()>1){
    				if(text_k_link.startsWith("/")){
    					text_k_link="http://www.naiz.eus"+text_k_link;
    				}
    			}
        		s.setLink(text_k_link);
        		sailLista.add(s);
            }
        
   		} catch (IOException e1) {
   			e1.printStackTrace();
   			System.out.println("errorea sailak lortzean");
   			try {
				doInBackground(new URL(null));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("errorea sailak lortzean2");
				e.printStackTrace();
			}
//   			AsinkTask thread = new AsinkTask();
//   			dialog.dismiss();
//   			URL url = null;
//   			thread.execute(url);
   		}
			return null;
	     }
	     @Override
	     protected void onProgressUpdate(Integer... progress) {
	    	 dialog.setMessage("Sailak lortzen..."+progress[0]);
	     }
	}

	@Override
   public void onActivityCreated(Bundle savedInstanceState) {
       super.onActivityCreated(savedInstanceState);
   }

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		String saila = sailLista.get(position).getTit();
		String link = sailLista.get(position).getLink();
		  FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
    	 fragmentManager.beginTransaction()
		     .replace(R.id.frame_container, AlbisteFragment.newInstance(saila,link))
		     .commit();
	}
	public static Fragment newInstance() {
		SailakFragment fragment = new SailakFragment();
        return fragment;  
	}
}
