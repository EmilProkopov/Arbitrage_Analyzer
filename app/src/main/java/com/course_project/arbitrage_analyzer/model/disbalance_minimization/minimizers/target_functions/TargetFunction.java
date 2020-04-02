package com.course_project.arbitrage_analyzer.model.disbalance_minimization.minimizers.target_functions;

public abstract class TargetFunction {

    protected double riskConst;

    public abstract double calculate(double v_0, double v_t);

    TargetFunction(double riskConst) {
        this.riskConst = riskConst;
    }
}
