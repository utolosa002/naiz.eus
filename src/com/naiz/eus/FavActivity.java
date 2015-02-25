package com.naiz.eus;
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
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FavActivity extends Activity {
	    /**
	     * The container view which has layout change animations turned on. In this sample, this view
	     * is a {@link android.widget.LinearLayout}.
	     */
	    private ViewGroup mContainerView;

	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_layout_fav);

	        mContainerView = (ViewGroup) findViewById(R.id.container);
	    }

	    @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        super.onCreateOptionsMenu(menu);
	        getMenuInflater().inflate(R.menu.activity_layout_fav, menu);
	        return true;
	    }

	    @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	        switch (item.getItemId()) {
	            case android.R.id.home:
	                // Navigate "up" the demo structure to the launchpad activity.
	                // See http://developer.android.com/design/patterns/navigation.html for more.
	                NavUtils.navigateUpTo(this, new Intent(this, MainActivity.class));
	                return true;

	            case R.id.action_add_item:
	                // Hide the "empty" view since there is now at least one item in the list.
	                findViewById(android.R.id.empty).setVisibility(View.GONE);
	                addItem();
	                return true;
	        }

	        return super.onOptionsItemSelected(item);
	    }

	    private void addItem() {
	        // Instantiate a new "row" view.
	        final ViewGroup newView = (ViewGroup) LayoutInflater.from(this).inflate(
	                R.layout.list_item_fav, mContainerView, false);

	        // Set the text in the new row to a random country.
	        ((TextView) newView.findViewById(android.R.id.text1)).setText(
	                COUNTRIES[(int) (Math.random() * COUNTRIES.length)]);

	        // Set a click listener for the "X" button in the row that will remove the row.
	        newView.findViewById(R.id.delete_button).setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View view) {
	                // Remove the row from its parent (the container view).
	                // Because mContainerView has android:animateLayoutChanges set to true,
	                // this removal is automatically animated.
	                mContainerView.removeView(newView);

	                // If there are no rows remaining, show the empty view.
	                if (mContainerView.getChildCount() == 0) {
	                    findViewById(android.R.id.empty).setVisibility(View.VISIBLE);
	                }
	            }
	        });

	        // Because mContainerView has android:animateLayoutChanges set to true,
	        // adding this view is automatically animated.
	        mContainerView.addView(newView, 0);
	    }

	    /**
	     * A static list of country names.
	     */
	    private static final String[] COUNTRIES = new String[]{
	            "Belgium", "France", "Italy", "Germany", "Spain",
	            "Austria", "Russia", "Poland", "Croatia", "Greece",
	            "Ukraine",
	    };
}
