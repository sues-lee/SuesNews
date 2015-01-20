package com.example.lee.suesnews.biz;

import com.example.lee.suesnews.bean.NewsItem;
import com.example.lee.suesnews.utils.HttpUtils;
import com.example.lee.suesnews.utils.SuesApiUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * 处理新闻的业务逻辑类
 * Created by Administrator on 2015/1/18.
 */
public class NewsItemBiz {

    private final String BASE_TABLE_CLASS = "border1";  //包含新闻列表的table的class
    private final String COLUMN_TABLE_CLASS = "columnStyle";    //包含新闻条目的table的class
    private final String POST_TIME_CLASS = "posttime";        //包含新闻时间的class

    public List<NewsItem> getNewsItems(int newsType,int currentPage) throws Exception {

        String url = SuesApiUtils.getNewsUrl(newsType, currentPage);
        String htmlStr = HttpUtils.doGet(url);

        List<NewsItem> newsItems = new ArrayList<NewsItem>();
        NewsItem newsItem;
        Document document = Jsoup.parse(htmlStr);
        Element itemTable = document.getElementsByClass(BASE_TABLE_CLASS).get(0);
        Elements items = itemTable.child(0).children();
        for (int i=0;i<items.size();i++){
            newsItem = new NewsItem();
            newsItem.setType(newsType);

            Element columnTable = items.get(i).getElementsByClass(COLUMN_TABLE_CLASS).get(0);
            Element link = columnTable.getElementsByTag("a").get(0);
            String contentUrl = link.attr("href");  //新闻内容链接
            newsItem.setLink(SuesApiUtils.NEWS_URL_MAIN+contentUrl);
            newsItem.setTitle(link.child(0).text());    //设置新闻标题

            Element postTime = columnTable.getElementsByClass(POST_TIME_CLASS).get(0);
            newsItem.setDate(postTime.text());
            //文章内容点击进入后再添加

            newsItems.add(newsItem);

        }

        return newsItems;
    }
}
