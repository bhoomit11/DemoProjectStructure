package com.timeout72hours.model;

import java.util.ArrayList;

/**
 * Created by bhumit on 2/12/17.
 */

public class FaqResponse {
    private String response;
    private String message;

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

    ArrayList<FaqData> faq = new ArrayList<>();

    public ArrayList<FaqData> getFaq() {
        return faq;
    }

    public void setFaq(ArrayList<FaqData> faq) {
        this.faq = faq;
    }
}
