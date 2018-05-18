package com.timeout72hours.model;

import java.util.ArrayList;

/**
 * Created by hardip on 3/12/17.
 */

public class PassesListResponse {

    private String response,message;
    private ArrayList<PassData> pass_detail;

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

    public ArrayList<PassData> getPass_detail() {
        return pass_detail;
    }

    public void setPass_detail(ArrayList<PassData> pass_detail) {
        this.pass_detail = pass_detail;
    }
}
