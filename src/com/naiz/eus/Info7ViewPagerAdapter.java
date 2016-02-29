package com.naiz.eus;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
 
public class Info7ViewPagerAdapter extends FragmentPagerAdapter {
 
	final int PAGE_COUNT = 5;
	// Tab Titles
	private String tabtitles[] = new String[] {"Info7", "Nazioartea", "Kultura", "Jendartea", "Gure Herria"};
	Context context;
 
	public Info7ViewPagerAdapter(FragmentManager fm) {
		super(fm);
	}
 
	@Override
	public int getCount() {
		return PAGE_COUNT;
	}
 
	@Override
	public Fragment getItem(int position) {
		String saila;
		String searchURL;
		switch (position) {
		case 0:
			saila="i7";
        	searchURL="http://info7.naiz.eus/";
			Fragment fragmenttab0 = new AlbisteFragment(saila,searchURL,null);
			return fragmenttab0;
		case 1:
			saila="i7nazioartea";
        	searchURL="http://info7.naiz.eus/eu/list_i7/nazioartea";
			Fragment fragmenttab1 = new AlbisteFragment(saila,searchURL,null);
			return fragmenttab1;
		case 2:
			saila="i7kultura";
        	searchURL="http://info7.naiz.eus/eu/list_i7/kultura";
			Fragment fragmenttab2 = new AlbisteFragment(saila,searchURL,null);
			return fragmenttab2;
		case 3:
			saila="i7jendartea";
        	searchURL="http://info7.naiz.eus/eu/list_i7/jendartea";
			AlbisteFragment fragmenttab3 = new AlbisteFragment(saila,searchURL,null);
			return fragmenttab3;		
		case 4:
			saila="i7gureherria";
	        searchURL="http://info7.naiz.eus/eu/list_i7/gure-herria";
			Fragment fragmenttab4 = new AlbisteFragment(saila,searchURL,null);
			return fragmenttab4;
 
		}
		return null;
	}
 
	@Override
	public CharSequence getPageTitle(int position) {
		return tabtitles[position];
	}
}