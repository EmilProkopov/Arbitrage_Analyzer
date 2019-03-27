package com.course_project.arbitrage_analyzer.network.bitfinex;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BitfinexResponse {

    @SerializedName("bids")
    @Expose
    private List<Bid> bids = null;
    @SerializedName("asks")
    @Expose
    private List<Ask> asks = null;

    public List<Bid> getBids() {
        return bids;
    }

    public void setBids(List<Bid> bids) {
        this.bids = bids;
    }

    public List<Ask> getAsks() {
        return asks;
    }

    public void setAsks(List<Ask> asks) {
        this.asks = asks;
    }

}