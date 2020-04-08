package com.course_project.arbitrage_analyzer.network.exmo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExmoTickers {

    @SerializedName("BTC_USD")
    @Expose
    private ExmoSingleTicker BTCUSDTicker;

    @SerializedName("ETH_USD")
    @Expose
    private ExmoSingleTicker ETHUSDTicker;

    public ExmoSingleTicker getBTCUSDTicker() {
        return BTCUSDTicker;
    }

    public void setBTCUSDTicker(ExmoSingleTicker BTCUSDTicker) {
        this.BTCUSDTicker = BTCUSDTicker;
    }

    public ExmoSingleTicker getETHUSDTicker() {
        return ETHUSDTicker;
    }

    public void setETHUSDTicker(ExmoSingleTicker ETHUSDTicker) {
        this.ETHUSDTicker = ETHUSDTicker;
    }
}
