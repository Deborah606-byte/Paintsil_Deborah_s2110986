//
// Name                 Deborah Ama Paintsil
// Student ID           s2110986
// Programme of Study   BSc (Hons) Computing
//
package com.example.paintsil_deborah_s2110986;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Date;

public class DataUpdateReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("DataUpdateReceiver", "Data update triggered at: " + new Date().toString());
        Intent serviceIntent = new Intent(context, UpdateWeatherService.class);
        context.startService(serviceIntent);
    }
}

