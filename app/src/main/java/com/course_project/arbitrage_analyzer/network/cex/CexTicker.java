package com.course_project.arbitrage_analyzer.network.cex;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CexTicker {

    @SerializedName("timestamp")
    @Expose
    private String timestamp;

    @SerializedName("low")
    private String low;

    @SerializedName("high")
    private String high;

    @SerializedName("last")
    private String last;

    @SerializedName("volume")
    private String volume;

    @SerializedName("volume30d")
    private String volume30d;

    @SerializedName("bid")
    private Double bid;

    @SerializedName("ask")
    private Double ask;

    @SerializedName("priceChange")
    private String priceChange;

    @SerializedName("priceChangePercentage")
    private String priceChangePercentage;

    @SerializedName("pair")
    private String pair;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getVolume30d() {
        return volume30d;
    }

    public void setVolume30d(String volume30d) {
        this.volume30d = volume30d;
    }

    public Double getBid() {
        return bid;
    }

    public void setBid(Double bid) {
        this.bid = bid;
    }

    public Double getAsk() {
        return ask;
    }

    public void setAsk(Double ask) {
        this.ask = ask;
    }

    public String getPriceChange() {
        return priceChange;
    }

    public void setPriceChange(String priceChange) {
        this.priceChange = priceChange;
    }

    public String getPriceChangePercentage() {
        return priceChangePercentage;
    }

    public void setPriceChangePercentage(String priceChangePercentage) {
        this.priceChangePercentage = priceChangePercentage;
    }

    public String getPair() {
        return pair;
    }

    public void setPair(String pair) {
        this.pair = pair;
    }
}
