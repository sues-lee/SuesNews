package com.example.lee.suesnews.dao;

import android.content.Context;

import com.example.lee.suesnews.bean.NewsItem;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.DeleteBuilder;

import java.sql.SQLException;
import java.util.List;

/**
 * 对新闻列表的数据库操作
 * Created by Administrator on 2015/2/24.
 */
public class NewsItemDao {

    private RuntimeExceptionDao<NewsItem,Integer> mNewsItemDao;
    private DataBaseHelper mDataBaseHelpter;

    public NewsItemDao(Context context) {
        mDataBaseHelpter = DataBaseHelper.getHelper(context);
        this.mNewsItemDao = mDataBaseHelpter.getNewsItemRuntimeDao();
    }

    /**
     * 更新或添加
     * @param newsItem 需要更新的新闻列表项
     */
    public void createOrUpdate(NewsItem newsItem){
        mNewsItemDao.createOrUpdate(newsItem);
    }

    /**
     * 按照标题删除
     * @param   title 需要删除的新闻项的标题
     * @return
     * @throws SQLException
     */
    public int deleteByTitle(String title) throws SQLException {

        DeleteBuilder<NewsItem, Integer> deleteBuilder = mNewsItemDao.deleteBuilder();
        deleteBuilder.where().eq("title",title);
        return deleteBuilder.delete();

    }

    /**
     * 按照url删除
     * @param   url 需要删除的新闻项的url
     * @return
     * @throws SQLException
     */
    public int deleteByUrl(String url) throws SQLException {

        DeleteBuilder<NewsItem, Integer> deleteBuilder = mNewsItemDao.deleteBuilder();
        deleteBuilder.where().eq("url",url);
        return deleteBuilder.delete();

    }

    /**
     * 按照标题查询
     * @param title
     * @return
     * @throws SQLException
     */
    public NewsItem searchByTitle(String title) throws SQLException {

        List<NewsItem> newsItems = mNewsItemDao.queryBuilder().where().eq("title",title).query();
        if (newsItems.size() > 0){
            return newsItems.get(0);
        }
        return null;
    }

    /**
     * 按照页码和类型查询
     * @param page
     * @param type
     * @return
     * @throws SQLException
     */
    public List<NewsItem> searchByPageAndType(int page,int type) throws SQLException {

        List<NewsItem> newsItems = mNewsItemDao.queryBuilder().where().eq("pageNumber",page).and()
                .eq("type",type).query();
        if (newsItems.size() > 0){
            return newsItems;
        }
        return null;
    }

    /**
     * 按照url查询
     * @param url
     * @return
     * @throws SQLException
     */
    public NewsItem searchByUrl(String url) throws SQLException {

        List<NewsItem> newsItems = mNewsItemDao.queryBuilder().where().eq("url",url).query();
        if (newsItems.size() > 0){
            return newsItems.get(0);
        }
        return null;

    }

}
