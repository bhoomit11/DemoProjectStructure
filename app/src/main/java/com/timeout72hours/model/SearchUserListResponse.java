package com.timeout72hours.model;

import java.util.ArrayList;

/**
 * Created by hardip on 8/12/17.
 */

public class SearchUserListResponse {

    private String response,message,page_count;
    private ArrayList<UserListData> user_data;

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

    public ArrayList<UserListData> getUser_data() {
        return user_data;
    }

    public void setUser_data(ArrayList<UserListData> user_data) {
        this.user_data = user_data;
    }

    public String getPage_count() {
        return page_count;
    }

    public void setPage_count(String page_count) {
        this.page_count = page_count;
    }
}
