//
// Name                 Deborah Ama Paintsil
// Student ID           s2110986
// Programme of Study   BSc (Hons) Computing
//
package com.example.paintsil_deborah_s2110986;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.paintsil_deborah_s2110986.RecyclerView.WeatherAdapter;

import org.w3c.dom.Text;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

public class DetailFragment extends Fragment {
    private WeatherItem weatherItem;

    public DetailFragment() {
        // Required empty public constructor
    }

    private static final String[] CITY_NAMES = {
            "Glasgow",
            "London",
            "New York",
            "Oman",
            "Mauritius",
            "Bangladesh"
    };

    // Define a static array of city URLs
    private static final String[] CITY_URLS = {
            "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/2648579",
            "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/2643743",
            "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/5128581",
            "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/287286",
            "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/934154",
            "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/1185241"
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            weatherItem = getArguments().getParcelable("weatherItem");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        // Initialize views and display weather information
        TextView cityNameTextView = view.findViewById(R.id.cityTextView);
        ImageView imageWeatherIcon = view.findViewById(R.id.weatherImageView);
        TextView tempTextView = view.findViewById(R.id.tempTextView);
        TextView conditionTextView = view.findViewById(R.id.conditionTextView);
        TextView weatherDateTextView = view.findViewById(R.id.weatherDateTextView);
        TextView windValueTextView = view.findViewById(R.id.windValueTextView);
        TextView humidityValueTextView = view.findViewById(R.id.humidityValueTextView);
        TextView pollutionValueTextView = view.findViewById(R.id.pollutionValueTextView);
        TextView minTempTextView = view.findViewById(R.id.minTempTextView);
        TextView minTempFahrenhTextView = view.findViewById(R.id.minTempFahrenhTextView);
        TextView maxTempTextView = view.findViewById(R.id.maxTempTextView);
        TextView maxTempFahrenTextView = view.findViewById(R.id.maxTempFahrenTextView);
        TextView sunriseTimeValueTextView = view.findViewById(R.id.sunriseTimeValueTextView);
        TextView sunsetTimeValueTextView = view.findViewById(R.id.sunsetTimeValueTextView);

        // Find the "3 Days Forecast" button
        Button forecastButton = view.findViewById(R.id.weatherButton);

        if (weatherItem != null) {
            cityNameTextView.setText(weatherItem.getCityName());
            tempTextView.setText(weatherItem.getTemperature());
            conditionTextView.setText(weatherItem.getWeatherCondition());
            weatherDateTextView.setText(weatherItem.getDate());
            windValueTextView.setText(weatherItem.getWind());
            humidityValueTextView.setText(weatherItem.getHumidity());
            pollutionValueTextView.setText(weatherItem.getPollution());
            minTempTextView.setText(weatherItem.getMinTemperature() + "°C");
            minTempFahrenhTextView.setText("( " + weatherItem.getMinTemperatureFahrenheit() + "°F )");
            maxTempTextView.setText(weatherItem.getMaxTemperature() + "°C");
            maxTempFahrenTextView.setText("(" + weatherItem.getMaxTemperatureFahrenheit() + "°F )");
            sunriseTimeValueTextView.setText(weatherItem.getSunriseTime());
            sunsetTimeValueTextView.setText(weatherItem.getSunsetTime());
            // Set other views with weather information

            // Set the weather icon based on the weather condition
            int iconResource = WeatherAdapter.getWeatherIconResource(weatherItem.getWeatherCondition());
            imageWeatherIcon.setImageResource(iconResource);
        }

        // Set an OnClickListener for the button
        forecastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment daysFragment = new DaysFragment();
                Bundle bundle = new Bundle();
                // Pass the URL to the DaysFragment
                if (weatherItem != null && weatherItem.getUrls().length > 0) {
                    bundle.putString("url", weatherItem.getUrls()[0]); // Accessing the first URL in the array
                    daysFragment.setArguments(bundle);

                    FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.container, daysFragment);
                    transaction.addToBackStack(null); // Optional, to allow back navigation
                    transaction.commit();
                } else {
                    // Handle the case where the URLs array is empty or null
                    // For example, display a toast indicating no URL available
                    Toast.makeText(getContext(), "No URL available", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Button sequenceButton = view.findViewById(R.id.sequenceButton);

        // Normalize the city name from the WeatherItem
        String normalizedCityName = weatherItem.getCityName().trim().toLowerCase();

        // Debugging: Log the normalized city name
        Log.d("DetailFragment", "Normalized city name: " + normalizedCityName);

        // Find the position of this city in the city_names_array
        int pos = -1;
        for (int i = 0; i < CITY_NAMES.length; i++) {
            String normalizedArrayName = CITY_NAMES[i].trim().toLowerCase();
            if (normalizedArrayName.equals(normalizedCityName)) {
                pos = i;
                if (i+1 < CITY_NAMES.length) { // Check if i+1 is within bounds
                    sequenceButton.setText("View details for " + CITY_NAMES[i+1]);
                } else {
                    // Handle the case where i+1 is out of bounds, e.g., set a default text or wrap around to the first city
                    sequenceButton.setText(CITY_NAMES[0]); // Example: set the text to the first city
                }
                break;
            }
        }



        if (pos != -1) { // Ensure the city name was found in the array
            // Your existing code to set the sequence button text and handle the button click remains unchanged
        } else {
            // Debugging: Log when the city name is not found
            Log.d("DetailFragment", "City not found in the list: " + weatherItem.getCityName());
            Toast.makeText(getContext(), "City not found in the list", Toast.LENGTH_SHORT).show();
        }

        // Set an OnClickListener for the sequenceButton
        sequenceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve the current position of the city in the list
                int currentPosition = -1;
                for (int i = 0; i < CITY_NAMES.length; i++) {
                    if (CITY_NAMES[i].trim().toLowerCase().equals(normalizedCityName)) {
                        currentPosition = i;
                        break;
                    }
                }
                if (currentPosition != -1) {
                    // Increase the position by 1 to get the next city
                    int newPosition = (currentPosition + 1) % CITY_NAMES.length; // Use modulo to loop back to the first city after the last

                    // Get the new city name and URL
                    String newCityName = CITY_NAMES[newPosition];
                    String newCityUrl = CITY_URLS[newPosition];

                    // Log the selected city name
                    Log.d("CitySelection", "Selected city: " + newCityName);

                    // Update the button text with the new city name
                    sequenceButton.setText("View details for " + newCityName);

                    // Fetch weather data for the new city
                    fetchWeatherData(newCityUrl);
                } else {
                    // Handle the case where the city name was not found in the array
                    Toast.makeText(getContext(), "City not found in the list", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void fetchWeatherData(String cityUrl) {
        new Thread(() -> {
            try {
                // Create a URL object
                URL url = new URL(cityUrl);
                // Open a connection to the URL
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();

                // Check the response code
                if (urlConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return;
                }

                // Parse the XML data
                WeatherItem weatherItem = XMLPullParserHelper.parseFirstWeatherItem(urlConnection.getInputStream());

                // Close the connection
                urlConnection.disconnect();

                // Update the UI on the main thread
                getActivity().runOnUiThread(() -> {
                    if (weatherItem != null) {
                        // Update the weather details
                        updateWeatherDetails(weatherItem);
                    } else {
                        // Handle the case where no weather data is available
                        Toast.makeText(getContext(), "No weather data available", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (XmlPullParserException | IOException e) {
                e.printStackTrace();
                // Handle exceptions
                getActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), "An error occurred while fetching weather data.", Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }



    private void updateWeatherDetails(WeatherItem weatherItem) {
        if (weatherItem != null) {
            // Create a new instance of DetailFragment
            DetailFragment detailFragment = new DetailFragment();

            // Create a Bundle to pass the WeatherItem to the DetailFragment
            Bundle bundle = new Bundle();
            bundle.putParcelable("weatherItem", weatherItem);
            detailFragment.setArguments(bundle);

            // Use FragmentManager to replace the current fragment with DetailFragment
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.container, detailFragment); // Assuming R.id.container is the ID of your container view
            transaction.addToBackStack(null); // Optional, to allow back navigation
            transaction.commit();
        }
    }



}