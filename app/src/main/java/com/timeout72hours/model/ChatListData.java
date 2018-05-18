package com.timeout72hours.model;

/**
 * Created by bhumit on 13/12/17.
 */

public class ChatListData {
    private String friend_id;
    private String friend_name;
    private String text;
    private String text_id;
    private String date;
    private String userImage;
    private String devide_token;

    public String getFriend_id() {
        return friend_id;
    }

    public void setFriend_id(String friend_id) {
        this.friend_id = friend_id;
    }

    public String getFriend_name() {
        return friend_name;
    }

    public void setFriend_name(String friend_name) {
        this.friend_name = friend_name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText_id() {
        return text_id;
    }

    public void setText_id(String text_id) {
        this.text_id = text_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getDevide_token() {
        return devide_token;
    }

    public void setDevide_token(String devide_token) {
        this.devide_token = devide_token;
    }
}