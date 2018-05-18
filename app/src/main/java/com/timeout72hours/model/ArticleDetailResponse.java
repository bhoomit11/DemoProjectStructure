package com.timeout72hours.model;

import java.util.ArrayList;

/**
 * Created by hardip on 6/12/17.
 */

public class ArticleDetailResponse {

    private String response,message,article_name,article_des,image,publish_date_time,total_likes,
            total_comment,liked,page_count;

    private ArrayList<CommentListData> comment_data;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getArticle_name() {
        return article_name;
    }

    public void setArticle_name(String article_name) {
        this.article_name = article_name;
    }

    public String getArticle_des() {
        return article_des;
    }

    public void setArticle_des(String article_des) {
        this.article_des = article_des;
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

    public String getLiked() {
        return liked;
    }

    public void setLiked(String liked) {
        this.liked = liked;
    }

    public ArrayList<CommentListData> getComment_data() {
        return comment_data;
    }

    public void setComment_data(ArrayList<CommentListData> comment_data) {
        this.comment_data = comment_data;
    }

    public String getPage_count() {
        return page_count;
    }

    public void setPage_count(String page_count) {
        this.page_count = page_count;
    }
}
