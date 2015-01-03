package com.naiz.eus.widget;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViewsService;

@SuppressLint("NewApi")
public class WidgetService extends RemoteViewsService {
	@Override
	public RemoteViewsFactory onGetViewFactory(Intent intent) {
		Log.i("WidgetService", "LOG");
		return(new LoremViewsFactory(this.getApplicationContext(), intent));
	}
}