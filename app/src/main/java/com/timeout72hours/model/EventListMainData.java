package com.timeout72hours.model;

import java.util.ArrayList;

/**
 * Created by hardip on 7/12/17.
 */

public class EventListMainData {

    private String date;
    private ArrayList<EventListStageData> stage_data;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<EventListStageData> getStage_data() {
        return stage_data;
    }

    public void setStage_data(ArrayList<EventListStageData> stage_data) {
        this.stage_data = stage_data;
    }
}
