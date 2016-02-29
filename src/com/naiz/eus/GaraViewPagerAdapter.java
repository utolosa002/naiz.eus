package com.naiz.eus;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
 
public class GaraViewPagerAdapter extends FragmentPagerAdapter {
 
	final int PAGE_COUNT = 9;
	// Tab Titles
	private String tabtitles[] = new String[] {"Gara", "Eguneko gaiak", "Ekonomia", "Euskal Herria", "Iritzia", "Kirolak", "Kultura", "Mundua", "Zientzia"};
	Context context;
 
	public GaraViewPagerAdapter(FragmentManager fm) {
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
			saila="gara";
        	searchURL="http://www.naiz.eus/hemeroteca/gara";
        	AlbisteFragment fragmenttab0 = new AlbisteFragment(saila,searchURL,null);
			return fragmenttab0;
		case 1:
			saila="garaegunekoa";
        	searchURL="http://www.naiz.eus/eu/hemeroteca/gara/sections/eguneko-gaiak";
        	AlbisteFragment fragmenttab1 = new AlbisteFragment(saila,searchURL,null);
			return fragmenttab1;
		case 2:
			saila="garaekonomia";
        	searchURL="http://www.naiz.eus/eu/hemeroteca/gara/sections/ekonomia";
        	AlbisteFragment fragmenttab2 = new AlbisteFragment(saila,searchURL,null);
			return fragmenttab2;
		case 3:
			saila="garaeh";
        	searchURL="http://www.naiz.eus/eu/hemeroteca/gara/sections/euskal-herria";
			AlbisteFragment fragmenttab3 = new AlbisteFragment(saila,searchURL,null);
			return fragmenttab3;		
		case 4:
			saila="garairitzia";
	        searchURL="http://www.naiz.eus/eu/hemeroteca/gara/sections/iritzia";
	        AlbisteFragment fragmenttab4 = new AlbisteFragment(saila,searchURL,null);
			return fragmenttab4;
		case 5:
			saila="garakirolak";
	        searchURL="http://www.naiz.eus/eu/hemeroteca/gara/sections/kirolak";
	        AlbisteFragment fragmenttab5 = new AlbisteFragment(saila,searchURL,null);
			return fragmenttab5;	
		case 6:
			saila="garakultura";
	        searchURL="http://www.naiz.eus/eu/hemeroteca/gara/sections/kultura";
	        AlbisteFragment fragmenttab6 = new AlbisteFragment(saila,searchURL,null);
			return fragmenttab6;	
		case 7:
			saila="garamundua";
	        searchURL="http://www.naiz.eus/eu/hemeroteca/gara/sections/mundua";
	        AlbisteFragment fragmenttab7 = new AlbisteFragment(saila,searchURL,null);
			return fragmenttab7;	
		case 8:
			saila="garazientzia";
	        searchURL="http://www.naiz.eus/eu/hemeroteca/gara/sections/zientzia";
	        AlbisteFragment fragmenttab8 = new AlbisteFragment(saila,searchURL,null);
			return fragmenttab8;
 
		}
		return null;
	}
 
	@Override
	public CharSequence getPageTitle(int position) {
		return tabtitles[position];
	}
}