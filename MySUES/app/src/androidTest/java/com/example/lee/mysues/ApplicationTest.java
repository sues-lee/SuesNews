package com.example.lee.mysues;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

import com.example.lee.mysues.bean.NewsItem;
import com.example.lee.mysues.biz.NewsItemBiz;
import com.example.lee.mysues.common.NewsTypes;

import java.util.List;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    //TODO:need unit test

    public ApplicationTest() {
        super(Application.class);
        testHttp01();
    }
    public void testHttp01(){

        NewsItemBiz biz = new NewsItemBiz();
        int currentPage = 1;
        try {
            List<NewsItem> newsItems = biz.getNewsItems(NewsTypes.NEWS_TPYE_BMXW,currentPage);
            for (NewsItem item : newsItems) {
                Log.i("SSS", item.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}