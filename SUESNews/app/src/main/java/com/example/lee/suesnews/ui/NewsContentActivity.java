package com.example.lee.suesnews.ui;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.extras.toolbar.MaterialMenuIconToolbar;
import com.example.lee.suesnews.R;
import com.example.lee.suesnews.bean.NewsContent;
import com.example.lee.suesnews.bean.NewsItem;
import com.example.lee.suesnews.biz.NewsItemBiz;
import com.example.lee.suesnews.ui.widget.GestureFrameLayout;
import com.example.lee.suesnews.ui.widget.ObservableScrollView;
import com.example.lee.suesnews.utils.HttpUtils;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.nineoldandroids.view.ViewHelper;

import java.util.List;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class NewsContentActivity extends BaseActivity implements ObservableScrollView.OnScrollChangedListener {

    private static final float MAX_TEXT_SCALE_DELTA = 0.3f;
    private static final boolean TOOLBAR_IS_STICKY = true;

    private final int CURRENT_VERSION = Build.VERSION.SDK_INT;
    private final int DP_TRANS_X = 24;      //标题左移的距离

    private final int VERSION_KITKAT = Build.VERSION_CODES.KITKAT;

    private MaterialMenuIconToolbar mMaterialMenu;
    private NewsContent mNewsContent;
    private String mNewsContentUrl;

    private View mImageView;
    private View mOverlayView;
    private ObservableScrollView mScrollView;

    private TextView mTitleTextView;        //文章标题
    private TextView mContextTextView;      //文章内容
    private TextView mTitleDateTextView;      //文章日期

    private int mActionBarSize;
    private int mFlexibleSpaceImageHeight;

    private int mToolbarColor;

    private GestureFrameLayout gestureFrameLayout;  //滑动返回

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
        mToolbar.setTitle("");
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

        mImageView = findViewById(R.id.title_image);
        mOverlayView = findViewById(R.id.overlay);
        mTitleTextView = (TextView) findViewById(R.id.title_text_view);
        mContextTextView = (TextView) findViewById(R.id.content_text_view);
        mTitleDateTextView = (TextView) findViewById(R.id.title_date);
        mTitleTextView.setHorizontallyScrolling(true);

        mToolbarColor = getResources().getColor(R.color.primary_color);

        mScrollView = (ObservableScrollView) findViewById(R.id.scrollContent);
        mScrollView.setOnScrollListener(this);



        mActionBarSize = getActionBarSize();
        mFlexibleSpaceImageHeight = getResources().getDimensionPixelSize(R.dimen.flexible_space_image_height);

        gestureFrameLayout = (GestureFrameLayout) findViewById(R.id.news_content_gesture_layout);
        gestureFrameLayout.attachToActivity(this);

        //因为顶栏透明，要让出顶栏和底栏空间
        if (CURRENT_VERSION >= VERSION_KITKAT) {
            gestureFrameLayout.setPadding(0, getStatusBarHeight(), 0, getNavigationBarHeight());
        }
        ScrollUtils.addOnGlobalLayoutListener(mScrollView, new Runnable() {
            @Override
            public void run() {
                mScrollView.onScrollChanged(0, 0,0,0);
            }
        });
    }


    @Override
    public void onScrollChanged(int scrollX, int scrollY, int oldX, int oldY) {
        // Translate overlay and image
        float flexibleRange = mFlexibleSpaceImageHeight - mActionBarSize;
        int minOverlayTransitionY = mActionBarSize - mOverlayView.getHeight();
        ViewHelper.setTranslationY(mOverlayView, ScrollUtils.getFloat(-scrollY, minOverlayTransitionY, 0));
        ViewHelper.setTranslationY(mImageView, ScrollUtils.getFloat(-scrollY / 2, minOverlayTransitionY, 0));

        // Change alpha of overlay
        ViewHelper.setAlpha(mOverlayView, ScrollUtils.getFloat((float) scrollY / flexibleRange, 0, 1));
        ViewHelper.setAlpha(mTitleDateTextView, 1 - ScrollUtils.getFloat((float) scrollY / flexibleRange, 0, 1));

        // Scale title text
        float scale = 1 + ScrollUtils.getFloat((flexibleRange - scrollY) / flexibleRange, 0, MAX_TEXT_SCALE_DELTA);
        ViewHelper.setPivotX(mTitleTextView, 0);
        ViewHelper.setPivotY(mTitleTextView, 0);
        ViewHelper.setScaleX(mTitleTextView, scale);
        ViewHelper.setScaleY(mTitleTextView, scale);

        // Translate title text
        float anim = ScrollUtils.getFloat((float) scrollY/flexibleRange,0,1);       //1-0
        int maxTitleTranslationY = (int) (mFlexibleSpaceImageHeight - mTitleTextView.getHeight() * scale);
        int maxTitleTranslationX = dp2px(DP_TRANS_X);
        int titleTranslationY = (int) (maxTitleTranslationY - scrollY  - dp2px(12) * (1 - anim));
        int titleTranslationX = (int) (maxTitleTranslationX *(1 - anim));
        if (TOOLBAR_IS_STICKY) {
            titleTranslationY = Math.max(0, titleTranslationY);
        }
        ViewHelper.setTranslationY(mTitleTextView, titleTranslationY );
        ViewHelper.setTranslationX(mTitleTextView, -titleTranslationX);
        ViewHelper.setTranslationY(mTitleDateTextView, titleTranslationY );
        ViewHelper.setTranslationX(mTitleDateTextView, titleTranslationX );

        if (TOOLBAR_IS_STICKY) {
            // Change alpha of toolbar background
            if (-scrollY + mFlexibleSpaceImageHeight <= mActionBarSize) {
                mToolbar.setBackgroundColor(ScrollUtils.getColorWithAlpha(1, mToolbarColor));
            } else {
                mToolbar.setBackgroundColor(ScrollUtils.getColorWithAlpha(0, mToolbarColor));
            }
        } else {
            // Translate Toolbar
            if (scrollY < mFlexibleSpaceImageHeight) {
                ViewHelper.setTranslationY(mToolbar, 0);
            } else {
                ViewHelper.setTranslationY(mToolbar, -scrollY);
            }
        }
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
            showShare(this,mNewsContent.getTitle() + " 分享自SUESNews");
            return true;
        }

        return super.onOptionsItemSelected(item);
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
