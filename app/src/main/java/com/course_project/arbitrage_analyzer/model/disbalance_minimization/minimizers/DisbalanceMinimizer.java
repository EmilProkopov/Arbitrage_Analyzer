package com.course_project.arbitrage_analyzer.model.disbalance_minimization.minimizers;

import com.course_project.arbitrage_analyzer.model.CompiledOrderBook;
import com.course_project.arbitrage_analyzer.model.PriceAmountPair;
import com.course_project.arbitrage_analyzer.model.Util;
import com.course_project.arbitrage_analyzer.model.disbalance_minimization.MinimizerResult;
import com.course_project.arbitrage_analyzer.model.disbalance_minimization.minimizers.target_functions.TargetFunction;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public abstract class DisbalanceMinimizer {

    protected TargetFunction targetFunction;
    private double tradeRatePerSecond = 1;
    private short maxRoundsCount;
    private int maxIterationsCount;
    private short timeHistoryMaxLength;
    private double alpha;
    private double sigma;

    private LinkedList<Long> timeHistory;

    public DisbalanceMinimizer(TargetFunction targetFunction, short maxRoundsCount
                        , short timeHistoryMaxLength, int maxIterationsCount) {

        this.targetFunction = targetFunction;
        this.maxRoundsCount = maxRoundsCount;
        this.timeHistory = new LinkedList<>();
        this.timeHistoryMaxLength = timeHistoryMaxLength;
        this.maxIterationsCount = maxIterationsCount;
        this.alpha = 10;
        this.sigma = 1;
    }


    abstract public double findOptimalV(CompiledOrderBook ob, double maxV_t);


    public double gaussian(double x) {

        if (sigma == 0) {
            return alpha;
        }
        return Math.exp(-(x - alpha)*(x - alpha) / (2 * sigma*sigma))
                / (sigma * Math.sqrt(2*Math.PI));
    }


    protected double q(double v_t) {
        double seconds = v_t / tradeRatePerSecond;
        double g = gaussian(seconds);
        return g;
    }


    private void updateTargetFunctionParams() {

        if (targetFunction == null) {
            return;
        }
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

        alpha = sampleMean;
        sigma = sampleVariance;
    }


    private CompiledOrderBook createUserOrderBook(CompiledOrderBook oldOB, double optimalV) {

        List<PriceAmountPair> userAsks = new ArrayList<>();
        List<PriceAmountPair> userBids = new ArrayList<>();

        List<PriceAmountPair> asks = oldOB.getAsks();
        List<PriceAmountPair> bids = oldOB.getBids();

        double curV_t = 0;

        int askInd = 0;
        int bidInd = 0;

        while (    (askInd < asks.size())
                && (bidInd < bids.size())
                && (asks.get(askInd).getPrice() < bids.get(bidInd).getPrice())
                && curV_t <= optimalV) {


            Double bidAmount = bids.get(bidInd).getAmount();
            Double askAmount = asks.get(askInd).getAmount();

            if (bidAmount.equals(0.0)) {
                bidInd += 1;
                continue;
            }
            if (askAmount.equals(0.0)) {
                askInd += 1;
                continue;
            }

            Double m = Math.min(bidAmount, askAmount);
            if (curV_t + asks.get(askInd).getPrice() * m >= optimalV) {
                double deltaSecond = curV_t + asks.get(askInd).getPrice() * m - optimalV;
                curV_t += asks.get(askInd).getPrice() * deltaSecond;
            } else {
                curV_t += asks.get(askInd).getPrice() * m;
            }

            userAsks.add(new PriceAmountPair(bids.get(bidInd).getPrice(), m, bids.get(bidInd).getMarketName()));
            userBids.add(new PriceAmountPair(asks.get(askInd).getPrice(), m, asks.get(askInd).getMarketName()));

            Double oldBidAmount = bids.get(bidInd).getAmount();
            Double oldAskAmount = asks.get(askInd).getAmount();
            bids.get(bidInd).setAmount(oldBidAmount - m);
            asks.get(askInd).setAmount(oldAskAmount - m);
        }

        // What to do if overlap optimalV
        CompiledOrderBook userOB = new CompiledOrderBook();

        if (userAsks.size() > 0) {
            double minAskPrice = userAsks.get(0).getPrice();
            for (PriceAmountPair order : userAsks) {
                minAskPrice = Math.min(minAskPrice, order.getPrice());
            }
            for (PriceAmountPair order : userAsks) {
                order.setPrice(minAskPrice);
            }
        }

        if (userBids.size() > 0) {
            double maxBidPrice = userBids.get(0).getPrice();
            for (PriceAmountPair order : userBids) {
                maxBidPrice = Math.max(maxBidPrice, order.getPrice());
            }
            for (PriceAmountPair order : userBids) {
                order.setPrice(maxBidPrice);
            }
        }

        userOB.setBids(userBids);
        userOB.setAsks(userAsks);

        return userOB;
    }


    public MinimizerResult getResult(CompiledOrderBook ob) {

        CompiledOrderBook obCopy1 = ob.clone();
        CompiledOrderBook obCopy2 = ob.clone();
        CompiledOrderBook obCopy3 = ob.clone();
        double maxV_t = Util.calculateOBOverlapV(obCopy1.getAsks(), obCopy1.getBids());

        long startTime = System.nanoTime();
        double optimalV = findOptimalV(obCopy2, maxV_t);
        CompiledOrderBook userOB = createUserOrderBook(ob, optimalV);
        long time = System.nanoTime() - startTime;
        timeHistory.addLast(time);
        updateTargetFunctionParams();

        double optimalAmount = Util.calculateOBOverlapAmount(obCopy3.getAsks(), obCopy3.getBids()
                , true, optimalV);

        return new MinimizerResult(optimalV, optimalAmount, time, userOB);
    }

    public void cleanTimeHistory() {
        this.timeHistory = new LinkedList<>();
    }


    protected TargetFunction getTargetFunction() {
        return targetFunction;
    }

    public void setTargetFunction(TargetFunction targetFunction) {
        this.targetFunction = targetFunction;
    }

    protected short getMaxRoundsCount() {
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

    protected int getMaxIterationsCount() { return maxIterationsCount; }
}
