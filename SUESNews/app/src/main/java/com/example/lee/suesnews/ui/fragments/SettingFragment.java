package com.example.lee.suesnews.ui.fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.example.lee.suesnews.R;
import com.example.lee.suesnews.ui.widget.GestureFrameLayout;

/**
 * Created by Administrator on 2015/3/1.
 */
public class SettingFragment extends PreferenceFragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);

    }

    @Override
    public void onResume() {
        super.onResume();
        // Registers a listener whenever a key changes
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(
                (android.content.SharedPreferences.OnSharedPreferenceChangeListener) getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(
                (android.content.SharedPreferences.OnSharedPreferenceChangeListener) getActivity());
    }
}
