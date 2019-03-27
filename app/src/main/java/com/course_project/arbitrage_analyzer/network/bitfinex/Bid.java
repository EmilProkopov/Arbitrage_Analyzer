package com.course_project.arbitrage_analyzer.network.bitfinex;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Bid {

    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("timestamp")
    @Expose
    private String timestamp;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

}