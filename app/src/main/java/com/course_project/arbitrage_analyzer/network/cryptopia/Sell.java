package com.course_project.arbitrage_analyzer.network.cryptopia;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Sell {

    @SerializedName("TradePairId")
    @Expose
    private Integer tradePairId;
    @SerializedName("Label")
    @Expose
    private String label;
    @SerializedName("Price")
    @Expose
    private Double price;
    @SerializedName("Volume")
    @Expose
    private Double volume;
    @SerializedName("Total")
    @Expose
    private Double total;

    public Integer getTradePairId() {
        return tradePairId;
    }

    public void setTradePairId(Integer tradePairId) {
        this.tradePairId = tradePairId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getVolume() {
        return volume;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

}