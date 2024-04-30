//
// Name                 Deborah Ama Paintsil
// Student ID           s2110986
// Programme of Study   BSc (Hons) Computing
//
package com.example.paintsil_deborah_s2110986;

import android.util.Log;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForecastXmlParser {
    public static List<ThreeDaysForecastItem> parse(InputStream inputStream) {
        Log.d("ForecastXmlParser", "Parsing started");
        List<ThreeDaysForecastItem> forecastItems = new ArrayList<>();

        try {
            // Read the input stream into a byte array
            byte[] xmlBytes = readFully(inputStream);

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new ByteArrayInputStream(xmlBytes), "UTF-8");

            int eventType = parser.getEventType();
            ThreeDaysForecastItem currentForecastItem = null;

            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = null;
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        tagName = parser.getName();
                        if ("item".equals(tagName)) {
                            currentForecastItem = new ThreeDaysForecastItem();
                        } else if (currentForecastItem != null) {
                            parseTag(tagName, parser, currentForecastItem);
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        tagName = parser.getName();
                        if ("item".equals(tagName) && currentForecastItem != null) {
                            forecastItems.add(currentForecastItem);
                            currentForecastItem = null;
                        }
                        break;
                }
                eventType = parser.next();
            }
            Log.d("ForecastXmlParser", "Parsing finished");
        } catch (XmlPullParserException | IOException e) {
            Log.e("ForecastXmlParser", "Error occurred during parsing", e);
            // Clear the list if parsing fails to avoid displaying partial data
            forecastItems.clear();
        }

        return forecastItems;
    }

    // Helper method to read the input stream into a byte array
    private static byte[] readFully(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, bytesRead);
        }
        return byteArrayOutputStream.toByteArray();
    }


    private static void parseTag(String tagName, XmlPullParser parser, ThreeDaysForecastItem currentForecastItem) throws XmlPullParserException, IOException {
        switch (tagName) {
            case "title":
                String title = parser.nextText();
                parseTitle(title, currentForecastItem);
                break;
            case "description":
                String description = parser.nextText();
                parseDescription(description, currentForecastItem);
                break;
            // Add additional cases for other tags you want to parse
        }
    }

    private static void parseTitle(String title, ThreeDaysForecastItem currentForecastItem) {
        // Pattern to match the title format and extract relevant information
        Pattern pattern = Pattern.compile("(.*?): (.*?), Minimum Temperature: (-?\\d+)°[CF] .*? Maximum Temperature: (-?\\d+)°[CF]");
        Matcher matcher = pattern.matcher(title);

        if (matcher.find()) {
            String day = matcher.group(1).trim(); // Extract the day
            String weatherCondition = matcher.group(2).trim(); // Extract the weather condition
            String minTemperature = matcher.group(3).trim(); // Extract the minimum temperature
            String maxTemperature = matcher.group(4).trim(); // Extract the maximum temperature

            // If the title contains "Tonight", set the day as "Today"
            if (day.contains("Tonight")) {
                day = "Today";
            }

            // Set the extracted information in the currentForecastItem object
            currentForecastItem.setDay(day);
            currentForecastItem.setWeatherCondition(weatherCondition);
            currentForecastItem.setTemperature(minTemperature + "°C / " + maxTemperature + "°C");

            // Additional logging for debugging
            Log.d("ForecastXmlParser", "Extracted Day: " + day);
            Log.d("ForecastXmlParser", "Extracted Weather Condition: " + weatherCondition);
            Log.d("ForecastXmlParser", "Extracted Temperature: " + minTemperature + "°C / " + maxTemperature + "°C");
        } else {
            // Log if the pattern does not match
            Log.d("ForecastXmlParser", "Pattern did not match the title: " + title);
        }
    }


    private static void parseDescription(String description, ThreeDaysForecastItem currentForecastItem) {
        // Pattern to match only the necessary information
        Pattern pattern = Pattern.compile("Maximum Temperature: (\\d+)°C \\((.*?)°F\\), Wind Direction: (.*?), Wind Speed: (\\d+)mph, Visibility: (.*?), Pressure: (\\d+)mb, Humidity: (\\d+)%, UV Risk: (\\d+), Pollution: (\\S*),");
        Matcher matcher = pattern.matcher(description);

        if (matcher.find()) {
            String maxTemperatureC = matcher.group(1); // Extract maximum temperature in Celsius
            String maxTemperatureF = matcher.group(2); // Extract maximum temperature in Fahrenheit
            String windDirection = matcher.group(3); // Extract wind direction
            String windSpeed = matcher.group(4); // Extract wind speed
            String visibility = matcher.group(5); // Extract visibility
            String pressure = matcher.group(6); // Extract pressure
            String humidity = matcher.group(7); // Extract humidity
            String uvRisk = matcher.group(8);
            String pollution = matcher.group(9);

            // Set the extracted information in the currentForecastItem object
            currentForecastItem.setWeatherUV(uvRisk);
            currentForecastItem.setWeatherWindDirection(windDirection);
            currentForecastItem.setWeatherWindSpeed(windSpeed + "mph");
            currentForecastItem.setWeatherVisibility(visibility);
            currentForecastItem.setWeatherPressure(pressure + "mb");
            currentForecastItem.setWeatherHumidity(humidity + "%");
            currentForecastItem.setPollution(pollution);

            // Additional logging for debugging
            Log.d("ForecastXmlParser", "Extracted Max Temperature: " + maxTemperatureC + "°C / " + maxTemperatureF + "°F");
            Log.d("ForecastXmlParser", "Extracted Wind Direction: " + windDirection);
            Log.d("ForecastXmlParser", "Extracted Wind Speed: " + windSpeed + "mph");
            Log.d("ForecastXmlParser", "Extracted Visibility: " + visibility);
            Log.d("ForecastXmlParser", "Extracted Pressure: " + pressure + "mb");
            Log.d("ForecastXmlParser", "Extracted Humidity: " + humidity + "%");
            Log.d("ForecastXmlParser", "Extracted Pollution: " + pollution);
        } else {
            // Log if the pattern does not match
            Log.d("ForecastXmlParser", "Pattern did not match the description: " + description);
        }
    }

}
