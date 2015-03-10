package com.example.lee.suesnews.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.lee.suesnews.R;
import com.example.lee.suesnews.ui.widget.GestureFrameLayout;
import com.example.lee.suesnews.utils.CommonUtils;

public class AboutActivity extends BaseActivity {

    private Toolbar mToolbar;
    private GestureFrameLayout mGestureFrameLayout;  //滑动返回
    private TextView mVersionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_black_18dp));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AboutActivity.this.finish();
            }
        });
        setSupportActionBar(mToolbar);
        mGestureFrameLayout = (GestureFrameLayout) findViewById(R.id.about_gesture_layout);
        mGestureFrameLayout.attachToActivity(this);

        mVersionTextView = (TextView) findViewById(R.id.version);
        mVersionTextView.setText("版本：" + CommonUtils.getVersion(this));

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_about, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
