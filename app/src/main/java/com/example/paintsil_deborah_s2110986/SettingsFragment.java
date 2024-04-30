//
// Name                 Deborah Ama Paintsil
// Student ID           s2110986
// Programme of Study   BSc (Hons) Computing
//
package com.example.paintsil_deborah_s2110986;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.Calendar;


public class SettingsFragment extends Fragment {
    private TimePicker updateTime1Picker;
    private TimePicker updateTime2Picker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        updateTime1Picker = view.findViewById(R.id.updateTime1Picker);
        updateTime2Picker = view.findViewById(R.id.updateTime2Picker);

        // Set default times
        updateTime1Picker.setHour(8);
        updateTime1Picker.setMinute(0);
        updateTime2Picker.setHour(20);
        updateTime2Picker.setMinute(0);

        // Set listeners for TimePickers
        updateTime1Picker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                // Save the selected time
                saveUpdateTime(hourOfDay, minute, "updateTime1");
            }
        });

        updateTime2Picker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                // Save the selected time
                saveUpdateTime(hourOfDay, minute, "updateTime2");
            }
        });

        // Find the save button and set its click listener
        // Find the save button and set its click listener
        Button saveButton = view.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour1 = updateTime1Picker.getHour();
                int minute1 = updateTime1Picker.getMinute();
                saveUpdateTime(hour1, minute1, "updateTime1");

                int hour2 = updateTime2Picker.getHour();
                int minute2 = updateTime2Picker.getMinute();
                saveUpdateTime(hour2, minute2, "updateTime2");

                // Start the UpdateWeatherService to update the weather data
                Intent intent = new Intent(getActivity(), UpdateWeatherService.class);
                getActivity().startService(intent);

            }
        });

        return view;
    }

    private void saveUpdateTime(int hour, int minute, String key) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("WeatherAppSettings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key + "Hour", hour);
        editor.putInt(key + "Minute", minute);
        editor.apply();

        // Schedule the alarm
        scheduleWeatherUpdateAlarm(hour, minute, key);
    }

    @SuppressLint("ScheduleExactAlarm")
    private void scheduleWeatherUpdateAlarm(int hour, int minute, String key) {
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getActivity(), WeatherUpdateAlarmReceiver.class);

        // For Android Nougat (API level 24) and below, do not use FLAG_IMMUTABLE or FLAG_MUTABLE
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Calculate the time for the alarm
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        // Schedule the alarm
        if (calendar.before(Calendar.getInstance())) {
            // If the chosen time is in the past, schedule for the next day
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        // Set the alarm
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }


    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver, new IntentFilter("com.example.paintsil_deborah_s2110986.UPDATE_WEATHER"));
    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMessageReceiver);
        super.onPause();
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean isUpdateSuccessful = intent.getBooleanExtra("isUpdateSuccessful", false);
            if (isUpdateSuccessful) {
                Toast.makeText(context, "Weather data update settings saved successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Failed to save weather data update settings", Toast.LENGTH_SHORT).show();
            }
        }
    };

    public class WeatherUpdateAlarmReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Start the UpdateWeatherService to update the weather data
            Intent updateIntent = new Intent(context, UpdateWeatherService.class);
            context.startService(updateIntent);
        }
    }



}
