package com.course_project.arbitrage_analyzer.network.kucoin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {

    @SerializedName("SELL")
    @Expose
    private List<List<Double>> sELL = null;
    @SerializedName("BUY")
    @Expose
    private List<List<Double>> bUY = null;
    @SerializedName("timestamp")
    @Expose
    private Long timestamp;

    public List<List<Double>> getSELL() {
        return sELL;
    }

    public void setSELL(List<List<Double>> sELL) {
        this.sELL = sELL;
    }

    public List<List<Double>> getBUY() {
        return bUY;
    }

    public void setBUY(List<List<Double>> bUY) {
        this.bUY = bUY;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

}