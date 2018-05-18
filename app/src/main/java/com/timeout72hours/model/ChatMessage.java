package com.timeout72hours.model;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.Date;

/**
 * Created by bhumit on 8/12/17.
 */

public class ChatMessage {
    private String sender_id;
    private String recever_id;
    private String sender_name;
    private String recevier_name;
    private String text;
    private String text_id;
    private String date;
    private String userImage;
    private String devide_token;
    private String display_name;
    private String display_id;

    private String isRead = "false";

    public ChatMessage() {
    }

    public ChatMessage(String sender_id, String recever_id, String sender_name, String recevier_name, String text, String text_id, String date) {
        this.sender_id = sender_id;
        this.recever_id = recever_id;
        this.sender_name = sender_name;
        this.recevier_name = recevier_name;
        this.text = text;
        this.text_id = text_id;
        this.date = date;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getRecever_id() {
        return recever_id;
    }

    public void setRecever_id(String recever_id) {
        this.recever_id = recever_id;
    }

    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }

    public String getRecevier_name() {
        return recevier_name;
    }

    public void setRecevier_name(String recevier_name) {
        this.recevier_name = recevier_name;
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

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getDisplay_id() {
        return display_id;
    }

    public void setDisplay_id(String display_id) {
        this.display_id = display_id;
    }

    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }
}