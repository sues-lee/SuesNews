package com.example.lee.mysues.utils;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.example.lee.mysues.common.NewsTypes;

/**
 * 相关API
 * Created by Administrator on 2015/1/18.
 */
public class SuesApiUtils {

    public static final String NEWS_URL_MAIN = "http://news.sues.edu.cn";
    //各个页面主要url
    public static final String NEWS_URL_XXYW = "http://news.sues.edu.cn/s/593/t/301/p/31/list.htm";
    public static final String NEWS_URL_XYKX = "http://news.sues.edu.cn/s/593/t/301/p/32/list.htm";
    public static final String NEWS_URL_KJDT = "http://news.sues.edu.cn/s/593/t/301/p/33/list.htm";
    public static final String NEWS_URL_MTJJ = "http://news.sues.edu.cn/s/593/t/301/p/34/list.htm";
    public static final String NEWS_URL_BMXW = "http://news.sues.edu.cn/s/593/t/301/p/35/list.htm";

    //用于拼接的基础url
    private static final String NEWS_URL_XXYW_PART = "http://news.sues.edu.cn/s/593/t/301/p/31/i/";
    private static final String NEWS_URL_XYKX_PART = "http://news.sues.edu.cn/s/593/t/301/p/32/i/";
    private static final String NEWS_URL_KJDT_PART = "http://news.sues.edu.cn/s/593/t/301/p/33/i/";
    private static final String NEWS_URL_MTJJ_PART = "http://news.sues.edu.cn/s/593/t/301/p/34/i/";
    private static final String NEWS_URL_BMXW_PART = "http://news.sues.edu.cn/s/593/t/301/p/35/i/";

    private static final String URL_END = "/list.htm";
    private static final String TOTAL_PAGE_END = "/i.htm";

    //URL规则：
    //基础url + 当前页码 + URL_END = 对应页码的新闻页面url
    //基础url + TOTAL_PAGE_END = 该页面的新闻总条数
    
    
    private static int getTotalPageByUrl(String url){
        String htmlStr = null;
        try {
            htmlStr = HttpUtils.doGet(url);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Document doc = Jsoup.parse(htmlStr);
        //Element element = doc.getElementById("LIST_PAGINATION_COUNT");

        int totalNewsItem = Integer.parseInt(htmlStr);  //新闻总条数
        int totalPage = totalNewsItem/21 + totalNewsItem%21==0?0:1; //新闻总页数
        return totalPage;
    }

    /**
     * 获得相应页面的新闻总页数
     * @param newsType
     * @return
     */
    public static int getTotalPage(int newsType){
        int totalPage = 0;
        switch(newsType){
            case NewsTypes.NEWS_TPYE_XXYW:
                totalPage = getTotalPageByUrl(NEWS_URL_XXYW_PART+TOTAL_PAGE_END);
                break;
            case NewsTypes.NEWS_TPYE_XYKX:
                totalPage = getTotalPageByUrl(NEWS_URL_XYKX+TOTAL_PAGE_END);
                break;
            case NewsTypes.NEWS_TPYE_KJDT:
                totalPage = getTotalPageByUrl(NEWS_URL_KJDT+TOTAL_PAGE_END);
                break;
            case NewsTypes.NEWS_TPYE_MTJJ:
                totalPage = getTotalPageByUrl(NEWS_URL_MTJJ+TOTAL_PAGE_END);
                break;
            case NewsTypes.NEWS_TPYE_BMXW:
                totalPage = getTotalPageByUrl(NEWS_URL_BMXW+TOTAL_PAGE_END);
                break;
            default:
                break;
        }
        return totalPage;
    }


    /**
     * 得到相应页面的url
     * @param newsType
     * @param currentPage
     * @return
     */
    public static String getNewsUrl(int newsType,int currentPage){
        int totalPage = getTotalPage(newsType);
        int pageUrl = totalPage - currentPage - 1;  //url中的页码
        String currentPageString = Integer.toString(pageUrl);
        String url = null;
        switch(newsType){
            case NewsTypes.NEWS_TPYE_XXYW:
                url = NEWS_URL_XXYW_PART + currentPageString;
                break;
            case NewsTypes.NEWS_TPYE_XYKX:
                url = NEWS_URL_XYKX_PART + currentPageString;
                break;
            case NewsTypes.NEWS_TPYE_KJDT:
                url = NEWS_URL_KJDT_PART + currentPageString;
                break;
            case NewsTypes.NEWS_TPYE_MTJJ:
                url = NEWS_URL_MTJJ_PART + currentPageString;
                break;
            case NewsTypes.NEWS_TPYE_BMXW:
                url = NEWS_URL_BMXW_PART + currentPageString;
                break;
            default:
                break;
        }
        url += URL_END;
        return url;
    }


}
