//
// Name                 Deborah Ama Paintsil
// Student ID           s2110986
// Programme of Study   BSc (Hons) Computing
//
package com.example.paintsil_deborah_s2110986;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.xmlpull.v1.XmlPullParserException;

public class HomeFragment extends Fragment {
    public interface OnButtonClickListener {
        void onButtonClick();
    }

    private OnButtonClickListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnButtonClickListener) {
            mListener = (OnButtonClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnButtonClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Button searchCityButton = view.findViewById(R.id.searchCityButton);
        searchCityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the FragmentManager
                FragmentManager fragmentManager = getFragmentManager();
                // Begin the transaction
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                // Replace the current fragment with the SearchFragment
                transaction.replace(R.id.container, new SearchFragment());
                // Add the transaction to the back stack
                transaction.addToBackStack(null);
                // Commit the transaction
                transaction.commit();
            }
        });
        return view;
    }
}