package com.example.lee.suesnews.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.example.lee.suesnews.R;

/**
 * RecyclerView Item点击监听器
 * Created by Administrator on 2015/2/15.
 */
public class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {

    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        public void onItemClick(View view,int position);
    }

    GestureDetector mGestureDetector;

    public RecyclerItemClickListener(Context context,OnItemClickListener listener){
        mListener = listener;
        mGestureDetector = new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){
            //判断手势为点击
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent event) {
        View item = recyclerView.findChildViewUnder(event.getX(),event.getY());
        if (item != null && mListener != null && mGestureDetector.onTouchEvent(event)){
            int position = recyclerView.getChildPosition(item);
            mListener.onItemClick(item,position);
            return true;
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView recyclerView, MotionEvent event) {
//        View item = recyclerView.findChildViewUnder(event.getX(),event.getY());
//        switch (event.getAction()){
//            case MotionEvent.ACTION_DOWN:
//                item.setBackground(recyclerView.getContext().getResources().getDrawable(R.color.text_color));
//                break;
//        }

    }
}
