package com.example.lee.suesnews.ui.fragments;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.widget.Toast;

import com.example.lee.suesnews.R;
import com.example.lee.suesnews.biz.NewsItemBiz;
import com.example.lee.suesnews.ui.MainActivity;
import com.example.lee.suesnews.ui.widget.GestureFrameLayout;
import com.umeng.fb.FeedbackAgent;
import com.umeng.update.UmengDialogButtonListener;
import com.umeng.update.UmengUpdateAgent;

/**
 *
 * Created by Administrator on 2015/3/1.
 */
public class SettingFragment extends PreferenceFragment {

    NewsItemBiz biz;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);
        biz = new NewsItemBiz(this.getActivity());

    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        String key = preference.getKey();
        String clearCache = getResources().getString(R.string.setting_key_clear_cache);
        String postback = getResources().getString(R.string.setting_key_post_back);
        String checkUpdate = getResources().getString(R.string.setting_key_check_update);

        if (key == clearCache){
            //clear cache
            biz.clearCache();
            Toast.makeText(getActivity(),getResources().getText(R.string.delete_success),Toast.LENGTH_LONG).show();
            return true;
        }else if (key == postback){
            FeedbackAgent agent = new FeedbackAgent(getActivity());
            agent.setWelcomeInfo(getActivity().getResources().getString(R.string.welcome_string));
            agent.startFeedbackActivity();
            return true;
        }else if (key == checkUpdate){
            UmengUpdateAgent.forceUpdate(getActivity());
            return true;
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);

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
