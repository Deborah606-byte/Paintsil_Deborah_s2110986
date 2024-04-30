//
// Name                 Deborah Ama Paintsil
// Student ID           s2110986
// Programme of Study   BSc (Hons) Computing
//
package com.example.paintsil_deborah_s2110986;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class OnboardingAdapter extends FragmentStateAdapter {

    public OnboardingAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new Onboarding1Fragment();
            case 1:
                return new Onboarding2Fragment();
            case 2:
                return new Onboarding3Fragment();
            case 3:
                return new Onboarding4Fragment();
            // Add more cases for additional screens
            default:
                return new Onboarding1Fragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4; // Number of onboarding screens
    }
}

