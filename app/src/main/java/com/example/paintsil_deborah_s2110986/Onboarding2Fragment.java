//
// Name                 Deborah Ama Paintsil
// Student ID           s2110986
// Programme of Study   BSc (Hons) Computing
//
package com.example.paintsil_deborah_s2110986;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class Onboarding2Fragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_onboarding2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button nextButton = view.findViewById(R.id.next1Button);
        ViewPager2 viewPager = (ViewPager2) requireActivity().findViewById(R.id.viewPager);

        nextButton.setOnClickListener(v -> {
            // Navigate to the next onboarding screen
            int nextItem = viewPager.getCurrentItem() + 1;
            if (nextItem < viewPager.getAdapter().getItemCount()) {
                viewPager.setCurrentItem(nextItem, true);
            }
        });
    }

}