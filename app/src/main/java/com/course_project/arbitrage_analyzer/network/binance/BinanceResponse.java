package com.course_project.arbitrage_analyzer.network.binance;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BinanceResponse {

    @SerializedName("lastUpdateId")
    @Expose
    private Integer lastUpdateId;
    @SerializedName("bids")
    @Expose
    private List<List<String>> bids = null;
    @SerializedName("asks")
    @Expose
    private List<List<String>> asks = null;

    public Integer getLastUpdateId() {
        return lastUpdateId;
    }

    public void setLastUpdateId(Integer lastUpdateId) {
        this.lastUpdateId = lastUpdateId;
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