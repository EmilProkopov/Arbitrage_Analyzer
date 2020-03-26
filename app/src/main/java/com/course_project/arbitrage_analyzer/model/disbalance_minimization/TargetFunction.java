package com.course_project.arbitrage_analyzer.model.disbalance_minimization;

public class TargetFunction {

    private double riskConst;
    private double alpha;
    private double sigma;

    public double gaussian(double x) {
        return Math.exp(-(x - alpha)*(x - alpha) / (2 * sigma*sigma))
                / (sigma * Math.sqrt(2*Math.PI));
    }

    public double calculate(double v_0, double v_t) {
        return riskConst * v_0 + (1-riskConst) * Math.max(v_t-v_0, 0);
    }

    TargetFunction(double riskConst, double alpha, double sigma) {
        this.riskConst = riskConst;
        this.alpha = alpha;
        this.sigma = sigma;
    }

    public double getRiskConst() {
        return riskConst;
    }

    public void setRiskConst(double riskConst) {
        this.riskConst = riskConst;
    }

    public double getAlpha() {
        return alpha;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    public double getSigma() {
        return sigma;
    }

    public void setSigma(double sigma) {
        this.sigma = sigma;
    }
}
