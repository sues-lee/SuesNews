package com.example.lee.suesnews.ui;


import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.extras.toolbar.MaterialMenuIconToolbar;

import com.example.lee.suesnews.R;
import com.example.lee.suesnews.bean.NewsItem;
import com.example.lee.suesnews.biz.NewsItemBiz;
import com.example.lee.suesnews.common.NewsTypes;
import com.example.lee.suesnews.ui.fragments.NewsListFragment;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.ArrayList;
import java.util.List;

import static android.support.v7.widget.RecyclerView.*;


public class MainActivity extends BaseActivity implements ObservableScrollViewCallbacks {

    private ViewPager mViewPager;
    //新闻列表
    private List<NewsListFragment> mFragmentList;
    private Toolbar mToolbar;
    private ViewGroup mMainPage;
    private DrawerLayout mDrawerLayout;
    private ViewGroup mDrawer;

    private MaterialMenuIconToolbar mMaterialMenu;

    PagerSlidingTabStrip mTabs;

    private ViewGroup mContent;

    private final int CURRENT_VERSION = Build.VERSION.SDK_INT;

    private final int VERSION_KITKAT = Build.VERSION_CODES.KITKAT;

    //侧边栏头部图片
    private ImageView mHeaderImage;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        initViewPager();

    }

    private void initViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar2);
        mToolbar.setTitle("My Title");
        setSupportActionBar(mToolbar);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(Gravity.LEFT);
            }
        });


        mMaterialMenu = new MaterialMenuIconToolbar(this, Color.WHITE, MaterialMenuDrawable.Stroke.THIN) {
            @Override
            public int getToolbarViewId() {
                return R.id.toolbar2;
            }
        };
        mMaterialMenu.setNeverDrawTouch(true);

        //设置侧滑菜单的监听
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View view, float v) {
                mMaterialMenu.setTransformationOffset(MaterialMenuDrawable.AnimationState.BURGER_ARROW,
                        v);
            }

            @Override
            public void onDrawerOpened(View view) {
                mMaterialMenu.animatePressedState(intToState(1));
            }

            @Override
            public void onDrawerClosed(View view) {
                mMaterialMenu.animatePressedState(intToState(0));
            }

            @Override
            public void onDrawerStateChanged(int i) {

            }
        });

        //使用tintManager设置状态栏的颜色
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        // enable status bar tint
        tintManager.setStatusBarTintEnabled(true);
        // enable navigation bar tint
        tintManager.setNavigationBarTintEnabled(true);
        // set a custom tint color for all system bars
        tintManager.setTintColor(getResources().getColor(R.color.dark_primary_color));

        SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();


        mContent = (ViewGroup) findViewById(R.id.content);
        mDrawer = (ViewGroup) findViewById(R.id.drawer);
        mMainPage = (ViewGroup) findViewById(R.id.main_page);
        //因为顶栏透明，要让出顶栏和底栏空间
        if (CURRENT_VERSION >= VERSION_KITKAT) {
            mMainPage.setPadding(0, config.getStatusBarHeight(), 0, config.getNavigationBarHeight());
            mDrawer.setPadding(0, config.getStatusBarHeight(), 0, config.getNavigationBarHeight());
        }

        //侧边栏
        mHeaderImage = (ImageView) findViewById(R.id.header_img);
        mHeaderImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //resume the click
            }
        });
    }

    private void initViewPager(){

        mTabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mFragmentList = new ArrayList<NewsListFragment>();

        //初始化fragment
        NewsListFragment fragment1 = NewsListFragment.newInstance(NewsTypes.NEWS_TPYE_XXYW);
        NewsListFragment fragment2 = NewsListFragment.newInstance(NewsTypes.NEWS_TPYE_XYKX);
        NewsListFragment fragment3 = NewsListFragment.newInstance(NewsTypes.NEWS_TPYE_KJDT);
        NewsListFragment fragment4 = NewsListFragment.newInstance(NewsTypes.NEWS_TPYE_MTJJ);
        NewsListFragment fragment5 = NewsListFragment.newInstance(NewsTypes.NEWS_TPYE_BMXW);

        mFragmentList.add(fragment1);
        mFragmentList.add(fragment2);
        mFragmentList.add(fragment3);
        mFragmentList.add(fragment4);
        mFragmentList.add(fragment5);




        //初始化ViewPager
        MyViewPagerAdapter adapter = new MyViewPagerAdapter(getSupportFragmentManager(),mFragmentList);
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(0);

        mTabs.setViewPager(mViewPager);
        mTabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {

            }

            @Override
            public void onPageSelected(int i) {
//                mViewPager.setCurrentItem(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    @Override
    public void onScrollChanged(int i, boolean b, boolean b2) {

    }

    @Override
    public void onDownMotionEvent() {

    }

    /**
     * 根据滑动方向设置ToolBar的显隐
     * @param scrollState   滑动方向
     */
    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        if (scrollState == ScrollState.UP){
            if (toolbarIsShown()){
                hideToolbar();
            }
        } else if (scrollState == ScrollState.DOWN){
            if (toolbarIsHidden()){
                showToolbar();
            }
        }
    }


    private boolean toolbarIsShown(){
        return mToolbar.getTranslationY() == 0;
    }

    private boolean toolbarIsHidden(){
        return mToolbar.getTranslationY() == -mToolbar.getHeight();
    }

    private void showToolbar(){
        moveToolbar(0);
    }


    private void hideToolbar(){
        moveToolbar(-mToolbar.getHeight());
    }


    /**
     * 将toolbar移动到某个位置
     * @param toTranslationY 移动到的Y轴位置
     */
    private void moveToolbar(float toTranslationY){
        if(mToolbar.getTranslationY() == toTranslationY){
            return;
        }
        //利用动画过渡移动的过程
        final ValueAnimator animator = ValueAnimator.ofFloat(mToolbar.getTranslationY(),toTranslationY).
                setDuration(200);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float translationY = (Float) animator.getAnimatedValue();
                mToolbar.setTranslationY(translationY);
                mContent.setTranslationY(translationY);
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mContent.getLayoutParams();
                lp.height = (int) (getScreenHeight() - translationY - getStatusBarHeight()
                        - lp.topMargin);
                if (CURRENT_VERSION >= VERSION_KITKAT){
                    lp.height -= getNavigationBarHeight();
                }
                Log.i("TEST", "after" + Float.toString(mToolbar.getHeight()));
                mContent.requestLayout();
            }
        });
        animator.start();
    }


    /**
     * 获取状态栏的高度
     * @return
     */
    private int getStatusBarHeight(){
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
    private int getNavigationBarHeight(){
        int result = 0;
        int resourceId = getResources().getIdentifier("navigation_bar_height","dimen","android");
        if (resourceId>0){
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

}
