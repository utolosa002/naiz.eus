package com.naiz.eus.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class PhoneBootCompleteReceiver extends BroadcastReceiver {
    public static boolean wasPhoneBootSucessful = true;//false;
    
    /**
     * Just changing the boolean value on Phone Boot Complete event one can send
     * custom broadcast from here as well send these broadcast of appwidget
     * provider to make necessary updates
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            wasPhoneBootSucessful = true;
        }
    }
}