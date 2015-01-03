package com.naiz.eus.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.naiz.eus.R;
import com.naiz.eus.model.Berria;

public class BerriaListAdapter extends BaseAdapter {

    Context context;
    List<Berria> rowItem;

    public BerriaListAdapter(Context context, List<Berria> rowItem) {
        this.context = context;
        this.rowItem = rowItem;

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
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.berria_list_item, null);
        }

        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.rowimage);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.rowtit);
        TextView txtSaila = (TextView) convertView.findViewById(R.id.rowsection);
        TextView txtAzpitit = (TextView) convertView.findViewById(R.id.rowsubtitle);
        TextView txtInfo= (TextView) convertView.findViewById(R.id.rowdata);

        Berria row_pos = rowItem.get(position);
        imgIcon.setImageBitmap(row_pos.getImage());
        txtTitle.setText(row_pos.getTitle());
        txtSaila.setText(row_pos.getSaila());
        txtAzpitit.setText(row_pos.getSubtitle());
        txtInfo.setText(row_pos.getExtraInfo());

        return convertView;

    }

}