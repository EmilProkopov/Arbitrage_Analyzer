package com.course_project.arbitrage_analyzer.model.disbalance_minimization.minimizers.target_functions;

public class TableTargetFunction extends TargetFunction {

    public TableTargetFunction(double riskConst) {
        super(riskConst);
    }

    @Override
    public double calculate(double v_0, double v_t) {
        return riskConst*v_0 + (1-riskConst)*Math.max(v_t-v_0, 0);
    }
}
