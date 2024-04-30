//
// Name                 Deborah Ama Paintsil
// Student ID           s2110986
// Programme of Study   BSc (Hons) Computing
//
package com.example.paintsil_deborah_s2110986;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.os.Build;
import android.util.Log;
public class XMLPullParserHelper {
    private static final String TAG_CHANNEL = "channel";
    private static final String TAG_ITEM = "item";
    private static final String TAG_TITLE = "title";
    private static final String TAG_DESCRIPTION = "description";
    private static final String TAG_PUB_DATE = "pubDate";

    private static int itemCount = 0;

    public static WeatherItem parseFirstWeatherItem(InputStream inputStream) throws XmlPullParserException {
        WeatherItem firstItem = null;
        String cityName = null;
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(inputStream, null);

            int eventType = parser.getEventType();
            boolean isInChannel = false;

            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = null;
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        tagName = parser.getName();
                        if (TAG_CHANNEL.equals(tagName)) {
                            isInChannel = true;
                        } else if (TAG_TITLE.equals(tagName) && isInChannel) {
                            String title = parser.nextText();
                            cityName = parseCityNameFromTitle(title); // Extract city name from the <channel>'s <title>
                            Log.d("XmlPullParserHelper", "Channel Title: " + title);
                            Log.d("XmlPullParserHelper", "Extracted City Name: " + cityName);
                            isInChannel = false;
                        } else if (TAG_ITEM.equals(tagName) && !isInChannel) {
                            WeatherItem currentItem = new WeatherItem();
                            currentItem.setCityName(cityName);
                            boolean isFirstItem = true;

                            while (eventType != XmlPullParser.END_TAG || !TAG_ITEM.equals(parser.getName())) {
                                eventType = parser.next();
                                if (eventType == XmlPullParser.TEXT) {
                                    continue;
                                }
                                if (eventType == XmlPullParser.END_TAG) {
                                    if (TAG_ITEM.equals(parser.getName())) {
                                        if (isFirstItem) {
                                            firstItem = currentItem;
                                            isFirstItem = false;
                                        }
                                        currentItem = null;
                                    }
                                    continue;
                                }
                                if (eventType == XmlPullParser.START_TAG) {
                                    tagName = parser.getName();
                                    if (TAG_TITLE.equals(tagName)) {
                                        String title = parser.nextText();
                                        parseTitle(title, currentItem, cityName);
                                    } else if (TAG_DESCRIPTION.equals(tagName)) {
                                        String description = parser.nextText();
                                        parseDescription(description, currentItem);
                                    } else if (TAG_PUB_DATE.equals(tagName)) {
                                        String rawDate = parser.nextText();
                                        String trimmedDate = trimDate(rawDate);
                                        currentItem.setDate(trimmedDate);
                                    }
                                }
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (TAG_CHANNEL.equals(parser.getName())) {
                            isInChannel = false;
                        }
                        break;
                    default:
                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }

        if (firstItem != null && cityName != null) {
            firstItem.setCityName(cityName);
        }

        return firstItem;
    }


    private static String trimDate(String rawDate) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
        SimpleDateFormat outputFormat = new SimpleDateFormat("EEE, dd MMM yyyy");
        try {
            Date date = inputFormat.parse(rawDate);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return rawDate;
        }
    }

    private static void parseTitle(String title, WeatherItem item, String cityName) {
        // Set the city name extracted from the title
        item.setCityName(cityName);

        Pattern pattern = Pattern.compile(".*?: (.*?), Minimum Temperature: (-?\\d+)°C .*? Maximum Temperature: (-?\\d+)°C .*");
        Matcher matcher = pattern.matcher(title);
        if (matcher.find()) {
            String condition = matcher.group(1).trim();
            String minTemperature = matcher.group(2).trim();
            String maxTemperature = matcher.group(3).trim();
            item.setTemperature(minTemperature + "°C / " + maxTemperature + "°C");
            item.setWeatherCondition(condition);

            // Additional logging for debugging
            Log.d("XmlPullParserHelper", "Extracted Weather Condition: " + condition);
            Log.d("XmlPullParserHelper", "Extracted Temperature: " + minTemperature + "°C / " + maxTemperature + "°C");
        } else {
            // Log if the pattern does not match
            Log.d("XmlPullParserHelper", "Pattern did not match the title: " + title);
        }
    }

    private static final Map<String, String> CAPITAL_TO_COUNTRY_MAP = new HashMap<String, String>() {{
        put("Port Louis", "Mauritius");
        put("Muscat", "Oman");
        put("Dhaka", "Bangladesh");
    }};

    private static String parseCityNameFromTitle(String title) {
        String cityName = "";
        int startIndex = title.indexOf("Forecast for") + "Forecast for".length();
        int endIndex = title.indexOf(",", startIndex);

        if (startIndex >= 0 && endIndex >= 0 && endIndex > startIndex) {
            cityName = title.substring(startIndex, endIndex).trim();
            // Check if the extracted city name is a capital and replace it with the correct country name if necessary
            if (CAPITAL_TO_COUNTRY_MAP.containsKey(cityName)) {
                cityName = CAPITAL_TO_COUNTRY_MAP.get(cityName);
            }
        } else {
            Log.d("XmlPullParserHelper", "City Name not found in title: " + title);
        }
        return cityName;
    }


    private static void parseDescription(String description, WeatherItem item) {
        Log.d("XmlPullParserHelper", "Parsing description: " + description);
        Pattern pattern = Pattern.compile("Wind Direction: (\\w+),?|Wind Speed: (\\d+mph),?|Humidity: (\\d+)%,?|Pollution: (\\w+),?|Minimum Temperature: (\\d+)°[CF] \\((\\d+)°[CF]\\),?|Maximum Temperature: (\\d+)°[CF] \\((\\d+)°[CF]\\),?|Sunrise: (\\d{2}:\\d{2} [A-Z]+),?|Sunset: (\\d{2}:\\d{2} [A-Z]+),?");
        Matcher matcher = pattern.matcher(description);
        String wind = "", humidity = "", pollution = "", minTemperature = "", maxTemperature = "", sunrise = "", sunset = "";
        String minTemperatureFahrenheit = "", maxTemperatureFahrenheit = "";

        while (matcher.find()) {
            if (matcher.group(1) != null) {
                wind = matcher.group(1);
            } else if (matcher.group(2) != null) {
                wind += " " + matcher.group(2);
            } else if (matcher.group(3) != null) {
                humidity = matcher.group(3);
            } else if (matcher.group(4) != null) {
                pollution = matcher.group(4);
                Log.d("XmlPullParserHelper", "Pollution: " + pollution);
            } else if (matcher.group(5) != null) {
                minTemperature = matcher.group(5);
                minTemperatureFahrenheit = matcher.group(6);
            } else if (matcher.group(7) != null) {
                maxTemperature = matcher.group(7);
                maxTemperatureFahrenheit = matcher.group(8);
            } else if (matcher.group(9) != null) {
                sunrise = matcher.group(9);
                Log.d("XmlPullParserHelper", "Sunrise: " + sunrise);
            } else if (matcher.group(10) != null) {
                sunset = matcher.group(10);
                Log.d("XmlPullParserHelper", "Sunset: " + sunset);
            }
        }

        if (sunrise.isEmpty()) {
            Log.d("XmlPullParserHelper", "Sunrise not found in description");
        }

        if (sunset.isEmpty()) {
            Log.d("XmlPullParserHelper", "Sunset not found in description");
        }
        if (pollution.isEmpty()) {
            Log.d("XmlPullParserHelper", "Pollution not found in description");
        }

        item.setWind(wind);
        item.setHumidity(humidity);
        item.setPollution(pollution);
        item.setMinTemperature(minTemperature);
        item.setMaxTemperature(maxTemperature);
        item.setSunriseTime(sunrise);
        item.setSunsetTime(sunset);
        item.setMinTemperatureFahrenheit(minTemperatureFahrenheit);
        item.setMaxTemperatureFahrenheit(maxTemperatureFahrenheit);
    }
}