package com.example.lee.suesnews.ui.fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.example.lee.suesnews.R;

/**
 * Created by Administrator on 2015/3/1.
 */
public class SettingFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);
    }
}
