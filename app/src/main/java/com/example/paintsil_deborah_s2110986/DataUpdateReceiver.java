package com.example.paintsil_deborah_s2110986;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.Date;

public class DataUpdateReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Log the time the update was triggered
        Log.d("DataUpdateReceiver", "Data update triggered at: " + new Date().toString());

        // Show a toast message to the user
        Toast.makeText(context, "Weather update is in progress...", Toast.LENGTH_SHORT).show();

        // Send a local broadcast if you need to notify other components in your app
        Intent updateIntent = new Intent("com.example.paintsil_deborah_s2110986.UPDATE_WEATHER_TRIGGERED");
        LocalBroadcastManager.getInstance(context).sendBroadcast(updateIntent);

        // Add a log statement to indicate when the update broadcast is sent
        Log.d("DataUpdateReceiver", "Update weather broadcast sent at: " + new Date().toString());
    }
}
