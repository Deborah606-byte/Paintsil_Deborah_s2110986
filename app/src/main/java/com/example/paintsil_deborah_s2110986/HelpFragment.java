package com.example.paintsil_deborah_s2110986;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.paintsil_deborah_s2110986.RecyclerView.FAQListAdapter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class HelpFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_help, container, false);

        List<FAQItem> faqItems = new ArrayList<>();
        faqItems.add(new FAQItem("How do I select a location to view the weather?", "You can select a location by searching for the city's name. The app will display a list of cities with weather information. Click on a city to view its detailed forecast."));
        faqItems.add(new FAQItem("How do I view the 3-day forecast for a city?", "After selecting a city, you can view the 3-day forecast by clicking on the '3 days forecast' button. This will show you the weather for the next three days, including min/max temperatures and weather classifications."));
        faqItems.add(new FAQItem("How do I navigate between locations?",
                "You can navigate between locations by selecting a different city from the list. The app allows you to cycle through the locations in a sequence, wrapping around to the start when you reach the end."));
        faqItems.add(new FAQItem("How often does the app update weather data?",
                "The app updates weather data when it starts and at regular intervals set by the user. The default update times are 08:00 and 20:00."));
        faqItems.add(new FAQItem("Can I view the current weather for a selected location?",
                "Yes, you can view the current weather for a selected location by accessing the \"latest observations\" feature. This will show you the current weather conditions, including temperature, weather classification, and more."));
        faqItems.add(new FAQItem("How do I use the app in portrait and landscape mode?",
                "The app is designed to display appropriately in both portrait and landscape mode. The layout adjusts to make the best use of screen space, ensuring that the weather information is clearly displayed regardless of the orientation."));
        faqItems.add(new FAQItem("Can I link back to the BBC Weather website or use HTML in a web view?",
                "No, linking back to the BBC Weather website or using HTML in a web view is not permitted. All functionality must be developed within the app using Android Studio and Java/XML."));
        faqItems.add(new FAQItem("Can I use Google Maps APIs or other external frameworks?",
                "Yes, you can use Google Maps APIs or other free mapping services in your application. However, using any other external frameworks is not permitted."));
        faqItems.add(new FAQItem("How do I change the update interval for weather data?",
                "You can change the update interval for weather data in the app's settings. The default intervals are 08:00 and 20:00, but you can customize these to suit your preferences."));
        faqItems.add(new FAQItem("What if I have a question not listed here?",
                "If you have any other questions or need further assistance, please refer to the app's about section for more information."));

        RecyclerView recyclerView = view.findViewById(R.id.FAQrecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        FAQListAdapter adapter = new FAQListAdapter(faqItems);
        recyclerView.setAdapter(adapter);

        return view;
    }
}
