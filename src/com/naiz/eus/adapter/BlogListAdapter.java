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
import com.naiz.eus.model.Blog;

public class BlogListAdapter extends BaseAdapter {

    Context context;
    List<Blog> rowItem;

    public BlogListAdapter(Context context, List<Blog> rowItem) {
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
            convertView = mInflater.inflate(R.layout.blog_list_item, null);
        }

        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.img_list_blog);
        TextView txtBTitle = (TextView) convertView.findViewById(R.id.txt_list_blog_tit);
        TextView txtEgilea= (TextView) convertView.findViewById(R.id.txt_list_blog_egilea);
        TextView txtPTitle= (TextView) convertView.findViewById(R.id.txt_list_blog_postTit);

        Blog row_pos = rowItem.get(position);
        imgIcon.setImageBitmap(row_pos.getEgileImage());
        txtBTitle.setText(row_pos.getTit());
        txtPTitle.setText(row_pos.getPostTit());
        txtEgilea.setText(row_pos.getEgilea());
        return convertView;

    }

}