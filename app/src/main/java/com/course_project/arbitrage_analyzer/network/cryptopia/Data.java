package com.course_project.arbitrage_analyzer.network.cryptopia;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {

    @SerializedName("Buy")
    @Expose
    private List<Buy> buy = null;
    @SerializedName("Sell")
    @Expose
    private List<Sell> sell = null;

    public List<Buy> getBuy() {
        return buy;
    }

    public void setBuy(List<Buy> buy) {
        this.buy = buy;
    }

    public List<Sell> getSell() {
        return sell;
    }

    public void setSell(List<Sell> sell) {
        this.sell = sell;
    }

}