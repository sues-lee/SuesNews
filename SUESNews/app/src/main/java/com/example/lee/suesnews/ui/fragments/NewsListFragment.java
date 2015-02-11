package com.example.lee.suesnews.ui.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lee.suesnews.R;
import com.example.lee.suesnews.ui.MyRecyclerAdapter;
import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.MaterialHeader;

/**
 * A simple {@link android.app.Fragment} subclass.
 */
public class NewsListFragment extends BaseFragment {

    private static final String ARG_DATA_LIST = "datalist";
    private static final String ARG_NEWS_TYPE = "newsType";

    public String getNewsType() {
        if (mNewsType != null)
            return mNewsType;
        else
            return "TITLE";
    }

    //新闻类型
    private String mNewsType;

    private ObservableRecyclerView mRecyclerView;
    private ObservableRecyclerView.Adapter mAdapter;
    private ObservableRecyclerView.LayoutManager mLayoutManager;
    private String[] mDummyDataList;

    private PtrFrameLayout frame;
    private MaterialHeader header;

    public NewsListFragment() {
        // Required empty public constructor
    }

    public static NewsListFragment newInstance(String newsType) {
        NewsListFragment fragment = new NewsListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NEWS_TYPE, newsType);
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
                ptrFrameLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ptrFrameLayout.refreshComplete();
                    }
                },2000);
            }
        });


        mRecyclerView = (ObservableRecyclerView) view.findViewById(R.id.my_recycler_view);

        mLayoutManager = new LinearLayoutManager(parentActivity);

        mRecyclerView.setScrollViewCallbacks((ObservableScrollViewCallbacks)parentActivity);

        mRecyclerView.setLayoutManager(mLayoutManager);

        //设置adapter
        mAdapter = new MyRecyclerAdapter(mDummyDataList);
        mRecyclerView.setAdapter(mAdapter);
}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mNewsType = getArguments().getString(ARG_NEWS_TYPE);
        }

        //得到数据
        getDummyData();
    }

    /**
     * 根据类型得到新闻数据
     */
    private void getDummyData(){
        mDummyDataList = new String[]{mNewsType,"A","B","C","D","A","B","C","D","A","B","C","D","A","B","C","D"};
    }

}
