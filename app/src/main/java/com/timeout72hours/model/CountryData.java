package com.timeout72hours.model;

import java.util.ArrayList;

/**
 * Created by bhumit on 1/12/17.
 */

public class CountryData {

    private String response,
    message;
    ArrayList<CountryDataList> contries_list=new ArrayList<>();

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

    public ArrayList<CountryDataList> getContries_list() {
        return contries_list;
    }

    public void setContries_list(ArrayList<CountryDataList> contries_list) {
        this.contries_list = contries_list;
    }
}
