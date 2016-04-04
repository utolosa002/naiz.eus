package com.naiz.eus;

import com.naiz.eus.R;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class GaraFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// Get the view from activity_main.xml
		View rootView = inflater.inflate(R.layout.slide_sail_acti, container,false);

		// Locate the viewpager in activity_main.xml
		ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.pager_saila);

		// Set the garaViewPagerAdapter into ViewPager
		viewPager.setAdapter(new GaraViewPagerAdapter(getChildFragmentManager()));
		return rootView;
	}
}