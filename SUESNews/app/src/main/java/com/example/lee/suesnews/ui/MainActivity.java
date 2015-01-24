package com.example.lee.suesnews.ui;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.lee.slidemenu_library.view.SlideMenu;
import com.example.lee.suesnews.R;
import com.example.lee.suesnews.bean.NewsItem;
import com.example.lee.suesnews.biz.NewsItemBiz;
import com.example.lee.suesnews.common.NewsTypes;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {

    private ListView listView;
    private ListView mMenuContentListView;   //菜单项列表
    private SlideMenu mSlideMenu;            //
    private ViewGroup mMenu;                 //菜单栏
    private TextView mMenuSetting;          //菜单栏的设置按钮

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //移除顶栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);


        init();

        //listView = (ListView) findViewById(R.id.listTest);
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
        //th.start();

        //mMenuContentListView.setAdapter(getMenuItemAdapter());


        String[] strs = new String[]{"a","b","c"};
        mMenuContentListView.setAdapter(
                new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,strs));

    }

    /**
     * 初始化控件
     */
    private void init() {
        mMenu = (ViewGroup) findViewById(R.id.menu);
        mMenuContentListView = (ListView) mMenu.findViewById(R.id.menu_content);

    }

    private ListAdapter getMenuItemAdapter(){
        String[] dataList = new String[4];
        dataList[0] = "菜单1";
        dataList[1] = "菜单2";
        dataList[2] = "菜单3";
        dataList[3] = "菜单4";
        ArrayAdapter adapter =
                new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,dataList);

        return adapter;
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
