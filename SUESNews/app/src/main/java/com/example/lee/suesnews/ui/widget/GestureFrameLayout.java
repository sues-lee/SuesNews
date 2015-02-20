package com.example.lee.suesnews.ui.widget;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * 增加滑动返回手势的FrameLayout
 * Created by Administrator on 2015/2/20.
 */
public class GestureFrameLayout extends FrameLayout {

    private GestureDetector mGesureDetector;

    public GestureFrameLayout(Context context) {
        super(context);

//        mGesureDetector = new GestureDetector(this,new GestureDetector.SimpleOnGestureListener() {
//
//            @Override
//            public boolean onFling(MotionEvent event, MotionEvent event2, float v, float v2) {
//                //监听右滑手势，在右滑时退出
//
//                float x = event2.getX() - event.getX();
//                //当向右滑距离大于屏幕1/3时，退出此页面
//                if (x > 0 && Math.abs(x) > getScreenWidth()/3){
//                    getContext().finish();
//                }
//                return true;
//            }
//        });
    }
}
