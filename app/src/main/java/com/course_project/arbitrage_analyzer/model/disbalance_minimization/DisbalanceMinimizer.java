package com.course_project.arbitrage_analyzer.model.disbalance_minimization;

import com.course_project.arbitrage_analyzer.model.CompiledOrderBook;

public abstract class DisbalanceMinimizer {

    private TargetFunction targetFunction;

    private short maxRoundsCount;

    DisbalanceMinimizer(TargetFunction targetFunction, short maxRoundsCount) {
        this.targetFunction = targetFunction;
        this.maxRoundsCount = maxRoundsCount;
    }

    abstract double findOptimalV(CompiledOrderBook ob, double maxV_t);

    private double calcMaxV_t(CompiledOrderBook ob) {
        return 1;
    }

    private CompiledOrderBook genNewOrderBook(CompiledOrderBook oldOB, double optimalV) {
        return new CompiledOrderBook();
    }

    public MinimizerResult getResult(CompiledOrderBook ob) {
        double maxV_t = calcMaxV_t(ob);

        long startTime = System.nanoTime();
        double optimalV = findOptimalV(ob, maxV_t);
        long time = System.nanoTime() - startTime;

        return new MinimizerResult(optimalV, time, genNewOrderBook(ob, optimalV));
    }


    public TargetFunction getTargetFunction() {
        return targetFunction;
    }

    public short getMaxRoundsCount() {
        return maxRoundsCount;
    }

    public void setMaxRoundsCount(short maxRoundsCount) {
        this.maxRoundsCount = maxRoundsCount;
    }
}
