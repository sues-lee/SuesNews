package com.example.lee.suesnews.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.lee.suesnews.R;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.Random;

public class WelcomeActivity extends Activity {

    ImageView mBackgroundImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        View decorView = getWindow().getDecorView();
        int uiOptions = decorView.getSystemUiVisibility();
        int newUiOptions = uiOptions;
        //newUiOptions |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        //TODO:隐藏导航栏
        decorView.setSystemUiVisibility(newUiOptions);

        mBackgroundImage = (ImageView) findViewById(R.id.image_background);
        Random random = new Random();
        int num = random.nextInt(4);
        int drawable = R.drawable.pic_background_1;
        switch (num ){
            case 0:
                break;
            case 1:
                drawable = R.drawable.pic_background_2;
                break;
            case 2:
                drawable = R.drawable.pic_background_3;
                break;
            case 3:
                drawable = R.drawable.pic_background_4;
                break;
        }
        mBackgroundImage.setImageDrawable(getResources().getDrawable(drawable));
        Animation animImage = AnimationUtils.loadAnimation(this,R.anim.image_welcome);
        mBackgroundImage.startAnimation(animImage);
        animImage.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //动画结束时打开首页
                startActivity(new Intent(WelcomeActivity.this,MainActivity.class));
                //TODO:
                overridePendingTransition(R.anim.no_anim, R.anim.activity_slide_out);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    public void finish() {
        mBackgroundImage.destroyDrawingCache();
        super.finish();
    }
}
