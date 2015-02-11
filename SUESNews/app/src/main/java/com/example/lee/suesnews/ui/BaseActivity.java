package com.example.lee.suesnews.ui;

import android.content.res.Resources;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;

import com.balysv.materialmenu.MaterialMenuDrawable;


/**
 *
 */
public class BaseActivity extends ActionBarActivity {

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
}
