package com.timeout72hours.model;

import java.util.ArrayList;

/**
 * Created by hardip on 16/12/17.
 */

public class TransactionsListResponse {

    private String response,message;
    private ArrayList<TransactionData> transaction_data;

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

    public ArrayList<TransactionData> getTransaction_data() {
        return transaction_data;
    }

    public void setTransaction_data(ArrayList<TransactionData> transaction_data) {
        this.transaction_data = transaction_data;
    }
}
