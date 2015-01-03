package com.naiz.eus;

import com.naiz.eus.R;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HarpidetzaFragment extends Fragment {

 	public HarpidetzaFragment(){} 	
 	
 	@Override
     public View onCreateView(LayoutInflater inflater, ViewGroup container,
             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_harpidetzak, container, false);
   
        return rootView;
    }

	public static Fragment newInstance() {
		HarpidetzaFragment fragment = new HarpidetzaFragment();
        return fragment;  
	}
}
