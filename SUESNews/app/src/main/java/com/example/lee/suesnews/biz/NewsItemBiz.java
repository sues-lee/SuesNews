package com.example.lee.suesnews.biz;

import android.util.Log;

import com.example.lee.suesnews.bean.NewsContent;
import com.example.lee.suesnews.bean.NewsItem;
import com.example.lee.suesnews.common.NewsTypes;
import com.example.lee.suesnews.utils.HttpUtils;
import com.example.lee.suesnews.utils.StringUtils;
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

    //新闻列表页相关class
    private static final String BASE_TABLE_CLASS = "border1";  //包含新闻列表的table的class
    private static final String COLUMN_TABLE_CLASS = "columnStyle";    //包含新闻条目的table的class
    private static final String POST_TIME_CLASS = "posttime";        //包含新闻时间的class
    private static final String NEWS_SOURCE_CLASS = "derivation";        //包含新闻来源媒体的class

    //新闻内容页相关class

    private static final String NEWS_TITLE_CLASS = "biaoti";        //包含新闻标题td的class
    private static final String NEWS_META_CLASS = "postmeta";       //包含新闻相关信息的p标签的class
    private static final String NEWS_META_ITEM_CLASS = "meta_item";  //包含新闻相关信息条目的class
    private static final String NEWS_ARTICLE_CLASS = "article";  //包含新闻内容td的class


    /**
     * 根据新闻类型和页码得到新闻列表
     * @param newsType      新闻类型
     * @param currentPage   页码
     * @return              新闻列表
     * @throws Exception
     */
    public static List<NewsItem> getNewsItems(int newsType,int currentPage) throws Exception {

        String url = SuesApiUtils.getNewsUrl(newsType, currentPage);
        Log.i("ASD","urlLIST:"+url);
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
            newsItem.setUrl(SuesApiUtils.NEWS_URL_MAIN + contentUrl);

            newsItem.setTitle(link.child(0).text());    //设置新闻标题


            if (newsType != NewsTypes.NEWS_TPYE_MTJJ){
                Element postTime = columnTable.getElementsByClass(POST_TIME_CLASS).get(0);
                newsItem.setDate(postTime.text());
            }else{
                Element source = columnTable.getElementsByClass(NEWS_SOURCE_CLASS).get(0);
                newsItem.setSource(source.text());
            }

            newsItem.setPageNumber(currentPage);
            //文章内容点击进入后再添加

            newsItems.add(newsItem);

        }

        return newsItems;
    }

    /**
     * 根据新闻的url获取新闻内容
     * @param url 新闻url
     * @return
     */
    public static NewsContent getNewsContent(String url) throws Exception {
//        Log.i("ASD","url:"+url);
        //获取html
        String htmlStr = HttpUtils.doGet(url);
//        Log.i("ASD","html"+htmlStr);
        NewsContent news = new NewsContent();

        Document document = Jsoup.parse(htmlStr);
//        Log.i("ASD","html"+htmlStr);
        //新闻url
        news.setUrl(url);

        //新闻标题
        Element titleElement = document.getElementsByClass(NEWS_TITLE_CLASS).get(0);
        Log.i("ASD","Title: "+titleElement.text());
        news.setTitle(titleElement.text());

        //包含新闻信息的p标签
        Element metaElement = document.getElementsByClass(NEWS_META_CLASS).get(0);
        Log.i("ASD","metaElement"+metaElement.text());
        //新闻时间
        news.setDate(StringUtils.getDateFromString(metaElement.text()));

        //新闻作者
        Element authorElement = document.getElementsByClass(NEWS_META_ITEM_CLASS).get(0);
        Log.i("ASD","authorElement"+authorElement.text());
        news.setAuthor(authorElement.text());

        //新闻来源
        Element sourceElement = document.getElementsByClass(NEWS_META_ITEM_CLASS).get(2);
        Log.i("ASD","sourceElement"+sourceElement.text());
        news.setSource(sourceElement.text());

        //新闻内容
        Element contentElement = document.getElementsByClass(NEWS_ARTICLE_CLASS).get(0);
        Elements contentItems = contentElement.children();
        //新闻内容都在p标签内，其中某些是图片
        for(Element contentItem : contentItems){

            Elements images = contentItem.getElementsByTag("img");
            //获取图片
            if (images.size() > 0){
                for (Element image : images){
                    news.addImgUrl(image.attr("src"));
                }
                continue;
            }
            if(contentItem.text() == ""){
                continue;
            }
            Log.i("ASD","contentText"+contentItem.text());
            news.addContent(contentItem.text());

        }


        return news;

    }
}
