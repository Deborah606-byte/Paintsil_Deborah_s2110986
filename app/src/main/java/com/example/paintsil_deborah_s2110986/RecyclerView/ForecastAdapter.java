//
// Name                 Deborah Ama Paintsil
// Student ID           s2110986
// Programme of Study   BSc (Hons) Computing
//
package com.example.paintsil_deborah_s2110986.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.paintsil_deborah_s2110986.R;
import com.example.paintsil_deborah_s2110986.ThreeDaysForecastItem;
import java.util.List;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder> {
    private List<ThreeDaysForecastItem> forecastItems;

    public ForecastAdapter(List<ThreeDaysForecastItem> forecastItems) {
        this.forecastItems = forecastItems;
    }

    @NonNull
    @Override
    public ForecastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_forecast, parent, false);
        return new ForecastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ForecastViewHolder holder, int position) {
        ThreeDaysForecastItem forecastItem = forecastItems.get(position);
        holder.dayTextView.setText(forecastItem.getDay());
        holder.conditionTextView.setText(forecastItem.getWeatherCondition());
        holder.temperatureTextView.setText(forecastItem.getTemperature());
        // Sets weather icon based on weather condition
        int weatherIconResourceId = getWeatherIcon(forecastItem.getWeatherCondition());
        holder.weatherIconImageView.setImageResource(weatherIconResourceId);
    }

    public static String categorizeWeatherCondition(String weatherCondition) {
        // Checks if weatherCondition is null
        if (weatherCondition == null) {
            // Handles the null case, for example, by returning a default value
            return "Unknown";
        }

        // Now it's safe to call toLowerCase() on weatherCondition
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
            return "Unknown";
        }
    }


    public static int getWeatherIcon(String weatherCondition) {
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
        return forecastItems.size();
    }
    public static class ForecastViewHolder extends RecyclerView.ViewHolder {
        TextView dayTextView, conditionTextView, temperatureTextView;
        ImageView weatherIconImageView;

        public ForecastViewHolder(@NonNull View itemView) {
            super(itemView);
            dayTextView = itemView.findViewById(R.id.dayTextView);
            conditionTextView = itemView.findViewById(R.id.dayConditionTextView);
            temperatureTextView = itemView.findViewById(R.id.dayTemperatureTextView);
            weatherIconImageView = itemView.findViewById(R.id.dayIconImageView); // Make sure this ID matches your layout
        }
    }

}
