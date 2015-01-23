package com.example.lee.suesnews.ui;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.lee.suesnews.R;
import com.example.lee.suesnews.bean.NewsItem;
import com.example.lee.suesnews.biz.NewsItemBiz;
import com.example.lee.suesnews.common.NewsTypes;

import java.util.List;


public class MainActivity extends Activity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listTest);
        Runnable r = new Runnable() {
            @Override
            public void run() {
                NewsItemBiz biz = new NewsItemBiz();
                int currentPage = 1;
                try {
                    List<NewsItem> newsItems = biz.getNewsItems(NewsTypes.NEWS_TPYE_XXYW,currentPage);
                    for (NewsItem item : newsItems) {
                        Log.i("SSS", item.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }finally{
                    return;
                }
            }
        };
        Thread th = new Thread(r);
        th.start();


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
}
