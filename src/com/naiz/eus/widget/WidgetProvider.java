package com.naiz.eus.widget;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.naiz.eus.MainActivity;
import com.naiz.eus.R;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
@SuppressLint("NewApi")
public class WidgetProvider extends AppWidgetProvider {
	public static String EXTRA_WORD= "com.naiz.eus.widget.lorem.WORD";
	public static String UPDATE_LIST = "UPDATE_LIST";
	@SuppressLint("NewApi")
	@Override
	public void onUpdate(Context ctxt, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

	    if (PhoneBootCompleteReceiver.wasPhoneBootSucessful) {
	     //   PhoneBootCompleteReceiver.wasPhoneBootSucessful = false;
	 
		for (int i=0; i<appWidgetIds.length; i++) {
			Log.i("WidgetProvider"+i, "LOG");
			
			Intent svcIntent=new Intent(ctxt, WidgetService.class);
			svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
			svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));
			ctxt.startService(svcIntent);
			RemoteViews widget=new RemoteViews(ctxt.getPackageName(), R.layout.widget_layout);
			widget.setRemoteAdapter(R.id.widget_albiste_lv, svcIntent);

			DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
			Date now = new Date();
			widget.setTextViewText(R.id.rfsh_tm, "Eguneratua: "+dateFormat.format(now));
			
			//TODO Berri bakoitzari
			Intent clickIntent=new Intent(ctxt, MainActivity.class);
			PendingIntent clickPI=PendingIntent
                              .getActivity(ctxt, 0,clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			widget.setPendingIntentTemplate(R.id.widget_albiste_lv, clickPI);
			
			//refresh
			clickIntent = new Intent(ctxt, WidgetProvider.class);
			clickIntent.setAction(UPDATE_LIST);
            PendingIntent pendingIntentRefresh = PendingIntent.getBroadcast(ctxt,0, clickIntent, 0);
            widget.setOnClickPendingIntent(R.id.rfsh_img, pendingIntentRefresh);
			
			appWidgetManager.updateAppWidget(appWidgetIds[i], widget);
			updateWidget(ctxt);
		}
		}
		super.onUpdate(ctxt, appWidgetManager, appWidgetIds);
	}
	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
		if(intent.getAction().equalsIgnoreCase(UPDATE_LIST)){
			updateWidget(context);
		}
		Log.e("onReceive", "LOG");
	}
	@Override
	public void onAppWidgetOptionsChanged(final Context context, final AppWidgetManager appWidgetManager, final int appWidgetId, final Bundle newOptions) { 
		 super.onAppWidgetOptionsChanged(context,appWidgetManager,appWidgetId,newOptions);
		 updateWidget(context);
	}
	private void updateWidget(Context ctxt) {
		Log.e("updateWidget", "LOG");
		
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(ctxt);
	    int appWidgetIds[] = appWidgetManager.getAppWidgetIds(new ComponentName(ctxt, WidgetProvider.class));
	    appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_albiste_lv);
	    
		RemoteViews widget=new RemoteViews(ctxt.getPackageName(), R.layout.widget_layout);
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Date now = new Date();
		widget.setTextViewText(R.id.rfsh_tm, "Eguneratua: "+dateFormat.format(now));

		appWidgetManager.updateAppWidget(appWidgetIds, widget);
	}
}