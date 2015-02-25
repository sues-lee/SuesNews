package com.example.lee.suesnews.ui.fragments;


import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.lee.suesnews.R;
import com.example.lee.suesnews.bean.NewsContent;
import com.example.lee.suesnews.bean.NewsItem;
import com.example.lee.suesnews.biz.NewsItemBiz;
import com.example.lee.suesnews.common.NewsTypes;
import com.example.lee.suesnews.dao.NewsItemDao;
import com.example.lee.suesnews.ui.MyRecyclerAdapter;
import com.example.lee.suesnews.ui.NewsContentActivity;
import com.example.lee.suesnews.ui.RecyclerItemClickListener;
import com.example.lee.suesnews.utils.HttpUtils;
import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.MaterialHeader;

/**
 * A simple {@link android.app.Fragment} subclass.
 */
public class NewsListFragment extends BaseFragment {

    private static final String ARG_DATA_LIST = "datalist";
    private static final String ARG_NEWS_TYPE = "newsType";


    //新闻类型
    private int mNewsType;

    private ObservableRecyclerView mRecyclerView;
    private MyRecyclerAdapter mAdapter;
    private ObservableRecyclerView.LayoutManager mLayoutManager;

    //当前页码
    private int mCurrentPage;

    private PtrFrameLayout frame;
    private MaterialHeader header;

    //缓存
    private List<NewsItem> mNewsItems = new ArrayList<NewsItem>();
//    //数据库访问
//    private NewsItemDao mNewsItemDao;

    public NewsListFragment() {
        // Required empty public constructor
    }

    public static NewsListFragment newInstance(int newsType) {
        NewsListFragment fragment = new NewsListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_NEWS_TYPE, newsType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news_list, container, false);
        init(view);

        return view;
    }

    private void init(View view) {
        Activity parentActivity = getActivity();

        frame = (PtrFrameLayout) view.findViewById(R.id.ptr_frame);
        header = new MaterialHeader(parentActivity.getBaseContext());

        header.setPadding(0,20,0,20);
        header.setPtrFrameLayout(frame);


        frame.setLoadingMinTime(1000);
        frame.setDurationToCloseHeader(300);
        frame.setHeaderView(header);
        frame.addPtrUIHandler(header);

        frame.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout ptrFrameLayout, View view, View view2) {
                return mRecyclerView.getCurrentScrollY() == 0;
            }

            @Override
            public void onRefreshBegin(final PtrFrameLayout ptrFrameLayout) {
                getNewsList(mAdapter, mCurrentPage, true);
                ptrFrameLayout.refreshComplete();
            }
        });


        mRecyclerView = (ObservableRecyclerView) view.findViewById(R.id.my_recycler_view);

        mLayoutManager = new LinearLayoutManager(parentActivity);

        mRecyclerView.setScrollViewCallbacks((ObservableScrollViewCallbacks)parentActivity);

        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(),new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        NewsItem item = mAdapter.getmNewsList().get(position);

                        //打开显示新闻内容的Activity,把新闻的url作为参数传过去
                        Intent startActivityIntent = new Intent(getActivity(),NewsContentActivity.class);
                        Bundle urlBundle = new Bundle();
                        urlBundle.putString("url",item.getUrl());
                        startActivityIntent.putExtra("key",urlBundle);
                        startActivity(startActivityIntent);
                        getActivity().overridePendingTransition(R.anim.activity_fade_in,
                                R.anim.no_anim);

                    }
                })
            );
        //设置adapter
        mAdapter = new MyRecyclerAdapter();
        mRecyclerView.setAdapter(mAdapter);

        //得到数据
        getNewsList(mAdapter, mCurrentPage, false);

        //监听list滑动事件
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //TODO:添加加载新项目的方法
//                int lastPos = mLayoutManager.getItemCount();
//                Log.i("SCROLL","dx:"+dx+"dy:"+dy);
            }
        });
}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mNewsType = getArguments().getInt(ARG_NEWS_TYPE);
        }
        mCurrentPage = 0;

    }



    /**
     * 获取某一页的数据
     * @param adapter
     * @param currentPage 页码
     * @param forced      是否强制刷新
     */
    private void getNewsList(MyRecyclerAdapter adapter,int currentPage,boolean forced) {
        int total = mNewsItems.size();
        //不强制刷新时，如果此页已存在则直接从内存中加载
        if (!forced && total>0 &&
                (mNewsItems.get(total-1).getPageNumber() >= currentPage) ){
            mAdapter.addNews(mNewsItems);
            mAdapter.notifyDataSetChanged();
            return;
        }

        Log.i("LIXU","size"+total);
        //TODO:在刷新出新的新闻时应增量刷新
        if(forced && mNewsItems.size()>0){
            mNewsItems.clear();
            Log.i("LIXU","清空"+total);
        }
        LoadNewsListTask loadDataTask = new LoadNewsListTask(adapter,mNewsType,forced);
        loadDataTask.execute(currentPage);
    }



    /**
     * 加载新闻列表的任务
     *
     */
    class LoadNewsListTask extends AsyncTask<Integer, Integer,List<NewsItem> >{

        private MyRecyclerAdapter mAdapter;
        private boolean mIsForced;
        private int mNewsType;

        public LoadNewsListTask(MyRecyclerAdapter adapter,int newsType,boolean forced) {
            super();
            mAdapter = adapter;
            mIsForced = forced;
            mNewsType = newsType;
        }

        /**
         *得到当前页码的新闻列表
         * @param currentPage 当前页码
         * @return 当前页码的新闻列表,出错返回null
         */
        @Override
        protected List<NewsItem> doInBackground(Integer... currentPage) {

            try {
                 boolean netAvailable = HttpUtils.IsNetAvailable(getActivity());
                 return mNewsItemBiz.getNewsItems(mNewsType, currentPage[0],netAvailable);

            } catch (Exception e) {
                e.printStackTrace();
                Log.i("ASDNET","neterror :"+e);
                return null;
            }

        }

        /**
         * 得到新闻列表后将其加载
         * @param newsItems 得到的新闻列表
         */
        @Override
        protected void onPostExecute(List<NewsItem> newsItems) {
            if (newsItems == null) {
                //TODO:字符串转换为资源
                Toast.makeText(getActivity(),"刷新失败，请检查网络后重试",Toast.LENGTH_LONG).show();
                return;
            }
            //TODO:处理强制刷新
            if(mIsForced){
                mAdapter.getmNewsList().clear();
            }
            mNewsItems.addAll(newsItems);

            mAdapter.addNews(newsItems);
            mAdapter.notifyDataSetChanged();
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
