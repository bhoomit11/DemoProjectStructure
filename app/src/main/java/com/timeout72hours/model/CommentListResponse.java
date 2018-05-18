package com.timeout72hours.model;

import java.util.ArrayList;

/**
 * Created by hardip on 6/12/17.
 */

public class CommentListResponse {

    private String response,message,page_count;

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

    public String getPage_count() {
        return page_count;
    }

    public void setPage_count(String page_count) {
        this.page_count = page_count;
    }

    public ArrayList<CommentListData> getComment_data() {
        return comment_data;
    }

    public void setComment_data(ArrayList<CommentListData> comment_data) {
        this.comment_data = comment_data;
    }
}
