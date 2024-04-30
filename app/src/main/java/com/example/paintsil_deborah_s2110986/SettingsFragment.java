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
import android.util.Log;
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

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // No need to display toast message here
            // This receiver is only responsible for handling broadcast about saving settings
            boolean isUpdateSuccessful = intent.getBooleanExtra("isUpdateSuccessful", false);
            if (isUpdateSuccessful) {
                // Log successful update
                Log.d("SettingsFragment", "Weather data update settings saved successfully");
            } else {
                // Log failed update
                Log.d("SettingsFragment", "Failed to save weather data update settings");
            }
        }
    };

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
                saveUpdateTime(hourOfDay, minute, "updateTime1");
            }
        });

        updateTime2Picker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
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

                // Display toast message for scheduled update times
                String toastMessage = "Weather will be updated at " + hour1 + ":" + minute1 + " and " + hour2 + ":" + minute2;
                Toast.makeText(getActivity(), toastMessage, Toast.LENGTH_SHORT).show();

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

        // Debug log to track when update times are saved
        Log.d("SettingsFragment", "Update time saved: " + key + " - " + hour + ":" + minute);
    }

    @SuppressLint("ScheduleExactAlarm")
    private void scheduleWeatherUpdateAlarm(int hour, int minute, String key) {
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getActivity(), DataUpdateReceiver.class);

        // Use a unique request code for each alarm
        int requestCode = (key.equals("updateTime1") ? 1 : 2);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        // Use setExactAndAllowWhileIdle for API level 23 and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
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


}
