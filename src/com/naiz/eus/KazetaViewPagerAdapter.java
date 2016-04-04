package com.naiz.eus;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
 
public class KazetaViewPagerAdapter extends FragmentPagerAdapter {
 
	final int PAGE_COUNT = 6;
	// Tab Titles
	private String tabtitles[] = new String[] {"Kazeta", "Euskal Herria", "Ekonomia", "Kultura", "Teknologia", "Kirola"};
	Context context;
 
	public KazetaViewPagerAdapter(FragmentManager fm) {
		super(fm);
	}
 
	@Override
	public int getCount() {
		return PAGE_COUNT;
	}
 
	@Override
	public Fragment getItem(int position) {
		System.out.println(" KazetaFragment get item");
		String saila;
		String searchURL;
		switch (position) {
		case 0:
			saila="kazeta";
        	searchURL="http://kazeta.naiz.eus/eu";
        	AlbisteFragment fragmenttab0 = new AlbisteFragment(saila,searchURL,null);
			return fragmenttab0;
		case 1:
			saila="kazetaeuskalherria";
        	searchURL="http://kazeta.naiz.eus/eu/list_kz/euskal-herria";
        	AlbisteFragment fragmenttab1 = new AlbisteFragment(saila,searchURL,null);
			return fragmenttab1;
		case 2:
			saila="kazetaekonomia";
        	searchURL="http://kazeta.naiz.eus/eu/list_kz/ekonomia";
        	AlbisteFragment fragmenttab2 = new AlbisteFragment(saila,searchURL,null);
			return fragmenttab2;
		case 3:
			saila="kazetakultura";
        	searchURL="http://kazeta.naiz.eus/eu/list_kz/kultura";
			AlbisteFragment fragmenttab3 = new AlbisteFragment(saila,searchURL,null);
			return fragmenttab3;		
		case 4:
			saila="kazetateknologia";
	        searchURL="http://kazeta.naiz.eus/eu/list_kz/teknologia";
	        AlbisteFragment fragmenttab4 = new AlbisteFragment(saila,searchURL,null);
			return fragmenttab4;
		case 5:
			saila="kazetakirola";
	        searchURL="http://kazeta.naiz.eus/eu/list_kz/kirola";
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