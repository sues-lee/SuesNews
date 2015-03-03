package com.example.lee.suesnews.ui.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.Scroller;

import com.example.lee.suesnews.R;

import java.util.LinkedList;
import java.util.List;

/**
 * 增加滑动返回手势的FrameLayout
 * Created by Administrator on 2015/2/20.
 */
public class GestureFrameLayout extends FrameLayout  {

    private static final String TAG = GestureFrameLayout.class.getSimpleName();
    private View mContentView;
    private int mTouchSlop;
    private int downX;
    private int downY;
    private int tempX;
    private Scroller mScroller;
    private int viewWidth;
    private boolean isSilding;
    private boolean isFinish;
    private Drawable mShadowDrawable;
    private Activity mActivity;
    private GestureDetector mGesture;    //判断左右滑动手势
    private List<ViewPager> mViewPagers = new LinkedList<ViewPager>();

    public GestureFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GestureFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mScroller = new Scroller(context);

        mShadowDrawable = getResources().getDrawable(R.drawable.shadow_left);
        mGesture = new GestureDetector(this.getContext(),onGestureListener);
    }

    /**
     * 设置手势监听器监听左右滑动手势
     */
    private GestureDetector.OnGestureListener onGestureListener=
            new GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    float startX = e1.getX();
                    float endX = e2.getX();
                    float x = endX - startX;
//                    if (endX > startX ){
//                        return false;
//                    }
                    if( x < 0 ){
                        scrollRight();
                        isFinish = true;
                    }
                    return true;
                }
            };


    /**
     * 与Activity绑定
     * @param activity 要绑定的activity
     */
    public void attachToActivity(Activity activity) {
        mActivity = activity;

        mContentView = (View) this.getParent();
//        TypedArray a = activity.getTheme().obtainStyledAttributes(
//                new int[] { android.R.attr.windowBackground });
//        int background = a.getResourceId(0, 0);
//        a.recycle();
//
//        ViewGroup decor = (ViewGroup) activity.getWindow().getDecorView();
//        ViewGroup decorChild = (ViewGroup) decor.getChildAt(0);
//        decorChild.setBackgroundResource(background);
//        decor.removeView(decorChild);
//        addView(decorChild);
//        setContentView(decorChild);
//        decor.addView(this);
    }

    private void setContentView(View decorChild) {
        mContentView = (View) decorChild.getParent();
    }

    /**
     * 事件拦截操作
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        //处理ViewPager冲突问题
        ViewPager mViewPager = getTouchViewPager(mViewPagers, ev);
        Log.i(TAG, "mViewPager = " + mViewPager);

        if(mViewPager != null && mViewPager.getCurrentItem() != 0){
            return super.onInterceptTouchEvent(ev);
        }

        //如果是左右滑动事件，则不将事件传递给子view
        if(mGesture.onTouchEvent(ev)){
            return true;
        }

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = tempX = (int) ev.getRawX();
                downY = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) ev.getRawX();
                // 满足此条件屏蔽SildingFinishLayout里面子类的touch事件
                if (moveX - downX > mTouchSlop
                        && Math.abs((int) ev.getRawY() - downY) < mTouchSlop) {
                    return true;
                }
                break;
        }

        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(mGesture.onTouchEvent(event)){
            return true;
        }
        switch (event.getAction()) {

            case MotionEvent.ACTION_MOVE:
                int moveX = (int) event.getRawX();
                int deltaX = tempX - moveX;
                tempX = moveX;
                if (moveX - downX > mTouchSlop
                        && Math.abs((int) event.getRawY() - downY) < mTouchSlop) {
                    isSilding = true;
                }

                if (moveX - downX >= 0 && isSilding) {
                    mContentView.scrollBy(deltaX, 0);
                }
                break;
            case MotionEvent.ACTION_UP:
                isSilding = false;
                if (mContentView.getScrollX() <= -viewWidth / 2) {
                    isFinish = true;
                    scrollRight();
                } else {
                    scrollOrigin();
                    isFinish = false;
                }
                break;
        }

        return true;
    }

    /**
     * 获取SwipeBackLayout里面的ViewPager的集合
     * @param mViewPagers
     * @param parent
     */
    private void getAlLViewPager(List<ViewPager> mViewPagers, ViewGroup parent){
        int childCount = parent.getChildCount();
        for(int i=0; i<childCount; i++){
            View child = parent.getChildAt(i);
            if(child instanceof ViewPager){
                mViewPagers.add((ViewPager)child);
            }else if(child instanceof ViewGroup){
                getAlLViewPager(mViewPagers, (ViewGroup)child);
            }
        }
    }


    /**
     * 返回我们touch的ViewPager
     * @param mViewPagers
     * @param ev
     * @return
     */
    private ViewPager getTouchViewPager(List<ViewPager> mViewPagers, MotionEvent ev){
        if(mViewPagers == null || mViewPagers.size() == 0){
            return null;
        }
        Rect mRect = new Rect();
        for(ViewPager v : mViewPagers){
            v.getHitRect(mRect);

            if(mRect.contains((int)ev.getX(), (int)ev.getY())){
                return v;
            }
        }
        return null;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            viewWidth = this.getWidth();

            getAlLViewPager(mViewPagers, this);
            Log.i(TAG, "ViewPager size = " + mViewPagers.size());
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (mShadowDrawable != null && mContentView != null) {

            int left = mContentView.getLeft()
                    - mShadowDrawable.getIntrinsicWidth();
            int right = left + mShadowDrawable.getIntrinsicWidth();
            int top = mContentView.getTop();
            int bottom = mContentView.getBottom();

            mShadowDrawable.setBounds(left, top, right, bottom);
            mShadowDrawable.draw(canvas);
        }

    }


    /**
     * 滚动出界面
     */
    private void scrollRight() {
        final int delta = (viewWidth + mContentView.getScrollX());
        // 调用startScroll方法来设置一些滚动的参数，我们在computeScroll()方法中调用scrollTo来滚动item
        mScroller.startScroll(mContentView.getScrollX(), 0, -delta + 1, 0,
                Math.abs(delta));
        postInvalidate();
    }

    /**
     * 滚动到起始位置
     */
    private void scrollOrigin() {
        int delta = mContentView.getScrollX();
        mScroller.startScroll(mContentView.getScrollX(), 0, -delta, 0,
                Math.abs(delta));
        postInvalidate();
    }

    @Override
    public void computeScroll() {
        // 调用startScroll的时候scroller.computeScrollOffset()返回true，
        if (mScroller.computeScrollOffset()) {
            mContentView.scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();

            if (mScroller.isFinished() && isFinish) {
                mActivity.finish();
            }
        }
    }
}
