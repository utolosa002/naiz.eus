package com.naiz.eus;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
 
public class NaizViewPagerAdapter extends FragmentPagerAdapter {
 
	final int PAGE_COUNT = 8;
	// Tab Titles
	private String tabtitles[] = new String[] { "naiz:", "Euskal Herria", "Kirolak", "Ekonomia", "Zientzia", "Kultura", "Mundua", "Teknologia" };
	Context context;
 
	public NaizViewPagerAdapter(FragmentManager fm) {
		super(fm);
	}
 
	@Override
	public int getCount() {
		return PAGE_COUNT;
	}
 

	public Fragment getItem(int position) {
		String saila;
		String searchURL;
		switch (position) {
		case 0:
			saila="naizazala";
        	searchURL="http://www.naiz.eus/";
        	AlbisteFragment fragmenttab0 = new AlbisteFragment(saila,searchURL,null);
			return fragmenttab0;
		case 1:
			saila="naizEuskalHerria";
        	searchURL="http://www.naiz.eus/eu/actualidad/euskal-herria";
        	AlbisteFragment fragmenttab1 = new AlbisteFragment(saila,searchURL,null);
			return fragmenttab1;
		case 2:
			saila="naizKirolak";
        	searchURL="http://www.naiz.eus/eu/actualidad/kirolak";
        	AlbisteFragment fragmenttab2 = new AlbisteFragment(saila,searchURL,null);
			return fragmenttab2;
		case 3:
			saila="naizEkonomia";
        	searchURL="http://www.naiz.eus/eu/actualidad/ekonomia";
        	AlbisteFragment fragmenttab3 = new AlbisteFragment(saila,searchURL,null);
			return fragmenttab3;
		case 4:
			saila="naizZientzia";
        	searchURL="http://www.naiz.eus/eu/actualidad/zientzia";
			AlbisteFragment fragmenttab4 = new AlbisteFragment(saila,searchURL,null);
			return fragmenttab4;
		case 5:
			saila="naizKultura";
	        searchURL="http://www.naiz.eus/eu/actualidad/kultura";
	        AlbisteFragment fragmenttab5 = new AlbisteFragment(saila,searchURL,null);
			return fragmenttab5;
		case 6:
			saila="naizMundua";
	        searchURL="http://www.naiz.eus/eu/actualidad/mundua";
			AlbisteFragment fragmenttab6 = new AlbisteFragment(saila,searchURL,null);
			return fragmenttab6;
		case 7:
			saila="naizTeknologia";
        	searchURL="http://www.naiz.eus/eu/actualidad/teknologia";
        	AlbisteFragment fragmenttab7 = new AlbisteFragment(saila,searchURL,null);
			return fragmenttab7;
 
		}
		return null;
	}
 
	@Override
	public CharSequence getPageTitle(int position) {
		return tabtitles[position];
	}
}