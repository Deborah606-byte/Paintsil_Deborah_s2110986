//
// Name                 Deborah Ama Paintsil
// Student ID           s2110986
// Programme of Study   BSc (Hons) Computing
//
package com.example.paintsil_deborah_s2110986;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.paintsil_deborah_s2110986.RecyclerView.WeatherAdapter;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchFragment extends Fragment implements WeatherAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private WeatherAdapter weatherAdapter;
    private List<WeatherItem> weatherItems;
    private List<WeatherItem> filteredWeatherItems;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        // Initialize the RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewCities);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize your weatherItems list here
        weatherItems = new ArrayList<>();
        filteredWeatherItems = new ArrayList<>();

        // Create an instance of the WeatherAdapter
        weatherAdapter = new WeatherAdapter(filteredWeatherItems, this);
        recyclerView.setAdapter(weatherAdapter);

        // Find the EditText and set the OnEditorActionListener
        EditText searchCityEditText = view.findViewById(R.id.searchCityEditText);
        searchCityEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not used
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Filter the weather items as the user types
                filterWeatherItems(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not used
            }
        });

        // Fetch and parse weather data for each URL
        String[] urls = {
                "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/2648579",
                "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/2643743",
                "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/5128581",
                "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/287286",
                "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/934154",
                "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/1185241"
        };

        for (String url : urls) {
            fetchAndParseWeatherData(url);
        }

        return view;
    }

    private void fetchAndParseWeatherData(String url) {
        new Thread(() -> {
            try {
                URL weatherUrl = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) weatherUrl.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = connection.getInputStream();
                    WeatherItem weatherItem = XMLPullParserHelper.parseFirstWeatherItem(inputStream);
                    if (weatherItem != null) {
                        ArrayList<String> urlList = new ArrayList<>();
                        urlList.add(url); // Add the URL to the ArrayList
                        weatherItem.setUrls(urlList); // Set the ArrayList as URLs
                        // Update the UI on the main thread
                        getActivity().runOnUiThread(() -> {
                            weatherItems.add(weatherItem);
                            filteredWeatherItems.add(weatherItem); // Add to filtered list as well
                            weatherAdapter.notifyDataSetChanged();
                        });
                    }
                } else {
                    // Handle non-OK response codes
                    getActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), "An error occurred while fetching weather data.", Toast.LENGTH_SHORT).show();
                    });

                }
            } catch (IOException | XmlPullParserException e) {
                e.printStackTrace();
                // Handle exceptions
                getActivity().runOnUiThread(() -> {
                    // Display an error message to the user
                    // This is a placeholder. You'll need to implement the logic to display an error message.
                });
            }
        }).start();
    }

    private void filterWeatherItems(String query) {
        filteredWeatherItems.clear();
        if (!query.isEmpty()) {
            for (WeatherItem item : weatherItems) {
                if (item.getCityName().toLowerCase().contains(query.toLowerCase())) {
                    filteredWeatherItems.add(item);
                }
            }
        }
        weatherAdapter.notifyDataSetChanged();

        // Check if the filtered list is empty and the query is not empty
        if (filteredWeatherItems.isEmpty() && !query.isEmpty()) {
            // Display a toast message indicating the city is not available
            Toast.makeText(getContext(), "City '" + query + "' not available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemSelected(WeatherItem weatherItem) {
        // Handle item selection here
        // This method is now correctly named and matches the interface definition
    }

    @Override
    public void onItemClick(View view, WeatherItem weatherItem) {
        // Pass the weather item and city ID to the DetailFragment
        Bundle bundle = new Bundle();
        ArrayList<String> urlsList = new ArrayList<>(Arrays.asList(weatherItem.getUrls()));
        bundle.putStringArrayList("urls", urlsList);
        bundle.putParcelable("weatherItem", weatherItem);
        DetailFragment detailFragment = new DetailFragment();
        detailFragment.setArguments(bundle);

        // Replace the current fragment with the DetailFragment
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container, detailFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


}