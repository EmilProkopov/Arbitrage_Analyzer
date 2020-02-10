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

        double deltaAmount;
        double maxV_t = 0;

        int askInd = 0;
        int bidInd = 0;

        while ((askInd < ob.getAsks().size()) && (bidInd < ob.getBids().size())
                && (ob.getAsks().get(askInd).getPrice() < ob.getBids().get(bidInd).getPrice())) {


            double bidAmount = ob.getBids().get(bidInd).getAmount();
            double askAmount = ob.getAsks().get(askInd).getAmount();

            if (askAmount > bidAmount) {
                deltaAmount = bidAmount;
                bidInd++;
                ob.getAsks().get(askInd).setAmount(askAmount - deltaAmount);
            }
            else if (askAmount < bidAmount) {
                deltaAmount = askAmount;
                askInd++;
                ob.getBids().get(bidInd).setAmount(bidAmount - deltaAmount);
            }
            else {
                deltaAmount = askAmount;
                askInd++;
                bidInd++;
            }

            maxV_t += deltaAmount;
        }

        return maxV_t;
    }


    private CompiledOrderBook genNewOrderBook(CompiledOrderBook oldOB, double optimalV) {
        return new CompiledOrderBook();
    }


    public MinimizerResult getResult(CompiledOrderBook ob) {
        double maxV_t = calcMaxV_t(ob);

        long startTime = System.nanoTime();
        double optimalV = findOptimalV(ob, maxV_t);
        CompiledOrderBook newOB = genNewOrderBook(ob, optimalV);
        long time = System.nanoTime() - startTime;

        return new MinimizerResult(optimalV, time, newOB);
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
