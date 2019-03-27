package com.course_project.arbitrage_analyzer.ui;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.course_project.arbitrage_analyzer.R;


public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        // Indicate here the XML resource you created above that holds the preferences
        setPreferencesFromResource(R.xml.settings, rootKey);
    }

}