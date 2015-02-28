package com.example.lee.suesnews.dao;

import android.content.Context;

import com.example.lee.suesnews.bean.NewsContent;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.DeleteBuilder;

import java.sql.SQLException;
import java.util.List;

/**
 * 对新闻内容的数据库操作
 * Created by Administrator on 2015/2/24.
 */
public class NewsContentDao {
    private RuntimeExceptionDao<NewsContent,Integer> mNewsContentDao;
    private DataBaseHelper mDataBaseHelpter;

    public NewsContentDao(Context context) {
        mDataBaseHelpter = DataBaseHelper.getHelper(context);
        this.mNewsContentDao = mDataBaseHelpter.getNewsContentRuntimeDao();
    }

    /**
     * 更新或添加
     * @param newsContent 需要更新的新闻内容
     */
    public void createOrUpdate(NewsContent newsContent){
        mNewsContentDao.createOrUpdate(newsContent);
    }

    /**
     * 添加
     * @param newsContent 需要更新的新闻内容
     */
    public void create(NewsContent newsContent){
        mNewsContentDao.create(newsContent);
    }

    /**
     * 按照标题删除
     * @param   title 需要删除的新闻内容的标题
     * @return
     * @throws java.sql.SQLException
     */
    public int deleteByTitle(String title) throws SQLException {

        DeleteBuilder<NewsContent, Integer> deleteBuilder = mNewsContentDao.deleteBuilder();
        deleteBuilder.where().eq("title",title);
        return deleteBuilder.delete();

    }

    /**
     * 按照url删除
     * @param   url 需要删除的新闻内容的url
     * @return
     * @throws SQLException
     */
    public int deleteByUrl(String url) throws SQLException {

        DeleteBuilder<NewsContent, Integer> deleteBuilder = mNewsContentDao.deleteBuilder();
        deleteBuilder.where().eq("url",url);
        return deleteBuilder.delete();

    }

    /**
     * 按照标题查询
     * @param title
     * @return
     * @throws SQLException
     */
    public NewsContent searchByTitle(String title) throws SQLException {

        List<NewsContent> newsContents = mNewsContentDao.queryBuilder().where().eq("title",title).query();
        if (newsContents.size() > 0){
            return newsContents.get(0);
        }
        return null;
    }



    /**
     * 按照url查询
     * @param url
     * @return
     * @throws SQLException
     */
    public NewsContent searchByUrl(String url) throws SQLException {

        List<NewsContent> newsContents = mNewsContentDao.queryBuilder().where().eq("url",url).query();
        if (newsContents.size() > 0){
            return newsContents.get(0);
        }
        return null;

    }
}
