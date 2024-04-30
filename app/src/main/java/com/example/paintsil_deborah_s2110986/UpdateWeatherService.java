//
// Name                 Deborah Ama Paintsil
// Student ID           s2110986
// Programme of Study   BSc (Hons) Computing
//
package com.example.paintsil_deborah_s2110986;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

public class UpdateWeatherService extends Service {
    private static final String[] CITY_URLS = {
            "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/2648579",
            "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/2643743",
            "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/5128581",
            "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/287286",
            "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/934154",
            "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/1185241"
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("UpdateWeatherService", "Service started at: " + new Date().toString());
        // Call the method to update the weather data
        updateWeatherData();

        // Update the UI
        // This is a simplified example. In a real app, you might use a LocalBroadcastManager
        // or another method to communicate with your activity or fragment.
        Intent updateIntent = new Intent("com.example.paintsil_deborah_s2110986.UPDATE_WEATHER");
        updateIntent.putExtra("isUpdateSuccessful", true);
        LocalBroadcastManager.getInstance(this).sendBroadcast(updateIntent);

        return START_NOT_STICKY;
    }


    private void updateWeatherData() {
        for (String url : CITY_URLS) {
            new Thread(() -> {
                try {
                    URL weatherUrl = new URL(url);
                    HttpURLConnection connection = (HttpURLConnection) weatherUrl.openConnection();
                    connection.setRequestMethod("GET");
                    connection.connect();

                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        InputStream inputStream = connection.getInputStream();
                        // Parse the input stream to extract weather data
                        // Save the weather data to your local storage (e.g., SharedPreferences, SQLite database)
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    // Handle the error
                }
            }).start();
        }
    }
}

