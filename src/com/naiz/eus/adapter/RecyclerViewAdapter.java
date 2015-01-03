package com.naiz.eus.adapter;


import com.naiz.eus.R;
import com.naiz.eus.model.Berria;
//
//import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class RecyclerViewAdapter{// extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private Berria[] itemsData;
    
    public RecyclerViewAdapter(Berria[] itemsData) {
        this.itemsData = itemsData;
	}

//	// Create new views (invoked by the layout manager)
//    @Override
//    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
//                                                   int viewType) {
//
//        View v = LayoutInflater.from(parent.getContext())
//        		.inflate(R.layout.berria_list_item, parent, false);
//        		// set the view's size, margins, paddings and layout parameters
//        		ViewHolder vh = new ViewHolder(v);
//        return vh;
//    }
//
//    // Replace the contents of a view (invoked by the layout manager)
//    @Override
//    public void onBindViewHolder(ViewHolder viewHolder, int position) {
//        
//    	// - get data from your itemsData at this position
//        // - replace the contents of the view with that itemsData
//        Berria row_pos = itemsData[position];
//        viewHolder.imgIcon.setImageBitmap(row_pos.getImage());
//        viewHolder.txtTitle.setText(row_pos.getTit());
//        viewHolder.txtSaila.setText(row_pos.getSaila());
//        viewHolder.txtDesk.setText(row_pos.getDesc());
//        viewHolder.txtInfo.setText(row_pos.getExtraInfo());
//    }
//    
//    // inner class to hold a reference to each item of RecyclerView 
//    public static class ViewHolder extends RecyclerView.ViewHolder {
//       
//    	public ImageView imgIcon;
//    	public TextView txtTitle;
//    	public TextView txtSaila;
//    	public TextView txtDesk;
//    	public TextView txtInfo;
//        
//        public ViewHolder(View itemLayoutView) {
//            super(itemLayoutView);
//            imgIcon = (ImageView) itemLayoutView.findViewById(R.id.rowimage);
//            txtTitle = (TextView) itemLayoutView.findViewById(R.id.rowtit);
//            txtSaila = (TextView) itemLayoutView.findViewById(R.id.rowprice);
//            txtDesk = (TextView) itemLayoutView.findViewById(R.id.rowdesk);
//            txtInfo= (TextView) itemLayoutView.findViewById(R.id.rowdata);
//        }
//    }
//
//    // Return the size of your itemsData (invoked by the layout manager)
//    @Override
//    public int getItemCount() {
//        return itemsData.length;
//    }
}