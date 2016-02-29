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
import java.io.IOException;
import java.util.ArrayList;

import com.naiz.eus.adapter.HerriListAdapter;
import com.naiz.eus.db.DatabaseHandler;
import com.naiz.eus.model.Saila;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class FavFragment extends ListFragment implements OnItemClickListener {
	    /**
	     * The container view which has layout change animations turned on. In this sample, this view
	     * is a {@link android.widget.LinearLayout}.
	     */
		private DatabaseHandler db;
		private ArrayList<Saila> HerriLista=new ArrayList<Saila>();
	    public FavFragment() {
		}
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			setHasOptionsMenu(true);
			View rootView = inflater.inflate(R.layout.activity_layout_fav, container, false);
		//	ListView lv = (ListView)rootView.findViewById(android.R.id.list);
				
			db = new DatabaseHandler(getActivity());
			try {
				db.createDataBase();
				db.close();
			} catch (IOException e1) {
				e1.printStackTrace();
				System.out.println("gaizki db");
			}
	        HerriLista = db.getFavHerria();
	        if(HerriLista!=null){
	        	addItem();
	        }
	        return rootView;
	    }

	    private void addItem() {
	    		HerriListAdapter adapter = new HerriListAdapter(getActivity(),HerriLista);
				setListAdapter(adapter);
				 if (FavFragment.this.isVisible()) {
					getListView().setOnItemClickListener(FavFragment.this);
					System.out.println("fav setklik barru ");
				}
	    }
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			String link = HerriLista.get(position).getLink();
			System.out.println("fav klikatua "+link);
			MainActivity.herrian=true;
			MainActivity.unekoHerria=HerriLista.get(position).getTit();
			FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
			fragmentManager.beginTransaction()
			     .replace(R.id.frame_container, EguraldiBatFragment.newInstance(link))
			     .commit();
		}
}
