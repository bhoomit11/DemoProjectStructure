package com.timeout72hours.model;

import java.util.ArrayList;

/**
 * Created by hardip on 7/12/17.
 */

public class EventListStageData {

    private String stage_name,stage_desc,stage_image;
    private ArrayList<EventListEventData> event_data_data;

    public String getStage_name() {
        return stage_name;
    }

    public void setStage_name(String stage_name) {
        this.stage_name = stage_name;
    }

    public String getStage_desc() {
        return stage_desc;
    }

    public void setStage_desc(String stage_desc) {
        this.stage_desc = stage_desc;
    }

    public String getStage_image() {
        return stage_image;
    }

    public void setStage_image(String stage_image) {
        this.stage_image = stage_image;
    }

    public ArrayList<EventListEventData> getEvent_data_data() {
        return event_data_data;
    }

    public void setEvent_data_data(ArrayList<EventListEventData> event_data_data) {
        this.event_data_data = event_data_data;
    }
}
