package com.recko.app;

import android.os.Bundle;


import androidx.annotation.Nullable;
import androidx.preference.PreferenceFragmentCompat;


public class FragmentSettings extends PreferenceFragmentCompat {

    public FragmentSettings() {
        // Required empty public constructor
    }

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settings_preferences);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
