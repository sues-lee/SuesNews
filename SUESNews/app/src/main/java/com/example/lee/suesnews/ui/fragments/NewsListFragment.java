package com.example.lee.suesnews.ui.fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.lee.suesnews.R;
import com.example.lee.suesnews.bean.NewsItem;
import com.example.lee.suesnews.biz.NewsItemBiz;
import com.example.lee.suesnews.common.NewsTypes;
import com.example.lee.suesnews.ui.MyRecyclerAdapter;
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
                getData(mAdapter,mCurrentPage,true);
                ptrFrameLayout.refreshComplete();
            }
        });


        mRecyclerView = (ObservableRecyclerView) view.findViewById(R.id.my_recycler_view);

        mLayoutManager = new LinearLayoutManager(parentActivity);

        mRecyclerView.setScrollViewCallbacks((ObservableScrollViewCallbacks)parentActivity);

        mRecyclerView.setLayoutManager(mLayoutManager);

        //设置adapter
        mAdapter = new MyRecyclerAdapter();
        mRecyclerView.setAdapter(mAdapter);

        //得到数据
        getData(mAdapter,mCurrentPage,false);

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
        mCurrentPage = 1;

    }

    /**
     * 根据类型得到新闻数据
     */
    private void getDummyData(){

    }

    /**
     * 获取某一页的数据
     * @param adapter
     * @param currentPage 页码
     * @param forced      是否强制刷新
     */
    private void getData(MyRecyclerAdapter adapter,int currentPage,boolean forced) {
        int total = mNewsItems.size();
//        Log.i("LIXU","total"+total);
        //不强制刷新时，如果此页已存在则直接从内存中加载
        if (!forced && total>0 &&
                (mNewsItems.get(total-1).getPageNumber() >= currentPage) ){
//            Log.i("LIXU","没刷新");
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
        LoadDataTask loadDataTask = new LoadDataTask(adapter,mNewsType,forced);
        loadDataTask.execute(currentPage);
    }

    /**
     * 加载新闻列表的任务
     *
     */
    class LoadDataTask extends AsyncTask<Integer, Integer,List<NewsItem> >{

        private MyRecyclerAdapter mAdapter;
        private boolean mIsForced;
        private int mNewsType;

        public LoadDataTask(MyRecyclerAdapter adapter,int newsType,boolean forced) {
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
                return NewsItemBiz.getNewsItems(mNewsType,currentPage[0]);
            } catch (Exception e) {
                e.printStackTrace();
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
