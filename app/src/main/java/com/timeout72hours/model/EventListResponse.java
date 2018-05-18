package com.timeout72hours.model;

import java.util.ArrayList;

/**
 * Created by hardip on 7/12/17.
 */

public class EventListResponse {

    private String response,message;
    private ArrayList<EventListMainData> main_data;

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

    public ArrayList<EventListMainData> getMain_data() {
        return main_data;
    }

    public void setMain_data(ArrayList<EventListMainData> main_data) {
        this.main_data = main_data;
    }
}
