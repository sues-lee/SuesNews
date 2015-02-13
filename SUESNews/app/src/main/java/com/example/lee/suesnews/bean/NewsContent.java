package com.example.lee.suesnews.bean;

import java.util.List;

/**
 * 新闻内容
 * Created by Administrator on 2015/2/13.
 */
public class NewsContent {

    private int id;
    private String title;   //新闻标题
    private int type;       //新闻类型
    private String date;    //日期
    private String author;  //作者
    private String source;  //来源
    private String url;     //文章地址
    private List<String> imgUrls;   //新闻图片地址


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getImgUrls() {
        return imgUrls;
    }

    public void setImgUrls(List<String> imgUrls) {
        this.imgUrls = imgUrls;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
