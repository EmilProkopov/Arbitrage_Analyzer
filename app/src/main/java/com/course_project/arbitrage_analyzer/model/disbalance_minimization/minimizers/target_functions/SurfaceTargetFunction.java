package com.course_project.arbitrage_analyzer.model.disbalance_minimization.minimizers.target_functions;

import com.course_project.arbitrage_analyzer.model.disbalance_minimization.minimizers.DisbalanceMinimizer;

public class SurfaceTargetFunction extends TargetFunction {

    private DisbalanceMinimizer minimizer;

    public SurfaceTargetFunction(double riskConst, DisbalanceMinimizer minimizer) {

        super(riskConst);
        this.minimizer = minimizer;
    }

    @Override
    public double calculate(double v_0, double v_t) {
        return -(riskConst*v_0 + (1-riskConst)*Math.max(v_t-v_0, 0))*minimizer.gaussian(v_t);
    }
}
