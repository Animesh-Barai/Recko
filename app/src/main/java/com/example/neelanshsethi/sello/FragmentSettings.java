package com.example.neelanshsethi.sello;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;


public class FragmentSettings extends PreferenceFragmentCompat {

    public FragmentSettings() {
        // Required empty public constructor
    }



    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settings_preferences);
    }



    @Override
    public void onDetach() {
        super.onDetach();
    }

}
