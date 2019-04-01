package com.course_project.arbitrage_analyzer.model;

public class SettingsContainer {

    private int updateRateSeconds;
    private String currencyPare;
    private int depthLimit;
    private boolean bitfinex;
    private boolean cex;
    private boolean exmo;
    private boolean gdax;

    public SettingsContainer() {
        updateRateSeconds = 10;
        currencyPare = "BTC/USD";
        depthLimit = 50;
        bitfinex = true;
        cex = true;
        exmo = true;
        gdax = true;
    }

    public boolean getBitfinex() {
        return bitfinex;
    }

    public void setBitfinex(boolean bitfinex) {
        this.bitfinex = bitfinex;
    }

    public boolean getCex() {
        return cex;
    }

    public void setCex(boolean cex) {
        this.cex = cex;
    }

    public boolean getExmo() {
        return exmo;
    }

    public void setExmo(boolean exmo) {
        this.exmo = exmo;
    }

    public boolean getGdax() {
        return gdax;
    }

    public void setGdax(boolean gdax) {
        this.gdax = gdax;
    }

    public int getUpdateRateSeconds() {
        return updateRateSeconds;
    }

    public void setUpdateRateSeconds(int updateRateSeconds) {
        this.updateRateSeconds = updateRateSeconds;
    }

    public String getCurrencyPare() {
        return currencyPare;
    }

    public void setCurrencyPare(String currencyPare) {
        this.currencyPare = currencyPare;
    }

    public int getDepthLimit() {
        return depthLimit;
    }

    public void setDepthLimit(int depthLimit) {
        this.depthLimit = depthLimit;
    }
}
