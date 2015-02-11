package com.example.lee.suesnews.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


import com.example.lee.suesnews.ui.fragments.NewsListFragment;

import java.util.List;

/**
 *
 * Created by Administrator on 2015/2/7.
 */
public class MyViewPagerAdapter extends FragmentPagerAdapter {

    private final String[] TITLES = {"今日要闻","体育新闻","学院新闻","艺术新闻","快讯"};
    List<NewsListFragment> mList;
    public MyViewPagerAdapter(FragmentManager fm, List<NewsListFragment> list) {
        super(fm);
        mList = list;
    }

    @Override
    public Fragment getItem(int i) {
        return mList.get(i);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position];
    }
}
