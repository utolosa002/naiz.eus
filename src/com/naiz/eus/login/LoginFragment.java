package com.naiz.eus.login;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.naiz.eus.MainActivity;
import com.naiz.eus.R;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences.Editor;
import android.view.View.OnClickListener;
import android.widget.EditText;
public class LoginFragment extends Fragment {
	private EditText username,password;
	public static final String name = "nameKey"; 
	public static final String pass = "passwordKey"; 
	public static final String res = "responseKey"; 
	private SharedPreferences sharedpreferences;
	private TextView sarrera;
	private Document resp=null;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_harpidetzak, container, false);
	    username = (EditText)rootView.findViewById(R.id.editText1);
	    password = (EditText)rootView.findViewById(R.id.editText2);
	    sarrera = (TextView)rootView.findViewById(R.id.textView3);
	    Button botoia = (Button)rootView.findViewById(R.id.button1);
	      if (android.os.Build.VERSION.SDK_INT > 9) {
	    	  StrictMode.ThreadPolicy policy = 
	    	          new StrictMode.ThreadPolicy.Builder().permitAll().build();
	    	  StrictMode.setThreadPolicy(policy);
	    	  }
			final OnClickListener denaListener = new OnClickListener() {
				public void onClick(View v) {
					login(v);
				}
			};
			botoia.setOnClickListener(denaListener);
	      return rootView;
	   }
	   @Override
	   public void onResume() {
		   sharedpreferences=getActivity().getSharedPreferences(MainActivity.MyPREFERENCES, 
				   Context.MODE_PRIVATE);
		   String responsestring="";
		   if (sharedpreferences.contains(name)){
			   if(sharedpreferences.contains(pass)){
				   FragmentManager fragmentManager = getFragmentManager();
				   HarpidetuaFragment fragment;
				   if(sharedpreferences.contains(res)){
					   responsestring= sharedpreferences.getString(res, responsestring);
					   fragment =new HarpidetuaFragment(responsestring);
				   }else{
					   fragment =new HarpidetuaFragment("");
				   }
				   fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
			   }
		   }
		   super.onResume();
	   }

	   public void login(View view){
	      Editor editor = sharedpreferences.edit();
	      String u = username.getText().toString();
	      String p = password.getText().toString();
	      resp = LoginOndo(u,p);
	      if (resp!=null){
	    	  editor.putString(name, u);
	    	  editor.putString(pass, p);
	    	  editor.putString(res, resp.toString());
	    	  editor.commit();
	    	  FragmentManager fragmentManager = getFragmentManager();
	    	  HarpidetuaFragment fragment =new HarpidetuaFragment(resp.toString());

				Log.d("esp.toString(): ",resp.toString());
	    	//  EguraldiBatFragment fragment =new EguraldiBatFragment(resp,false);
	    	  fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
          }else{
        	  sarrera.setText("Erabiltzaile edo pasahitz okerra");
        	  username.setText("");
        	  password.setText("");
          }
	   }
	private Document LoginOndo(String u, String p) {
        if(u.length()>0) {
        	try {
        		Document s = Jsoup.connect("https://www.naiz.eus/eu/suscripcion/entrar")
        				.data("login",u)
        				.data("password",p)
        				.timeout(10*1000)
        				.post();
        		String se = s.toString();
        		if(se.contains("profile-block public-data")){
        			return s;
        		}else{
        			  Toast.makeText(getActivity(),"Erabiltzaile edo pasahitz okerrak \n Saiatu berriro",Toast.LENGTH_SHORT).show();
        			return 	null;
        		}
           } catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
           }
        } else {
            //display message if text field is empty
        	return null;
        }
		return null;
	}
}