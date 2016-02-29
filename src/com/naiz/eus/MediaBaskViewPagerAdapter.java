package com.naiz.eus;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
 
public class MediaBaskViewPagerAdapter extends FragmentPagerAdapter {
 
	final int PAGE_COUNT = 6;
	// Tab Titles
	private String tabtitles[] = new String[] {"MediaBask", "Euskal Herria", "Ekonomia", "Kultura", "Teknologia", "Kirola"};
	Context context;
 
	public MediaBaskViewPagerAdapter(FragmentManager fm) {
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
			saila="mediabask";
        	searchURL="http://mediabask.naiz.eus/eu";
        	AlbisteFragment fragmenttab0 = new AlbisteFragment(saila,searchURL,null);
			return fragmenttab0;
		case 1:
			saila="mediabaskeuskalherria";
        	searchURL="http://mediabask.naiz.eus/eu/list_mbsk/euskal-herria";
        	AlbisteFragment fragmenttab1 = new AlbisteFragment(saila,searchURL,null);
			return fragmenttab1;
		case 2:
			saila="mediabaskekonomia";
        	searchURL="http://mediabask.naiz.eus/eu/list_mbsk/ekonomia";
        	AlbisteFragment fragmenttab2 = new AlbisteFragment(saila,searchURL,null);
			return fragmenttab2;
		case 3:
			saila="mediabaskkultura";
        	searchURL="http://mediabask.naiz.eus/eu/list_mbsk/kultura";
			AlbisteFragment fragmenttab3 = new AlbisteFragment(saila,searchURL,null);
			return fragmenttab3;		
		case 4:
			saila="mediabaskteknologia";
	        searchURL="http://mediabask.naiz.eus/eu/list_mbsk/teknologia";
	        AlbisteFragment fragmenttab4 = new AlbisteFragment(saila,searchURL,null);
			return fragmenttab4;
		case 5:
			saila="mediabaskkirola";
	        searchURL="http://mediabask.naiz.eus/eu/list_mbsk/kirola";
	        AlbisteFragment fragmenttab5 = new AlbisteFragment(saila,searchURL,null);
			return fragmenttab5;
		}
		return null;
	}
 
	@Override
	public CharSequence getPageTitle(int position) {
		return tabtitles[position];
	}
}