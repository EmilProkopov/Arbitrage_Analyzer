package com.course_project.arbitrage_analyzer.network.cex;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CexResponse {

    @SerializedName("timestamp")
    @Expose
    private Integer timestamp;
    @SerializedName("bids")
    @Expose
    private List<List<Double>> bids = null;
    @SerializedName("asks")
    @Expose
    private List<List<Double>> asks = null;
    @SerializedName("pair")
    @Expose
    private String pair;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("sell_total")
    @Expose
    private String sellTotal;
    @SerializedName("buy_total")
    @Expose
    private String buyTotal;

    public Integer getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Integer timestamp) {
        this.timestamp = timestamp;
    }

    public List<List<Double>> getBids() {
        return bids;
    }

    public void setBids(List<List<Double>> bids) {
        this.bids = bids;
    }

    public List<List<Double>> getAsks() {
        return asks;
    }

    public void setAsks(List<List<Double>> asks) {
        this.asks = asks;
    }

    public String getPair() {
        return pair;
    }

    public void setPair(String pair) {
        this.pair = pair;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSellTotal() {
        return sellTotal;
    }

    public void setSellTotal(String sellTotal) {
        this.sellTotal = sellTotal;
    }

    public String getBuyTotal() {
        return buyTotal;
    }

    public void setBuyTotal(String buyTotal) {
        this.buyTotal = buyTotal;
    }

}