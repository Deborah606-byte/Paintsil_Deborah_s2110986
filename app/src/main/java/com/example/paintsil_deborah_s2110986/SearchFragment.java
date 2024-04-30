//
// Name                 Deborah Ama Paintsil
// Student ID           s2110986
// Programme of Study   BSc (Hons) Computing
//
package com.example.paintsil_deborah_s2110986;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewCities);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        weatherItems = new ArrayList<>();
        filteredWeatherItems = new ArrayList<>();

        weatherAdapter = new WeatherAdapter(filteredWeatherItems, this);
        recyclerView.setAdapter(weatherAdapter);

        EditText searchCityEditText = view.findViewById(R.id.searchCityEditText);
        searchCityEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterWeatherItems(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
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
            if (isNetworkAvailable()) {
                fetchAndParseWeatherData(url);
            } else {
                // Display a message to the user about no internet connectivity
                Log.d("NetworkStatus", "No internet connectivity. Displaying toast message.");
                Toast.makeText(getContext(), "No internet connectivity. Please try again later.", Toast.LENGTH_SHORT).show();
            }
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

                });
            }
        }).start();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = activeNetworkInfo != null && activeNetworkInfo.isConnected();
        Log.d("NetworkStatus", "isNetworkAvailable: " + isConnected);
        return isConnected;
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

        if (filteredWeatherItems.isEmpty() && !query.isEmpty()) {
            // Display a toast message indicating the city is not available
            Toast.makeText(getContext(), "City '" + query + "' not available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemSelected(WeatherItem weatherItem) {

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