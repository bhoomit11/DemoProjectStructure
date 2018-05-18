package com.timeout72hours.model;

/**
 * Created by hardip on 6/12/17.
 */

public class ArticleListData {

    public String getArticle_id() {
        return article_id;
    }

    public void setArticle_id(String article_id) {
        this.article_id = article_id;
    }

    public String getArticle_name() {
        return article_name;
    }

    public void setArticle_name(String article_name) {
        this.article_name = article_name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPublish_date_time() {
        return publish_date_time;
    }

    public void setPublish_date_time(String publish_date_time) {
        this.publish_date_time = publish_date_time;
    }

    public String getTotal_likes() {
        return total_likes;
    }

    public void setTotal_likes(String total_likes) {
        this.total_likes = total_likes;
    }

    public String getTotal_comment() {
        return total_comment;
    }

    public void setTotal_comment(String total_comment) {
        this.total_comment = total_comment;
    }

    private String article_id,article_name,image,publish_date_time,total_likes,total_comment;


}
