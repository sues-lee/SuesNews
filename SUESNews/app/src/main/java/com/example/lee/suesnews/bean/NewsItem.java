package com.example.lee.suesnews.bean;

import com.j256.ormlite.field.DatabaseField;

/**
 * 新闻实体类
 * Created by Administrator on 2015/1/18.
 */
public class NewsItem {

    @DatabaseField(generatedId = true)
    private int id;    //ID
    @DatabaseField
    private int type;       //文章类型
    @DatabaseField
    private String title;   //标题
    @DatabaseField(canBeNull = false)
    private String url;    //链接
    @DatabaseField(canBeNull = true)
    private String date;    //日期
    @DatabaseField
    private String content; //文章内容
    @DatabaseField
    private int pageNumber; //所在页码
    @DatabaseField(canBeNull = true)
    private String source;  //媒体新闻的来源


    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "NewsItem{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", date='" + date + '\'' +
                ", content='" + content + '\'' +
                '}';
    }



}
