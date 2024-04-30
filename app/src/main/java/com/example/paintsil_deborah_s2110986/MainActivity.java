package com.example.paintsil_deborah_s2110986;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements HomeFragment.OnButtonClickListener {

    Toolbar toolbar;
    BottomNavigationView bottomNavigationView;

    HomeFragment homeFragment = new HomeFragment();
    LocationFragment locationFragment = new LocationFragment();
    AboutFragment aboutFragment = new AboutFragment();

    SearchFragment searchFragment = new SearchFragment();
    SettingsFragment settingsFragment = new SettingsFragment();

    HelpFragment helpFragment = new HelpFragment();

    DaysFragment daysFragment = new DaysFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, UpdateWeatherService.class);
        startService(intent);

        // Send broadcast to trigger the toast message at app startup
        Intent updateWeatherIntent = new Intent("com.example.paintsil_deborah_s2110986.UPDATE_WEATHER_TRIGGERED");
        LocalBroadcastManager.getInstance(this).sendBroadcast(updateWeatherIntent);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Forecastify");

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                String title = "";

                if (item.getItemId() == R.id.home) {
                    selectedFragment = homeFragment;
                    title = "Weather";
                } else if (item.getItemId() == R.id.location) {
                    selectedFragment = locationFragment;
                    title = "Search";
                } else if (item.getItemId() == R.id.about) {
                    selectedFragment = aboutFragment;
                    title = "About Us";
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.container, selectedFragment).commit();
                getSupportActionBar().setTitle(title);

                return true;
            }
        });
        scheduleDataUpdates();

        LocalBroadcastManager.getInstance(this).registerReceiver(mUpdateWeatherReceiver,
                new IntentFilter("com.example.paintsil_deborah_s2110986.UPDATE_WEATHER_TRIGGERED"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.fragment_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.search) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, searchFragment).addToBackStack(null).commit();
            getSupportActionBar().setTitle("Search");
            return true;
        } else if (item.getItemId() == R.id.settings) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, settingsFragment).addToBackStack(null).commit();
            getSupportActionBar().setTitle("Settings");
            return true;
        } else if (item.getItemId() == R.id.help) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, helpFragment).addToBackStack(null).commit();
            getSupportActionBar().setTitle("Help");
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onButtonClick() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container, new DaysFragment());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void scheduleDataUpdates() {
        SharedPreferences sharedPreferences = getSharedPreferences("WeatherAppSettings", Context.MODE_PRIVATE);
        int updateTime1Hour = sharedPreferences.getInt("updateTime1Hour", 8);
        int updateTime1Minute = sharedPreferences.getInt("updateTime1Minute", 0);
        int updateTime2Hour = sharedPreferences.getInt("updateTime2Hour", 20);
        int updateTime2Minute = sharedPreferences.getInt("updateTime2Minute", 0);

        Log.d("MainActivity", "Scheduling updates for: " + updateTime1Hour + ":" + updateTime1Minute + " and " + updateTime2Hour + ":" + updateTime2Minute);

        scheduleUpdate(updateTime1Hour, updateTime1Minute);
        scheduleUpdate(updateTime2Hour, updateTime2Minute);
    }

    private void scheduleUpdate(int hour, int minute) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, DataUpdateReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    private BroadcastReceiver mUpdateWeatherReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "Weather data is being updated", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mUpdateWeatherReceiver,
                new IntentFilter("com.example.paintsil_deborah_s2110986.UPDATE_WEATHER_TRIGGERED"));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mUpdateWeatherReceiver);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mUpdateWeatherReceiver);
        super.onDestroy();
    }
}
