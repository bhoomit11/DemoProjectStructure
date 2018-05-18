package com.timeout72hours.model;

import java.util.ArrayList;

/**
 * Created by hardip on 20/12/17.
 */

public class TransactionDetailResponse {

    private String response,message;
    private ArrayList<TransactionDetailData> transaction_detail;

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

    public ArrayList<TransactionDetailData> getTransaction_detail() {
        return transaction_detail;
    }

    public void setTransaction_detail(ArrayList<TransactionDetailData> transaction_detail) {
        this.transaction_detail = transaction_detail;
    }
}
