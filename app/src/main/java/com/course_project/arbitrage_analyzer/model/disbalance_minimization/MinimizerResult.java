package com.course_project.arbitrage_analyzer.model.disbalance_minimization;

import com.course_project.arbitrage_analyzer.model.CompiledOrderBook;

public class MinimizerResult {

    private double optimalV;
    private double optimalAmount;
    private CompiledOrderBook resultOrderBook; // Lists of orders that should be made by trader
    private long time; // Time required for finding optimalV and compiling the order book

    public MinimizerResult(double optimalV, double optimalAmount, long time, CompiledOrderBook resultOrderBook) {
        this.optimalV = optimalV;
        this.optimalAmount = optimalAmount;
        this.resultOrderBook = resultOrderBook;
        this.time = time;
    }

    public double getOptimalV() {
        return optimalV;
    }

    public double getOptimalAmount() { return optimalAmount; }

    public CompiledOrderBook getResultOrderBook() { return resultOrderBook; }

    public long getTime() {
        return time;
    }
}
