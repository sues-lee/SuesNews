package com.example.lee.suesnews.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.extras.toolbar.MaterialMenuIconToolbar;
import com.example.lee.suesnews.R;
import com.example.lee.suesnews.ui.fragments.SettingFragment;

/**
 *
 * Created by Administrator on 2015/3/1.
 */
public class SettingActivity extends BaseActivity {

    private SettingFragment mSettingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("设置");
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SettingActivity.this.finish();
            }
        });
        
        MaterialMenuIconToolbar materialMenu = new MaterialMenuIconToolbar(this, Color.WHITE, MaterialMenuDrawable.Stroke.THIN) {
            @Override
            public int getToolbarViewId() {
                return R.id.toolbar;
            }
        };
        materialMenu.setState(intToState(1));

        mSettingFragment = new SettingFragment();
        replaceFragment(R.id.setting_content,mSettingFragment);
    }
}
