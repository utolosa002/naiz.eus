package com.naiz.eus.login;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.naiz.eus.AlbisteFragment;
import com.naiz.eus.MainActivity;
import com.naiz.eus.R;
import com.naiz.eus.model.NavDrawerItem;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class HarpidetuaFragment extends Fragment {
	public Document doc;
	public SharedPreferences sharedpreferences;
	public HarpidetuaFragment() {}
	public HarpidetuaFragment(String html){
		doc=Jsoup.parseBodyFragment(html);
		Log.d("parse",html);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_erab, container, false);
	    ////LOGIN MENUA EZARRI////
		SharedPreferences sharedpreferences = getActivity().getSharedPreferences(MainActivity.MyPREFERENCES, Context.MODE_PRIVATE);
		String defValue = "";
		String balue="";
		balue = sharedpreferences.getString("nameKey", defValue);
		if (balue!=""){
			if(!(MainActivity.navDrawerItems==null||MainActivity.navDrawerItems.size()==0)){
				MainActivity.navDrawerItems.set(MainActivity.navDrawerItems.size()-1, new NavDrawerItem(balue, R.drawable.ic_action_person));
			}
		}else{
			MainActivity.navDrawerItems.set(MainActivity.navDrawerItems.size()-1, new NavDrawerItem("Sartu", R.drawable.ic_action_person));
		}	
		MainActivity.adapter.notifyDataSetChanged();
		MainActivity.mDrawerList.setAdapter(MainActivity.adapter);
		////////
		TextView t=(TextView)rootView.findViewById(R.id.ongietorria);
	    Button botoilogout = (Button)rootView.findViewById(R.id.buttonlogout);
	    Button botoiexit = (Button)rootView.findViewById(R.id.buttonExit);
	    ImageView profilIcon = (ImageView)rootView.findViewById(R.id.img_profil);
	    
	    Bitmap img;
	    String albiste_irudia="";
	    Elements irudia = null;
	    Elements profile_irudia = null;
	    if (doc!=null){
	    	profile_irudia = doc.select("div[class*=profile-form]");
			Log.d("profile_irudia",profile_irudia.toString());
		if (profile_irudia.first() != null) {
			irudia =profile_irudia.first().select("img");
			Log.d("irudia",profile_irudia.toString());
			albiste_irudia = irudia.first().attr("src");
			if (albiste_irudia.startsWith("/")) {
				albiste_irudia = "http://www.naiz.eus"
						+ albiste_irudia;
			}
			img = MainActivity.getBitmapFromURL(albiste_irudia);
		}
		img = MainActivity.getBitmapFromURL(albiste_irudia);
	    profilIcon.setImageBitmap(img);
	    }
	    t.setText("Kaixo "+balue+"!\n Saioa hasia duzu ");
		final OnClickListener logoutListener = new OnClickListener() {
			public void onClick(View v) {
				logout(v);
			}
		};
		final OnClickListener exitListener = new OnClickListener() {
			public void onClick(View v) {
				exit(v);
			}
		};
		botoilogout.setOnClickListener(logoutListener);
		botoiexit.setOnClickListener(exitListener);
		return rootView;
	}
	
	public void logout(View view){
		SharedPreferences sharedpreferences = getActivity().getSharedPreferences(MainActivity.MyPREFERENCES, Context.MODE_PRIVATE);
		Editor editor = sharedpreferences.edit();
		editor.clear();
		editor.commit();
		FragmentManager fragmentManager = getFragmentManager();
		AlbisteFragment fragment =new AlbisteFragment();
		fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
   }
   public void exit(View view){
	   FragmentManager fragmentManager = getFragmentManager();
	   AlbisteFragment fragment =new AlbisteFragment();
	   fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
   }
}