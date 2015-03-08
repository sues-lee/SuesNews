package com.example.lee.suesnews.ui;

import android.annotation.TargetApi;
import android.app.FragmentManager;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.ViewConfiguration;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.example.lee.suesnews.R;
import com.example.lee.suesnews.biz.NewsItemBiz;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;


/**
 *基础Activity
 */
public class BaseActivity extends ActionBarActivity {

    protected Toolbar mToolbar;
    SystemBarTintManager mTintManager;
//    protected NewsItemBiz mNewsItemBiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        mNewsItemBiz = new NewsItemBiz(this);

        //使用tintManager设置状态栏的颜色
        mTintManager= new SystemBarTintManager(this);
        // enable status bar tint
        mTintManager.setStatusBarTintEnabled(true);
        // enable navigation bar tint
        if (isHasNavigationBar()) {
            mTintManager.setNavigationBarTintEnabled(true);
        }
        // set a custom tint color for all system bars
        mTintManager.setTintColor(getResources().getColor(R.color.dark_primary_color));

//        SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
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
        overridePendingTransition(R.anim.no_anim,R.anim.activity_slide_out);
//        );
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

    /**
     * 检查是否有虚拟按键
     * @return
     */
    protected boolean isHasNavigationBar(){
        //通过是否有物理按键来确定是否有虚拟按键
        boolean hasMenuKey = ViewConfiguration.get(this).hasPermanentMenuKey();
        boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
        return !(hasBackKey && hasMenuKey);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    protected void replaceFragment(int viewId, android.app.Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(viewId, fragment).commit();
    }

    protected int getActionBarSize() {
        TypedValue typedValue = new TypedValue();
        int[] textSizeAttr = new int[]{R.attr.actionBarSize};
        int indexOfAttrTextSize = 0;
        TypedArray a = obtainStyledAttributes(typedValue.data, textSizeAttr);
        int actionBarSize = a.getDimensionPixelSize(indexOfAttrTextSize, -1);
        a.recycle();
        return actionBarSize;
    }

    /**
     * 分享
     */
    protected void showShare(Context context,String text) {
        ShareSDK.initSDK(this);

        String appHomePage = getString(R.string.app_home_page);
        String shareText = text != "" ? text : appHomePage;

        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // 分享时Notification的图标和文字
        oks.setNotification(R.drawable.ibooks, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(getString(R.string.share));
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl(appHomePage);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(shareText);
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImagePath(Environment.getExternalStorageDirectory().getPath()
                + "/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(appHomePage);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(appHomePage);

        // 启动分享GUI
        oks.show(this);
    }

}
