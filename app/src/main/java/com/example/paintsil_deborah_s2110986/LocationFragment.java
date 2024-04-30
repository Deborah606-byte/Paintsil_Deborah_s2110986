//
// Name                 Deborah Ama Paintsil
// Student ID           s2110986
// Programme of Study   BSc (Hons) Computing
//
package com.example.paintsil_deborah_s2110986;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.appcompat.widget.SearchView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class LocationFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Context mContext;
    private SearchView searchView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location, container, false);
        mContext = getActivity();

        // Initialize the SearchView
        searchView = view.findViewById(R.id.citySearchSearchView);
        // Get the EditText inside the SearchView
        EditText searchEditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Handle search query submission
                searchCity(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Optional: Handle text change
                return false;
            }
        });

        // Check for Google Play Services
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(getActivity());
        if (status != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(status)) {
                // Show dialog to resolve the error
                googleApiAvailability.getErrorDialog(getActivity(), status, 2404).show();
            } else {
                // Google Play Services is not supported
                // Handle the error in your app
            }
        } else {
            // Google Play Services is available and up to date
            SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Safely hide the action bar when the fragment is resumed
        if (getActivity() instanceof AppCompatActivity) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // Safely show the action bar when the fragment is paused
        if (getActivity() instanceof AppCompatActivity) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add markers for each city
        LatLng glasgow = new LatLng(55.8642, -4.2518);
        mMap.addMarker(new MarkerOptions().position(glasgow).title("Glasgow"));

        LatLng london = new LatLng(51.5074, -0.1278);
        mMap.addMarker(new MarkerOptions().position(london).title("London"));

        LatLng newYork = new LatLng(40.7128, -74.0060);
        mMap.addMarker(new MarkerOptions().position(newYork).title("New York"));

        LatLng oman = new LatLng(23.5888, 58.3842);
        mMap.addMarker(new MarkerOptions().position(oman).title("Oman"));

        LatLng mauritius = new LatLng(-20.2855, 57.4783);
        mMap.addMarker(new MarkerOptions().position(mauritius).title("Mauritius"));

        LatLng bangladesh = new LatLng(23.684997, 90.356331);
        mMap.addMarker(new MarkerOptions().position(bangladesh).title("Bangladesh"));

        // Move the camera to a specific location without adding a marker
        // For this example, let's center the map on London
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mauritius, 10));

        // Set up a listener for marker click.
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                fetchWeatherData(getCityUrl(marker.getTitle()), new WeatherDataCallback() {
                    @Override
                    public void onWeatherDataReceived(WeatherItem weatherItem) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showWeatherDialog(marker.getTitle(), weatherItem);
                            }
                        });
                    }
                });
                return false;
            }
        });
    }

    // Weather Data Callback interface
    public interface WeatherDataCallback {
        void onWeatherDataReceived(WeatherItem weatherItem);
    }

    private void fetchWeatherData(String cityUrl, WeatherDataCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
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

                    // Create a new XmlPullParser instance
                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    XmlPullParser parser = factory.newPullParser();
                    // Set the input for the parser
                    parser.setInput(new InputStreamReader(urlConnection.getInputStream()));

                    // Parse the XML data
                    WeatherItem weatherItem = XMLPullParserHelper.parseFirstWeatherItem(urlConnection.getInputStream());

                    // Close the connection
                    urlConnection.disconnect();

                    // Invoke callback
                    callback.onWeatherDataReceived(weatherItem);
                } catch (XmlPullParserException | IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void showWeatherDialog(String cityName, WeatherItem weatherItem) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.CustomAlertDialogTheme);
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.weather_location_item, null);
        builder.setView(dialogView);

        // Set the city name
        builder.setTitle(cityName);

        // Set the temperature
        TextView temperatureTextView = dialogView.findViewById(R.id.textView_temperature);
        temperatureTextView.setText("Temperature: " + weatherItem.getTemperature() + "°C");
        temperatureTextView.setTextColor(getResources().getColor(android.R.color.white));

        // Set the weather image
        ImageView weatherImageView = dialogView.findViewById(R.id.locationImageView);
        weatherImageView.setImageResource(getWeatherIconResource(weatherItem.getWeatherCondition()));

        // Set wind observation
        ImageView windyImageView = dialogView.findViewById(R.id.windyImageView);
        windyImageView.setImageResource(R.drawable.windy);
        TextView windyValueTextView = dialogView.findViewById(R.id.windyValueTextView);
        windyValueTextView.setText("Wind: " + weatherItem.getWind());
        windyValueTextView.setTextColor(getResources().getColor(android.R.color.white));
        TextView windyTextView = dialogView.findViewById(R.id.windyTextView);
        windyTextView.setText("Wind Direction");
        windyTextView.setTextColor(getResources().getColor(android.R.color.white));

        // Set humidity observation
        ImageView humidImageView = dialogView.findViewById(R.id.humideImageView);
        humidImageView.setImageResource(R.drawable.humidity);
        TextView humidValueTextView = dialogView.findViewById(R.id.humideValueTextView);
        humidValueTextView.setText("Humidity: " + weatherItem.getHumidity() + "%");
        humidValueTextView.setTextColor(getResources().getColor(android.R.color.white));
        TextView humidTextView = dialogView.findViewById(R.id.humideTextView);
        humidTextView.setText("Humidity");
        humidTextView.setTextColor(getResources().getColor(android.R.color.white));

        // Set pollution observation
        ImageView polluteImageView = dialogView.findViewById(R.id.polluteImageView);
        polluteImageView.setImageResource(R.drawable.ecology);
        TextView polluteValueTextView = dialogView.findViewById(R.id.polluteValueTextView);
        polluteValueTextView.setText("Pollution: " + weatherItem.getPollution());
        polluteValueTextView.setTextColor(getResources().getColor(android.R.color.white));
        TextView polluteTextView = dialogView.findViewById(R.id.polluteTextView);
        polluteTextView.setText("Pollution");
        polluteTextView.setTextColor(getResources().getColor(android.R.color.white));

        builder.setPositiveButton("OK", null);

        // Create the AlertDialog object and return it
        AlertDialog dialog = builder.create();
        dialog.show();
    }



    private String getCityUrl(String cityName) {
        switch (cityName) {
            case "Glasgow":
                return "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/2648579";
            case "London":
                return "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/2643743";
            case "New York":
                return "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/5128581";
            case "Oman":
                return "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/287286";
            case "Mauritius":
                return "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/934154";
            case "Bangladesh":
                return "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/1185241";
            default:
                return "";
        }
    }

    public static String categorizeWeatherCondition(String weatherCondition) {
        String condition = weatherCondition.toLowerCase();

        if (condition.contains("clear") || condition.contains("sunny")) {
            return "Clear";
        } else if (condition.contains("cloudy") || condition.contains("overcast") || condition.contains("cloud")){
            return "Cloudy";
        } else if (condition.contains("rain") || condition.contains("drizzle") || condition.contains("shower")) {
            return "Rainy";
        } else if (condition.contains("snow") || condition.contains("flurry")) {
            return "Snowy";
        } else if (condition.contains("storm") || condition.contains("thunder")) {
            return "Stormy";
        } else if (condition.contains("windy") || condition.contains("gust")) {
            return "Windy";
        } else if (condition.contains("mist")) {
            return "Mist";
        } else if (condition.contains("fog")) {
            return "Fog";
        } else {
            return "Unknown"; // Default category if no match found
        }
    }

    public static int getWeatherIconResource(String weatherCondition) {
        String category = categorizeWeatherCondition(weatherCondition);

        switch (category) {
            case "Clear":
                return R.drawable.day_clear;
            case "Cloudy":
                return R.drawable.cloudy;
            case "Rainy":
                return R.drawable.rain;
            case "Snowy":
                return R.drawable.snow;
            case "Stormy":
                return R.drawable.thunder;
            case "Windy":
                return R.drawable.wind;
            case "Mist":
                return R.drawable.mist;
            case "Fog":
                return R.drawable.fog;
            default:
                return R.drawable.day_clear;
        }
    }

    private void searchCity(String cityName) {
        // Define the LatLng for each city
        Map<String, LatLng> cities = new HashMap<>();
        cities.put("Glasgow", new LatLng(55.8642, -4.2518));
        cities.put("London", new LatLng(51.5074, -0.1278));
        cities.put("New York", new LatLng(40.7128, -74.0060));
        cities.put("Oman", new LatLng(23.5888, 58.3842));
        cities.put("Mauritius", new LatLng(-20.2855, 57.4783));
        cities.put("Bangladesh", new LatLng(23.684997, 90.356331));

        // Move the camera to the city's location if it exists
        if (cities.containsKey(cityName)) {
            LatLng cityLocation = cities.get(cityName);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cityLocation, 10));
            mMap.addMarker(new MarkerOptions().position(cityLocation).title(cityName));
        } else {
            // City not found, show a message
            Toast.makeText(getContext(), "City not found", Toast.LENGTH_SHORT).show();
        }
    }


}
