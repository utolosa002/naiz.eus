package com.naiz.eus.login;

import com.naiz.eus.R;

import android.support.v4.app.Fragment;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class FacebookFragment extends Fragment {

    String USERNAME_KEY ="UserName";
    String prefName = "userNamePref";
    
 	public FacebookFragment(){} 	
 	
 	@Override
     public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_harpidetzak, container, false);
        // get Accounts
        Account[] emailak = getEmail();
        for(Account account: emailak)
        {
	         String emailId=account.toString();
	         Toast.makeText(this.getActivity().getApplicationContext(), "emailId: "+emailId,Toast.LENGTH_LONG).show();
        }
        //SharedPreferences
	    Context ctx = this.getActivity().getApplicationContext();
		SharedPreferences userPrefs = ctx.getSharedPreferences(prefName, ctx.MODE_PRIVATE);
        String userName = userPrefs.getString(USERNAME_KEY, emailak.toString());
        Toast.makeText(this.getActivity().getApplicationContext(), "userName: "+userName,Toast.LENGTH_LONG).show();
        return rootView;
    }

	public static Fragment newInstance() {
		FacebookFragment fragment = new FacebookFragment();
        return fragment;  
	}
	
	public void savePreferences(){}
	
	public Account[] getEmail(){
		////getAccounts
		Account[] faccounts=AccountManager.get(this.getActivity().getApplicationContext()).getAccountsByType("com.facebook.auth.login");
		for(Account account: faccounts)
		{
			String emailId=account.toString();
			Log.d("List of  email id's of user", emailId);
		}
	    String myEmailid=faccounts[0].name;
	    ////SharedPreferences
	    Context ctx = this.getActivity().getApplicationContext();
	    SharedPreferences  prefs =  ctx.getSharedPreferences(prefName,ctx.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = prefs.edit();
        prefEditor.putString(USERNAME_KEY, myEmailid);
        prefEditor.commit();
	    ////
 	    // return myEmailid;
 	    return faccounts;
	}
}