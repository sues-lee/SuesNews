package com.example.lee.suesnews.ui;

import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.example.lee.suesnews.R;
import com.readystatesoftware.systembartint.SystemBarTintManager;


/**
 *
 */
public class BaseActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //使用tintManager设置状态栏的颜色
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        // enable status bar tint
        tintManager.setStatusBarTintEnabled(true);
        // enable navigation bar tint
        tintManager.setNavigationBarTintEnabled(true);
        // set a custom tint color for all system bars
        tintManager.setTintColor(getResources().getColor(R.color.dark_primary_color));

        SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
    }

    /**
     * 根据int得出对应的图标状态
     * @param state
     * @return 图标状态，默认为汉堡型
     */
   protected MaterialMenuDrawable.IconState intToState(int state){
       switch (state){
           case 0:
               return MaterialMenuDrawable.IconState.BURGER;
           case 1:
               return MaterialMenuDrawable.IconState.ARROW;
           case 2:
               return MaterialMenuDrawable.IconState.X;
           case 3:
               return MaterialMenuDrawable.IconState.CHECK;
       }

       return MaterialMenuDrawable.IconState.BURGER;
   }

    /**
     * 获得当前系统版本号
     * @return
     */
   protected int getVersionNumber(){
       return Build.VERSION.SDK_INT;
   }

    protected static int dp2px(float dpValue){
        return (int)(dpValue * Resources.getSystem().getDisplayMetrics().density);
    }

    protected static int px2dp(float pxValue){
        return (int)(pxValue / Resources.getSystem().getDisplayMetrics().density);
    }

    protected int getScreenWidth(){
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    protected int getScreenHeight(){
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.no_anim,R.anim.activity_fade_out
        );
    }


    /**
     * 获取状态栏的高度
     * @return
     */
    protected int getStatusBarHeight(){
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height","dimen","android");
        if (resourceId>0){
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 获取navigation bar的高度
     * @return
     */
    protected int getNavigationBarHeight(){
        int result = 0;
        int resourceId = getResources().getIdentifier("navigation_bar_height","dimen","android");
        if (resourceId>0){
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
