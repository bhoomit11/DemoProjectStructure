package com.timeout72hours.model;

/**
 * Created by hardip on 16/12/17.
 */

public class TransactionData {

    private String date_time,transaction_id,transaction_type,pos_name,amount,has_detail;

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getTransaction_type() {
        return transaction_type;
    }

    public void setTransaction_type(String transaction_type) {
        this.transaction_type = transaction_type;
    }

    public String getPos_name() {
        return pos_name;
    }

    public void setPos_name(String pos_name) {
        this.pos_name = pos_name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getHas_detail() {
        return has_detail;
    }

    public void setHas_detail(String has_detail) {
        this.has_detail = has_detail;
    }
}
