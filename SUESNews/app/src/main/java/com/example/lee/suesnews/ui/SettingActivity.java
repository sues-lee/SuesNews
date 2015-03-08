package com.example.lee.suesnews.ui;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.extras.toolbar.MaterialMenuIconToolbar;
import com.example.lee.suesnews.R;
import com.example.lee.suesnews.ui.fragments.SettingFragment;
import com.example.lee.suesnews.ui.widget.GestureFrameLayout;

/**
 *
 * Created by Administrator on 2015/3/1.
 */
public class SettingActivity extends BaseActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private SettingFragment mSettingFragment;
    private GestureFrameLayout mGestureFrameLayout;  //滑动返回

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        mGestureFrameLayout = (GestureFrameLayout) findViewById(R.id.news_content_gesture_layout);
        mGestureFrameLayout.attachToActivity(this);

        mToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_black_18dp));
        //mToolbar.setTitle("设置");
        setSupportActionBar(mToolbar);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SettingActivity.this.finish();
            }
        });


        mSettingFragment = new SettingFragment();
        replaceFragment(R.id.setting_content,mSettingFragment);
    }


    /**
     * 保存更改的设置项
     * @param sharedPreferences
     * @param key
     */
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        sharedPreferences.getString(key,"");
    }
}
