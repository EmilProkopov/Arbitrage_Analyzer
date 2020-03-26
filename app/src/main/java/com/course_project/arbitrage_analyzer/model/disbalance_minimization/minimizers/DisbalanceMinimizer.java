package com.course_project.arbitrage_analyzer.model.disbalance_minimization.minimizers;

import com.course_project.arbitrage_analyzer.model.CompiledOrderBook;
import com.course_project.arbitrage_analyzer.model.disbalance_minimization.MinimizerResult;
import com.course_project.arbitrage_analyzer.model.disbalance_minimization.TargetFunction;

import java.util.LinkedList;
import java.util.ListIterator;

public abstract class DisbalanceMinimizer {

    private TargetFunction targetFunction;
    private double tradeRatePerSecond;
    private short maxRoundsCount;
    private short timeHistoryMaxLength;

    private LinkedList<Long> timeHistory;

    DisbalanceMinimizer(TargetFunction targetFunction, short maxRoundsCount, short timeHistoryMaxLength) {
        this.targetFunction = targetFunction;
        this.maxRoundsCount = maxRoundsCount;
        this.timeHistory = new LinkedList<>();
        this.timeHistoryMaxLength = timeHistoryMaxLength;
    }


    abstract double findOptimalV(CompiledOrderBook ob, double maxV_t);


    private void updateTargetFunctionParams() {

        while (timeHistory.size() > timeHistoryMaxLength) {
            timeHistory.removeFirst();
        }

        double sampleMean = 0;
        ListIterator<Long> iterator = timeHistory.listIterator();
        while (iterator.hasNext()) {
            sampleMean += iterator.next() / (10e9 * timeHistory.size());
        }

        double sampleVariance = 0;
        iterator = timeHistory.listIterator();
        double tmp;
        while (iterator.hasNext()) {
            tmp = iterator.next() / 10e9;
            sampleVariance +=  (tmp - sampleMean)*(tmp - sampleMean)/ timeHistory.size();
        }

        this.targetFunction.setAlpha(sampleMean);
        this.targetFunction.setSigma(sampleVariance);
    }


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


    private CompiledOrderBook createUserOrderBook(CompiledOrderBook oldOB, double optimalV) {
        //
        return new CompiledOrderBook();
    }


    public MinimizerResult getResult(CompiledOrderBook ob) {
        double maxV_t = calcMaxV_t(ob);

        long startTime = System.nanoTime();
        double optimalV = findOptimalV(ob, maxV_t);
        CompiledOrderBook userOB = createUserOrderBook(ob, optimalV);
        long time = System.nanoTime() - startTime;
        timeHistory.addLast(time);
        updateTargetFunctionParams();

        return new MinimizerResult(optimalV, time, userOB);
    }

    public void cleanTimeHistory() {
        this.timeHistory = new LinkedList<>();
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

    public double getTradeRatePerSecond() {
        return tradeRatePerSecond;
    }

    public void setTradeRatePerSecond(double tradeRatePerSecond) {
        this.tradeRatePerSecond = tradeRatePerSecond;
    }
}
