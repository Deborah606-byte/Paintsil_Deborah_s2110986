//
// Name                 Deborah Ama Paintsil
// Student ID           s2110986
// Programme of Study   BSc (Hons) Computing
//
package com.example.paintsil_deborah_s2110986;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class WeatherItem implements Parcelable {
    private String cityName;
    private String temperature;
    private String weatherCondition;
    private String date;
    private String wind;
    private String humidity;
    private String pollution;
    private String minTemperature;
    private String maxTemperature;
    private String sunriseTime;
    private String sunsetTime;
    private String minTemperatureFahrenheit;
    private String maxTemperatureFahrenheit;
    private int nextDayWeatherIcon;
    private String nextDay;
    private String nextDayWeatherCondition;
    private String nextDayTemperature;
    private String nextDayWind;
    private String nextDayHumidity;
    private String nextDayPollution;
    private String dayThree;
    private String dayThreeWeatherCondition;
    private String dayThreeTemperature;
    private String dayThreeWeatherIcon;
    private String[] urls;

    public WeatherItem() {
        // Default constructor
    }
    // Getters and setters for the WeatherItem fields
    public void setUrls(ArrayList<String> urls) {
        this.urls = urls.toArray(new String[0]);
    }

    public String[] getUrls() {
        return urls;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getWeatherCondition() {
        return weatherCondition;
    }

    public void setWeatherCondition(String weatherCondition) {
        this.weatherCondition = weatherCondition;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getPollution() {
        return pollution;
    }

    public void setPollution(String pollution) {
        this.pollution = pollution;
    }
    public String getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(String minTemperature) {
        this.minTemperature = minTemperature;
    }

    public String getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(String maxTemperature) {
        this.maxTemperature = maxTemperature;
    }
    public String getMinTemperatureFahrenheit() {
        return minTemperatureFahrenheit;
    }

    public void setMinTemperatureFahrenheit(String minTemperatureFahrenheit) {
        this.minTemperatureFahrenheit = minTemperatureFahrenheit;
    }

    public String getMaxTemperatureFahrenheit() {
        return maxTemperatureFahrenheit;
    }

    public void setMaxTemperatureFahrenheit(String maxTemperatureFahrenheit) {
        this.maxTemperatureFahrenheit = maxTemperatureFahrenheit;
    }

    public String getSunriseTime() {
        return sunriseTime;
    }

    public void setSunriseTime(String sunriseTime) {
        this.sunriseTime = sunriseTime;
    }

    public String getSunsetTime() {
        return sunsetTime;
    }

    public void setSunsetTime(String sunsetTime) {
        this.sunsetTime = sunsetTime;
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
    public String getDayThree() {
        return dayThree;
    }

    public void setDayThree(String dayThree) {
        this.dayThree = dayThree;
    }

    public String getDayThreeWeatherCondition() {
        return dayThreeWeatherCondition;
    }

    public void setDayThreeWeatherCondition(String dayThreeWeatherCondition) {
        this.dayThreeWeatherCondition = dayThreeWeatherCondition;
    }

    public String getDayThreeTemperature() {
        return dayThreeTemperature;
    }

    public void setDayThreeTemperature(String dayThreeTemperature) {
        this.dayThreeTemperature = dayThreeTemperature;
    }

    public String getDayThreeWeatherIcon() {
        return dayThreeWeatherIcon;
    }

    public void setDayThreeWeatherIcon(String dayThreeWeatherIcon) {
        this.dayThreeWeatherIcon = dayThreeWeatherIcon;
    }

    // Parcelable implementation
    protected WeatherItem(Parcel in) {
        cityName = in.readString();
        temperature = in.readString();
        weatherCondition = in.readString();
        date = in.readString();
        humidity = in.readString();
        wind = in.readString();
        pollution = in.readString();
        minTemperature = in.readString();
        maxTemperature = in.readString();
        sunriseTime = in.readString();
        sunsetTime = in.readString();
        minTemperatureFahrenheit = in.readString();
        maxTemperatureFahrenheit = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(cityName);
        dest.writeString(temperature);
        dest.writeString(weatherCondition);
        dest.writeString(date);
        dest.writeString(humidity);
        dest.writeString(wind);
        dest.writeString(pollution);
        dest.writeString(minTemperature);
        dest.writeString(maxTemperature);
        dest.writeString(sunriseTime);
        dest.writeString(sunsetTime);
        dest.writeString(minTemperatureFahrenheit);
        dest.writeString(maxTemperatureFahrenheit);
    }

    public static final Creator<WeatherItem> CREATOR = new Creator<WeatherItem>() {
        @Override
        public WeatherItem createFromParcel(Parcel in) {
            return new WeatherItem(in);
        }

        @Override
        public WeatherItem[] newArray(int size) {
            return new WeatherItem[size];
        }
    };
}

