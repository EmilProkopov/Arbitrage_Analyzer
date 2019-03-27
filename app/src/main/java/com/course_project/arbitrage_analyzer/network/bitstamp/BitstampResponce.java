package com.course_project.arbitrage_analyzer.network.bitstamp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BitstampResponce {

    @SerializedName("timestamp")
    @Expose
    private String timestamp;
    @SerializedName("bids")
    @Expose
    private List<List<String>> bids = null;
    @SerializedName("asks")
    @Expose
    private List<List<String>> asks = null;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public List<List<String>> getBids() {
        return bids;
    }

    public void setBids(List<List<String>> bids) {
        this.bids = bids;
    }

    public List<List<String>> getAsks() {
        return asks;
    }

    public void setAsks(List<List<String>> asks) {
        this.asks = asks;
    }

}