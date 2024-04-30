//
// Name                 Deborah Ama Paintsil
// Student ID           s2110986
// Programme of Study   BSc (Hons) Computing
//
package com.example.paintsil_deborah_s2110986;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.paintsil_deborah_s2110986.RecyclerView.WeatherAdapter;

import org.w3c.dom.Text;

public class DetailFragment extends Fragment {
    private WeatherItem weatherItem;

    public DetailFragment() {
        // Required empty public constructor
    }

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

        return view;
    }
}