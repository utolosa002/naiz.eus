/*
 * Copyright 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.naiz.eus;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

/**
 * Demonstrates a "screen-slide" animation using a {@link ViewPager}. Because {@link ViewPager}
 * automatically plays such an animation when calling {@link ViewPager#setCurrentItem(int)}, there
 * isn't any animation-specific code in this sample.
 *
 * <p>This sample shows a "next" button that advances the user to the next step in a wizard,
 * animating the current screen out (to the left) and the next screen in (from the right). The
 * reverse animation is played when the user presses the "previous" button.</p>
 *
 * @see ScreenSlidePageFragment
 */
@SuppressLint("NewApi")
public class ScreenSlideActivity extends FragmentActivity {
    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static final int NUM_PAGES = MainActivity.albisteKop;

	public static int testutamaina=MainActivity.testutamaina;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;

	public ArrayList<String> Linkak;

	private int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_slide);
        
        Linkak=getIntent().getExtras().getStringArrayList("Linkak");
        pos = getIntent().getExtras().getInt("pos");
        
        System.out.println("SlideActivity - onCreate:pos="+pos);
    	System.out.println("SlideActivity - onCreate:linkak.size="+Linkak.size());
    	
        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        
        mPager.setCurrentItem(pos);
        
        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // When changing pages, reset the action bar actions since they are dependent
                // on which page is currently active. An alternative approach is to have each
                // fragment expose actions itself (rather than the activity exposing actions),
                // but for simplicity, the activity provides the actions in this sample.
                invalidateOptionsMenu();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.activity_screen_slide, menu);

        menu.findItem(R.id.action_previous).setEnabled(mPager.getCurrentItem() > 0);

        // Add either a "next" or "finish" button to the action bar, depending on which page
        // is currently selected.
        MenuItem item = menu.add(Menu.NONE, R.id.action_next, Menu.NONE,
                (mPager.getCurrentItem() == mPagerAdapter.getCount() - 1)
                        ? R.string.action_finish
                        : R.string.action_next);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        
		switch (item.getItemId()) {
        case R.id.action_share:
        	Intent mShareIntent;
    	mShareIntent = new Intent();
		mShareIntent.setAction(Intent.ACTION_SEND);
		mShareIntent.setType("text/plain");
		mShareIntent.putExtra(Intent.EXTRA_TEXT,":naiz - "+Linkak.get(pos));
		startActivity(mShareIntent);
	return true;
		case R.id.action_favorites:
			Intent i = new Intent(this,FavActivity.class);
//			i.putStringArrayListExtra("Linkak", LinkLista);
//			i.putExtra("pos", position);
			startActivity(i);
			return true;
        case R.id.action_login:
			CharSequence login[] = new CharSequence[] {"naiz:", "google", "facebook", "erregistratu"};

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Login");
			builder.setItems(login, new DialogInterface.OnClickListener() {
			    @Override
			    public void onClick(DialogInterface dialog, int which) {
			    	if (which==0){
//			    	String link6 = "http://www.naiz.eus/eu/suscripcion/entrar";
//					WebViewFragment fragment = new WebViewFragment(link6);
//					//HarpidetzaFragment fragment = new HarpidetzaFragment();
//			    	FragmentManager fragmentManager = getSupportFragmentManager();
//					fragmentManager.beginTransaction()
//							.replace(R.id.frame_container, fragment).commit();
					}
			    }
			});
			builder.show();
			
//			searchURL = "http://www.naiz.eus/eu/suscripcion/entrar";
//			ThreadClass thread = new ThreadClass(this);
//			thread.start(); 
//			//wait for thread to finish
//			try {
//				thread.join();
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			return true;
		case R.id.action_textsize_seekbar:
			AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
			LayoutInflater inflater = this.getLayoutInflater();
			View v=inflater.inflate(R.layout.global, null);
			builder2.setView(v).setTitle(R.string.testu_tamaina_tit);
			SeekBar sbetxtsize = (SeekBar)v.findViewById(R.id.textsize_seekbar);
			sbetxtsize.setMax(50);
			sbetxtsize.setProgress(ScreenSlideActivity.testutamaina);
			sbetxtsize.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {
					// TODO Auto-generated method stub
				}
				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {
					// TODO Auto-generated method stub
				}
				@Override
				public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
					ScreenSlideActivity.testutamaina=progress;
					MainActivity.testutamaina=progress;
					if (ScreenSlidePageFragment.Berriatxt!=null){
						WebSettings settings = ScreenSlidePageFragment.Berriatxt.getSettings();
		            	settings.setDefaultFontSize(ScreenSlideActivity.testutamaina);
		            }
		            if(BlogBatFragment.postTxt!=null){
		            	WebSettings settings = BlogBatFragment.postTxt.getSettings();
		            	settings.setDefaultFontSize(ScreenSlideActivity.testutamaina);
		            }
		        }
			});
			builder2.show();
			return true;
        case android.R.id.home:
            // Navigate "up" the demo structure to the launchpad activity.
            // See http://developer.android.com/design/patterns/navigation.html for more.
            NavUtils.navigateUpTo(this, new Intent(this, MainActivity.class));
            return true;
            case R.id.action_previous:
                // Go to the previous step in the wizard. If there is no previous step,
                // setCurrentItem will do nothing.
                mPager.setCurrentItem(mPager.getCurrentItem() - 1);
                return true;

            case R.id.action_next:
                // Advance to the next step in the wizard. If there is no next step, setCurrentItem
                // will do nothing.
                mPager.setCurrentItem(mPager.getCurrentItem() + 1);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
	@Override
	public void onBackPressed() {
		MainActivity.hasieran=true;
		super.onBackPressed();
	}
    /**
     * A simple pager adapter that represents 5 {@link ScreenSlidePageFragment} objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
        	System.out.println("SlideActivity - getItem:linkak.size="+Linkak.size());
        	System.out.println("SlideActivity - getItem:position="+position);
        	if (Linkak.size()<position){
        		position=Linkak.size();
        	}
            return ScreenSlidePageFragment.create(position,Linkak);
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}
