//
// Name                 Deborah Ama Paintsil
// Student ID           s2110986
// Programme of Study   BSc (Hons) Computing
//
package com.example.paintsil_deborah_s2110986.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.paintsil_deborah_s2110986.R;
import com.example.paintsil_deborah_s2110986.WeatherItem;

import java.util.List;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder> {

    public interface OnItemClickListener {
        void onItemSelected(WeatherItem weatherItem); // Renamed method
        void onItemClick(View view, WeatherItem weatherItem);
    }
    private List<WeatherItem> weatherItems;

    private OnItemClickListener listener;

    public WeatherAdapter(List<WeatherItem> weatherItems, OnItemClickListener listener) {
        this.weatherItems = weatherItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_weather_city, parent, false);
        return new WeatherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherViewHolder holder, int position) {
        WeatherItem weatherItem = weatherItems.get(position);
        holder.itemView.setOnClickListener(v -> {
            // Pass the clicked item to the listener
            listener.onItemClick(v, weatherItem); // Corrected to pass the view and the weatherItem
        });
        holder.cityNameTextView.setText(weatherItem.getCityName());
        holder.weatherConditionTextView.setText(weatherItem.getWeatherCondition());
        holder.temperatureTextView.setText(weatherItem.getTemperature());

        // Set the weather icon based on the weather condition
        int iconResource = getWeatherIconResource(weatherItem.getWeatherCondition());
        holder.weatherIconImageView.setImageResource(iconResource);
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

    @Override
    public int getItemCount() {
        return weatherItems.size();
    }

    public static class WeatherViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout weatherItemLayout;
        TextView cityNameTextView, weatherConditionTextView, temperatureTextView;
        ImageView weatherIconImageView;

        public WeatherViewHolder(@NonNull View itemView) {
            super(itemView);
            weatherItemLayout = itemView.findViewById(R.id.weatherItemLayout);
            cityNameTextView = itemView.findViewById(R.id.cityNameTextView);
            weatherConditionTextView = itemView.findViewById(R.id.weatherConditionTextView);
            temperatureTextView = itemView.findViewById(R.id.temperatureTextView);
            weatherIconImageView = itemView.findViewById(R.id.weatherIconImageView);
        }
    }
}