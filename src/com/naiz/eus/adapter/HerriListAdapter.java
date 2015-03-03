package com.naiz.eus.adapter;

import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
//import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.naiz.eus.EguraldiBatFragment;
import com.naiz.eus.FavFragment;
import com.naiz.eus.MainActivity;
import com.naiz.eus.R;
import com.naiz.eus.db.DatabaseHandler;
import com.naiz.eus.model.Saila;

public class HerriListAdapter extends BaseAdapter {
    Context context;
    List<Saila> rowItem;
    private DatabaseHandler db;

    public HerriListAdapter(Context context, List<Saila> rowItem) {
        this.context = context;
        this.rowItem = rowItem;
        db = new DatabaseHandler(context);
		try {
			db.createDataBase();
			db.close();
		} catch (IOException e1) {
			e1.printStackTrace();
			System.out.println("gaizki db");
		}
    }

    @Override
    public int getCount() {
        return rowItem.size();
    }

    @Override
    public Object getItem(int position) {
        return rowItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return rowItem.indexOf(getItem(position));
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.list_item_fav, null);
        }
        TextView txtTitle = (TextView) convertView.findViewById(R.id.favtext1);
        final Saila row_pos = rowItem.get(position);
        txtTitle.setText(row_pos.getTit());
        
        // Set a click listener for the "X" button in the row that will remove the row.
        convertView.findViewById(R.id.favline1).setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View view) {
	            	String link = rowItem.get(position).getLink();
	    			System.out.println("fav klikatua "+link);
	    			link="http://www.naiz.eus/eu/eguraldia/euskal-herria/"+link+"/"+rowItem.get(position).getTit();
	    			FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
	    			fragmentManager.beginTransaction()
	    			     .replace(R.id.frame_container, EguraldiBatFragment.newInstance(link))
	    			     .commit();
	            }
	        });
        
        // Set a click listener for the "X" button in the row that will remove the row.
        convertView.findViewById(R.id.delete_button).setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View view) {
	            	Toast.makeText(parent.getContext(), "clicka", Toast.LENGTH_LONG).show();
	            	db.changeFavHerria(row_pos.getTit());
	            	Toast.makeText(parent.getContext(), "change"+row_pos.getTit(), Toast.LENGTH_LONG).show();
	            	FavFragment fragment = new FavFragment();
	            	FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
	    			fragmentManager.beginTransaction()
	    			     .replace(R.id.frame_container, fragment)
	    			     .commit();
	            }
	        });
        return convertView;
    }
}