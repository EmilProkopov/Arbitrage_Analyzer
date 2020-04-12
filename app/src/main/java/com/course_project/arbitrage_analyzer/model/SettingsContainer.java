package com.course_project.arbitrage_analyzer.model;

import com.course_project.arbitrage_analyzer.model.disbalance_minimization.minimizers.MinimizerType;

public class SettingsContainer {

    private int updateRateSeconds;
    private String currencyPare;
    private int depthLimit;
    private boolean bitfinex;
    private boolean cex;
    private boolean exmo;
    private boolean gdax;

    private short historySize;
    private double riskConst;
    private int numberOfLaunches;
    private int maxIterations;
    private MinimizerType minimizerType;

    public SettingsContainer() {
        updateRateSeconds = 10;
        currencyPare = "BTC/USD";
        depthLimit = 50;
        bitfinex = true;
        cex = true;
        exmo = true;
        gdax = true;
        historySize = 10;
        riskConst = 0.5;
        numberOfLaunches = 1;
        maxIterations = 1000;
        minimizerType = MinimizerType.Simple;
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

    public short getHistorySize() {
        return historySize;
    }

    public void setHistorySize(short historySize) {
        this.historySize = historySize;
    }

    public double getRiskConst() {
        return riskConst;
    }

    public void setRiskConst(double riskConst) {
        this.riskConst = riskConst;
    }

    public int getNumberOfLaunches() {
        return numberOfLaunches;
    }

    public void setNumberOfLaunches(int numberOfLaunches) {
        this.numberOfLaunches = numberOfLaunches;
    }

    public MinimizerType getMinimizerType() {
        return minimizerType;
    }

    public void setMinimizerType(MinimizerType minimizerType) {
        this.minimizerType = minimizerType;
    }

    public int getMaxIterations() {
        return maxIterations;
    }

    public void setMaxIterations(int maxIterations) {
        this.maxIterations = maxIterations;
    }
}
