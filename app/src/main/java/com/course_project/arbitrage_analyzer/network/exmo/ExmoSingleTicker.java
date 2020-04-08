package com.course_project.arbitrage_analyzer.network.exmo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExmoSingleTicker {

    @SerializedName("buy_price")
    @Expose
    private String buyPrice;

    @SerializedName("sell_price")
    @Expose
    private String sellPrice;

    @SerializedName("last_trade")
    @Expose
    private String lastTrade;

    @SerializedName("high")
    @Expose
    private String high;

    @SerializedName("low")
    @Expose
    private String low;

    @SerializedName("avg")
    @Expose
    private String avg;

    @SerializedName("vol")
    @Expose
    private String vol;

    @SerializedName("vol_curr")
    @Expose
    private String volCurr;

    @SerializedName("updated")
    @Expose
    private Integer updated;

    public String getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(String buyPrice) {
        this.buyPrice = buyPrice;
    }

    public String getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(String sellPrice) {
        this.sellPrice = sellPrice;
    }

    public String getLastTrade() {
        return lastTrade;
    }

    public void setLastTrade(String lastTrade) {
        this.lastTrade = lastTrade;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getAvg() {
        return avg;
    }

    public void setAvg(String avg) {
        this.avg = avg;
    }

    public String getVol() {
        return vol;
    }

    public void setVol(String vol) {
        this.vol = vol;
    }

    public String getVolCurr() {
        return volCurr;
    }

    public void setVolCurr(String volCurr) {
        this.volCurr = volCurr;
    }

    public Integer getUpdated() {
        return updated;
    }

    public void setUpdated(Integer updated) {
        this.updated = updated;
    }
}
