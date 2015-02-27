package com.example.lee.suesnews.bean;

import com.j256.ormlite.field.DatabaseField;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 新闻内容
 * Created by Administrator on 2015/2/13.
 */
public class NewsContent {

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private String title;   //新闻标题
    @DatabaseField
    private int type;       //新闻类型
    @DatabaseField
    private String date;    //日期
    @DatabaseField
    private String author;  //作者
    @DatabaseField
    private String source;  //来源
    @DatabaseField(canBeNull = false)
    private String url;     //文章地址
//    @DatabaseField
//    private Date updateTime;//更新时间
    @DatabaseField
    private String content; //文章内容
//    @DatabaseField
    private List<String> imgUrls;   //新闻图片地址



    /**
     * 得到格式化的新闻内容
     * @return
     */
    public String getFormatedContent(){

        return content;
    }


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

    public void addImgUrl(String imgUrl){
        if (this.imgUrls == null){
            imgUrls = new ArrayList<String>();
        }
        imgUrls.add(imgUrl);
    }

    public void addContent(String content){
        content += "\t\t" + content + "\n\n";
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

    @Override
    public String toString() {
        return "NewsContent{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", type=" + type +
                ", date='" + date + '\'' +
                ", author='" + author + '\'' +
                ", source='" + source + '\'' +
                ", url='" + url + '\'' +
                ", contents=" + content +
                ", imgUrls=" + imgUrls +
                '}';
    }
}
