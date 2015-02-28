package com.example.lee.suesnews.ui;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.extras.toolbar.MaterialMenuIconToolbar;
import com.example.lee.suesnews.R;
import com.example.lee.suesnews.bean.NewsContent;
import com.example.lee.suesnews.bean.NewsItem;
import com.example.lee.suesnews.biz.NewsItemBiz;
import com.example.lee.suesnews.utils.HttpUtils;

import java.util.List;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class NewsContentActivity extends BaseActivity {

    private MaterialMenuIconToolbar mMaterialMenu;
    private NewsContent mNewsContent;
    private String mNewsContentUrl;

    private TextView mTitleTextView;        //文章标题
    private TextView mContextTextView;      //文章内容
    private TextView mTitleDateTextView;      //文章日期

    private NewsItemBiz mNewsItemBiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_content);

        init();
        mNewsItemBiz = new NewsItemBiz(this);
        //通过bundle获取文章内容的url
        mNewsContentUrl = this.getIntent().getBundleExtra("key").getString("url");
        LoadNewsContentTask loadNewsContentTask = new LoadNewsContentTask();
        loadNewsContentTask.execute(mNewsContentUrl);
    }

    private void init() {
        mToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("新闻内容");
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewsContentActivity.this.finish();
            }
        });

        mMaterialMenu = new MaterialMenuIconToolbar(this, Color.WHITE, MaterialMenuDrawable.Stroke.THIN) {
            @Override
            public int getToolbarViewId() {
                return R.id.toolbar;
            }
        };

        mMaterialMenu.setState(intToState(1));


        mTitleTextView = (TextView) findViewById(R.id.title_text_view);
        mContextTextView = (TextView) findViewById(R.id.content_text_view);
        mTitleDateTextView = (TextView) findViewById(R.id.title_date);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_news_content, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_share) {
            showShare();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 分享
     */
    private void showShare() {
        ShareSDK.initSDK(this);

        String appHomePage = getString(R.string.app_home_page);

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
        oks.setText("内容分享测试: " + mNewsContent.getTitle());
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

    /**
     * 加载新闻内容的任务
     *
     */
    class LoadNewsContentTask extends AsyncTask<String, Integer,NewsContent > {


        public LoadNewsContentTask() {
            super();

        }

        /**
         *得到当前url的新闻内容
         * @param urls 当前url
         * @return 当前页码的新闻列表,出错返回null
         */
        @Override
        protected NewsContent doInBackground(String... urls) {

            try {
                return mNewsItemBiz.getNewsContent(urls[0]);
            } catch (Exception e) {
                e.printStackTrace();
                Log.i("ASD","Content错误： "+ e);
                return null;
            }

        }

        /**
         * 得到新闻内容后将其加载
         * @param newsContent 得到的新闻内容
         */
        @Override
        protected void onPostExecute(NewsContent newsContent) {
            if (newsContent == null) {
                //TODO:字符串转换为资源
                Toast.makeText(NewsContentActivity.this, "刷新失败，请检查网络后重试", Toast.LENGTH_LONG).show();
                return;
            }
            //TODO:处理信息缓存

            mNewsContent = newsContent;
            //将内容载入界面
            mTitleTextView.setText(newsContent.getTitle());
            mTitleDateTextView.setText(newsContent.getDate());
            mContextTextView.setText(newsContent.getFormatedContent());
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }
}
