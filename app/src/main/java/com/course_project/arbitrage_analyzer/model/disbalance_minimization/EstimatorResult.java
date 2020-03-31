package com.course_project.arbitrage_analyzer.model.disbalance_minimization;

public class EstimatorResult {
    private double firstCurrencyDisbalance;
    private double usedSecondCurrencyAmount;

    public EstimatorResult(double firstCurrencyDisbalance, double usedSecondCurrencyAmount) {
        this.firstCurrencyDisbalance = firstCurrencyDisbalance;
        this.usedSecondCurrencyAmount = usedSecondCurrencyAmount;
    }

    public double getFirstCurrencyDisbalance() {
        return firstCurrencyDisbalance;
    }

    public double getUsedSecondCurrencyAmount() {
        return usedSecondCurrencyAmount;
    }
}
