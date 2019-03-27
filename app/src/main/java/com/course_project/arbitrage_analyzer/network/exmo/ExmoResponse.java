package com.course_project.arbitrage_analyzer.network.exmo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExmoResponse {

    @SerializedName("BTC_USD")
    @Expose
    private BTCUSD BTCUSD;

    @SerializedName("ETH_USD")
    @Expose
    private ETHUSD ETHUSD;

    public BTCUSD getBTCUSD() {
        return BTCUSD;
    }

    public void setBTCUSD(BTCUSD BTCUSD) {
        this.BTCUSD = BTCUSD;
    }

    public ETHUSD getETHUSD() {
        return ETHUSD;
    }

    public void setETHUSD(ETHUSD ETHUSD) {
        this.ETHUSD = ETHUSD;
    }
}