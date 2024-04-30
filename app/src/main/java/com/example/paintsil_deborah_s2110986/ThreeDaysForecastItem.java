//
// Name                 Deborah Ama Paintsil
// Student ID           s2110986
// Programme of Study   BSc (Hons) Computing
//
package com.example.paintsil_deborah_s2110986;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class ThreeDaysForecastItem implements Parcelable {
    private String day;
    private int weatherIcon;
    private String weatherCondition;
    private String temperature;

    private ArrayList<String> urls;

    private int nextDayWeatherIcon;
    private String nextDay;
    private String nextDayWeatherCondition;
    private String nextDayTemperature;
    private String nextDayWind;
    private String nextDayHumidity;
    private String nextDayPollution;

    private String weatheruv;
    private String weatherWindDirection;
    private String weatherWindSpeed;
    private String weatherHumidity;
    private String weatherPressure;
    private String weatherVisibility;

    private String pollution;
    public ThreeDaysForecastItem() {
    }

    protected ThreeDaysForecastItem(Parcel in) {
        day = in.readString();
        weatherIcon = in.readInt();
        weatherCondition = in.readString();
        temperature = in.readString();
        urls = in.createStringArrayList();
        nextDayWeatherIcon = in.readInt();
        nextDay = in.readString();
        nextDayWeatherCondition = in.readString();
        nextDayTemperature = in.readString();
        nextDayWind = in.readString();
        nextDayHumidity = in.readString();
        nextDayPollution = in.readString();
        weatheruv = in.readString();
        weatherWindDirection = in.readString();
        weatherWindSpeed = in.readString();
        weatherHumidity = in.readString();
        weatherPressure = in.readString();
        weatherVisibility = in.readString();
        pollution = in.readString();
    }

    public static final Creator<ThreeDaysForecastItem> CREATOR = new Creator<ThreeDaysForecastItem>() {
        @Override
        public ThreeDaysForecastItem createFromParcel(Parcel in) {
            return new ThreeDaysForecastItem(in);
        }

        @Override
        public ThreeDaysForecastItem[] newArray(int size) {
            return new ThreeDaysForecastItem[size];
        }
    };

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getWeatherIcon() {
        return weatherIcon;
    }

    public void setWeatherIcon(int weatherIcon) {
        this.weatherIcon = weatherIcon;
    }

    public String getWeatherCondition() {
        return weatherCondition;
    }

    public void setWeatherCondition(String weatherCondition) {
        this.weatherCondition = weatherCondition;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public ArrayList<String> getUrls() {
        return urls;
    }

    public void setUrls(ArrayList<String> urls) {
        this.urls = urls;
    }

    public int getNextDayWeatherIcon() {
        return nextDayWeatherIcon;
    }

    public void setNextDayWeatherIcon(int nextDayWeatherIcon) {
        this.nextDayWeatherIcon = nextDayWeatherIcon;
    }

    public String getNextDay() {
        return nextDay;
    }

    public void setNextDay(String nextDay) {
        this.nextDay = nextDay;
    }

    public String getNextDayWeatherCondition() {
        return nextDayWeatherCondition;
    }

    public void setNextDayWeatherCondition(String nextDayWeatherCondition) {
        this.nextDayWeatherCondition = nextDayWeatherCondition;
    }

    public String getNextDayTemperature() {
        return nextDayTemperature;
    }

    public void setNextDayTemperature(String nextDayTemperature) {
        this.nextDayTemperature = nextDayTemperature;
    }

    public String getNextDayWind() {
        return nextDayWind;
    }

    public void setNextDayWind(String nextDayWind) {
        this.nextDayWind = nextDayWind;
    }

    public String getNextDayHumidity() {
        return nextDayHumidity;
    }

    public void setNextDayHumidity(String nextDayHumidity) {
        this.nextDayHumidity = nextDayHumidity;
    }

    public String getNextDayPollution() {
        return nextDayPollution;
    }

    public void setNextDayPollution(String nextDayPollution) {
        this.nextDayPollution = nextDayPollution;
    }

    // Getter for weatherTemperature
    public String getWeatherUV() {
        return weatheruv;
    }

    // Setter for weatherTemperature
    public void setWeatherUV(String weatheruv) {
        this.weatheruv = weatheruv;
    }

    // Getter for weatherWindDirection
    public String getWeatherWindDirection() {
        return weatherWindDirection;
    }

    // Setter for weatherWindDirection
    public void setWeatherWindDirection(String weatherWindDirection) {
        this.weatherWindDirection = weatherWindDirection;
    }

    // Getter for weatherWindSpeed
    public String getWeatherWindSpeed() {
        return weatherWindSpeed;
    }

    // Setter for weatherWindSpeed
    public void setWeatherWindSpeed(String weatherWindSpeed) {
        this.weatherWindSpeed = weatherWindSpeed;
    }

    // Getter for weatherHumidity
    public String getWeatherHumidity() {
        return weatherHumidity;
    }

    // Setter for weatherHumidity
    public void setWeatherHumidity(String weatherHumidity) {
        this.weatherHumidity = weatherHumidity;
    }

    // Getter for weatherPressure
    public String getWeatherPressure() {
        return weatherPressure;
    }

    // Setter for weatherPressure
    public void setWeatherPressure(String weatherPressure) {
        this.weatherPressure = weatherPressure;
    }

    // Getter for weatherVisibility
    public String getWeatherVisibility() {
        return weatherVisibility;
    }

    // Setter for weatherVisibility
    public void setWeatherVisibility(String weatherVisibility) {
        this.weatherVisibility = weatherVisibility;
    }

    public String getPollution() {
        return pollution;
    }

    public void setPollution(String pollution) {
        this.pollution = pollution;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(day);
        dest.writeInt(weatherIcon);
        dest.writeString(weatherCondition);
        dest.writeString(temperature);
        dest.writeStringList(urls);
        dest.writeInt(nextDayWeatherIcon);
        dest.writeString(nextDay);
        dest.writeString(nextDayWeatherCondition);
        dest.writeString(nextDayTemperature);
        dest.writeString(nextDayWind);
        dest.writeString(nextDayHumidity);
        dest.writeString(nextDayPollution);
        dest.writeString(weatherPressure);
        dest.writeString(weatherVisibility);
        dest.writeString(weatherHumidity);
        dest.writeString(weatheruv);
        dest.writeString(weatherWindSpeed);
        dest.writeString(weatherWindDirection);
    }
}
