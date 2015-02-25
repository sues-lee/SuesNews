package com.example.lee.suesnews.ui.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.example.lee.suesnews.biz.NewsItemBiz;


public class BaseFragment extends Fragment {
    protected NewsItemBiz mNewsItemBiz;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNewsItemBiz = new NewsItemBiz(getActivity());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mNewsItemBiz != null){
            //TODO:关闭helper
        }
    }
}
