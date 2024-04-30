//
// Name                 Deborah Ama Paintsil
// Student ID           s2110986
// Programme of Study   BSc (Hons) Computing
//
package com.example.paintsil_deborah_s2110986;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.paintsil_deborah_s2110986.RecyclerView.ForecastAdapter;
import com.example.paintsil_deborah_s2110986.RecyclerView.WeatherAdapter;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.io.BufferedReader;
import java.io.InputStreamReader;


public class DaysFragment extends Fragment {
    private RecyclerView forecastRecyclerView;
    private ForecastAdapter forecastAdapter;
    private List<ThreeDaysForecastItem> forecastItems;
    private View view;

    private static final String[] predefinedUrls = {
            "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/2648579",
            "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/2643743",
            "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/5128581",
            "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/287286",
            "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/934154",
            "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/1185241"
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_days, container, false);

        forecastRecyclerView = view.findViewById(R.id.forecastRecyclerView);
        forecastRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        forecastItems = new ArrayList<>();
        forecastAdapter = new ForecastAdapter(forecastItems);
        forecastRecyclerView.setAdapter(forecastAdapter);

        // Retrieve the URL passed from the DetailFragment
        String url = null;
        if (getArguments() != null) {
            url = getArguments().getString("url");
        }

        // Log the retrieved URL
        Log.d("DaysFragment", "URL: " + url);

        // Compare the URL with the predefined URLs and fetch data only if it matches
        if (url != null && Arrays.asList(predefinedUrls).contains(url)) {
            Log.d("DaysFragment", "Matching URL found: " + url);
            fetchAndParseForecastData(url);
        } else {
            Log.d("DaysFragment", "No matching URL found.");
        }

        return view;
    }

    private void fetchAndParseForecastData(String url) {
        new Thread(() -> {
            try {
                URL forecastUrl = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) forecastUrl.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = connection.getInputStream();
                    List<ThreeDaysForecastItem> forecasts = ForecastXmlParser.parse(inputStream);
                    if (!forecasts.isEmpty()) {
                        // Update the UI on the main thread
                        getActivity().runOnUiThread(() -> {
                            synchronized (forecastItems) {
                                forecastItems.addAll(forecasts);
                            }
                            forecastAdapter.notifyDataSetChanged();

                            // Assuming you have a method to update the UI with the current day's forecast
                            updateCurrentDayForecastUI();

                            secondDayWeatherForecastUI();
                        });
                    }
                } else {
                    // Handle non-OK response codes
                    getActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), "An error occurred while fetching forecast data.", Toast.LENGTH_SHORT).show();
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
                // Handle exceptions
                getActivity().runOnUiThread(() -> {
                });
            }
        }).start();
    }

    private void secondDayWeatherForecastUI() {
        if (!forecastItems.isEmpty()) {
            ThreeDaysForecastItem secondDayForecast = forecastItems.get(1);

            ImageView nextDayweatherIconImageView = view.findViewById(R.id.nextDayweatherIconImageView);

            TextView nextDaytemperatureTextView = view.findViewById(R.id.nextDaytemperatureTextView);
            nextDaytemperatureTextView.setText(secondDayForecast.getTemperature());

            TextView nextDayweatherConditionTextView = view.findViewById(R.id.nextDayweatherConditionTextView);
            nextDayweatherConditionTextView.setText(secondDayForecast.getWeatherCondition());

            TextView nextDayWindTextView = view.findViewById(R.id.nextDayWindTextView);
            nextDayWindTextView.setText(secondDayForecast.getWeatherWindDirection());

            TextView nextDayHumidityTextView = view.findViewById(R.id.nextDayHumidityTextView);
            nextDayHumidityTextView.setText(secondDayForecast.getWeatherHumidity());

            TextView nextDayPollutionTextView = view.findViewById(R.id.nextDayPollutionTextView);
            nextDayPollutionTextView.setText(secondDayForecast.getPollution());

            // Used the getWeatherIcon method from ForecastAdapter to set the weather icon
            int weatherIconResource = ForecastAdapter.getWeatherIcon(secondDayForecast.getWeatherCondition());
            nextDayweatherIconImageView.setImageResource(weatherIconResource);
        }
    }

    private void updateCurrentDayForecastUI() {
        if (!forecastItems.isEmpty()) {
            ThreeDaysForecastItem currentDayForecast = forecastItems.get(0); // Since the current day is always at index 0

            // Updating the UI with the current day's forecast details
            TextView UVValueTextView = view.findViewById(R.id.uvValueTextView);
            UVValueTextView.setText(currentDayForecast.getWeatherUV());

            TextView windValueTextView = view.findViewById(R.id.windValueTextView);
            windValueTextView.setText(currentDayForecast.getWeatherWindDirection());

            TextView windSpeedValueTextView = view.findViewById(R.id.windSpeedValueTextView);
            windSpeedValueTextView.setText(currentDayForecast.getWeatherWindSpeed());

            TextView humidityValueTextView = view.findViewById(R.id.humidityValueTextView);
            humidityValueTextView.setText(currentDayForecast.getWeatherHumidity());

            TextView pressureValueTextView = view.findViewById(R.id.pressureValueTextView);
            pressureValueTextView.setText(currentDayForecast.getWeatherPressure());

            TextView visibilityValueTextView = view.findViewById(R.id.visibilityValueTextView);
            visibilityValueTextView.setText(currentDayForecast.getWeatherVisibility());
        }
    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }


}
