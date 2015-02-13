package com.example.lee.suesnews.ui;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lee.suesnews.R;
import com.example.lee.suesnews.bean.NewsItem;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by Administrator on 2015/2/5.
 */

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder>{

    //当前显示的数据
    private List<NewsItem> mNewsList = new ArrayList<NewsItem>();

    public List<NewsItem> getmNewsList() {
        return mNewsList;
    }

    public MyRecyclerAdapter(){
    }


    public MyRecyclerAdapter(List<NewsItem> myDataset){
        mNewsList = myDataset;
    }

    /**
     * 添加新闻列表
     * @param news 要添加的新闻列表
     */
    public void addNews(List<NewsItem> news){
        mNewsList.addAll(news);
        Log.i("LIXU", "adapter" + mNewsList.size());
    }

    /**
     * 创建ViewHolder
     * @param viewGroup 父View
     * @param i 位置
     * @return 返回得到的ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recyclerview,
                viewGroup,false);
        ViewHolder holder = new ViewHolder(v);
        holder.mTitleTextView = (TextView) v.findViewById(R.id.titleTextView);
        holder.mDateTextView = (TextView) v.findViewById(R.id.dateTextView);
        return holder;
    }

    /**
     * 将数据绑定到ViewHolder
     * @param viewHolder    要绑定的ViewHolder
     * @param i             ViewHolder的位置
     */
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.mTitleTextView.setText(mNewsList.get(i).getTitle());
        //如果日期为空，则尝试使用新闻来源
        viewHolder.mDateTextView.setText(mNewsList.get(i).getDate() == null ?
                mNewsList.get(i).getSource():mNewsList.get(i).getDate());
    }

    @Override
    public int getItemCount() {
        return mNewsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView mTitleTextView;
        public TextView mDateTextView;
        public ViewHolder(View v){
            super(v);
        }
    }
}