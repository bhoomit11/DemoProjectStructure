package com.timeout72hours.model;

import java.util.ArrayList;

/**
 * Created by hardip on 6/12/17.
 */

public class ArticleListResponse {

    private String response,message;
    private ArrayList<ArticleListData> article_data;

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

    public ArrayList<ArticleListData> getArticle_data() {
        return article_data;
    }

    public void setArticle_data(ArrayList<ArticleListData> article_data) {
        this.article_data = article_data;
    }
}
