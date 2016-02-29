package com.naiz.eus;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
 
public class Gaur8ViewPagerAdapter extends FragmentPagerAdapter {
 
	final int PAGE_COUNT = 5;
	// Tab Titles
	private String tabtitles[] = new String[] {"Gaur8", "Atzerria", "Begiradak", "e-rritarrak", "Herria"};
	Context context;
 
	public Gaur8ViewPagerAdapter(FragmentManager fm) {
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
			saila="g8";
        	searchURL="http://www.naiz.eus/hemeroteca/gaur8";
        	AlbisteFragment fragmenttab0 = new AlbisteFragment(saila,searchURL,null);
			return fragmenttab0;
		case 1:
			saila="g8atzerria";
        	searchURL="http://www.naiz.eus/eu/hemeroteca/gaur8/sections/atzerria";
        	AlbisteFragment fragmenttab1 = new AlbisteFragment(saila,searchURL,null);
			return fragmenttab1;
		case 2:
			saila="g8begiradak";
        	searchURL="http://www.naiz.eus/eu/hemeroteca/gaur8/sections/begiradak";
        	AlbisteFragment fragmenttab2 = new AlbisteFragment(saila,searchURL,null);
			return fragmenttab2;
		case 3:
			saila="g8erritarrak";
        	searchURL="http://www.naiz.eus/eu/hemeroteca/gaur8/sections/erritarrak";
			AlbisteFragment fragmenttab3 = new AlbisteFragment(saila,searchURL,null);
			return fragmenttab3;		
		case 4:
			saila="g8herria";
	        searchURL="http://www.naiz.eus/eu/hemeroteca/gaur8/sections/herria";
	        AlbisteFragment fragmenttab4 = new AlbisteFragment(saila,searchURL,null);
			return fragmenttab4;
 
		}
		return null;
	}
 
	@Override
	public CharSequence getPageTitle(int position) {
		return tabtitles[position];
	}
}