package com.timeout72hours.model;

import java.util.ArrayList;

/**
 * Created by hardip on 15/12/17.
 */

public class OngoingEventResponse {

    private String response,message,is_timer;
    private ArrayList<EventListEventData> event_data;

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

    public String getIs_timer() {
        return is_timer;
    }

    public void setIs_timer(String is_timer) {
        this.is_timer = is_timer;
    }

    public ArrayList<EventListEventData> getEvent_data() {
        return event_data;
    }

    public void setEvent_data(ArrayList<EventListEventData> event_data) {
        this.event_data = event_data;
    }
}
