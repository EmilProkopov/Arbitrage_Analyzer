package com.course_project.arbitrage_analyzer.model;


import com.course_project.arbitrage_analyzer.model.disbalance_minimization.EstimatorResult;
import com.course_project.arbitrage_analyzer.model.disbalance_minimization.MinimizerResult;

import java.util.ArrayList;
import java.util.List;

//Set of data collected in doInBackground() of SoloAsyncTask.
public class OutputDataSet {

    private List<Double> amountPoints;
    private List<Double> profitPoints;
    private List<Deal> deals;
    private Double profit;
    private MinimizerResult minimizerResult;
    private Double optimalProfit;

    private EstimatorResult estimate;
    private Double realFirstCurrencyAmount;
    private Double realProfit;

    private String firstCurrency;
    private String secondCurrency;
    private Double firstCurrencyAmount;
    private Double secondCurrencyAmount;

    private List<Double> bidAmountPoints;
    private List<Double> askAmountPoints;
    private List<Double> bidPricePoints;
    private List<Double> askPricePoints;

    private static final double impossiblyHugePrice = 1e9;

    public OutputDataSet() {
        amountPoints = new ArrayList<>();
        profitPoints = new ArrayList<>();
        deals = new ArrayList<>();
        profit = 0.0;
        minimizerResult = new MinimizerResult(0.0, 0.0, 0L
                , new CompiledOrderBook());
        optimalProfit = 0.0;
        realFirstCurrencyAmount = 0.0;
        estimate = new EstimatorResult(0.0, 0.0);
        realProfit = 0.0;
        firstCurrencyAmount = 0.0;
        secondCurrencyAmount = 0.0;
        bidAmountPoints = new ArrayList<>();
        askAmountPoints = new ArrayList<>();
        askPricePoints = new ArrayList<>();
        bidPricePoints = new ArrayList<>();
    }

    //Unite deals of same type made on same market into one deal.
    public void uniteDealsMadeOnSameMarkets() {

        ArrayList<Deal> newDealList = new ArrayList<>();
        //List of market names.
        ArrayList<String> marketNames = new ArrayList<>();
        //Fill list of market names.
        for (int i = 0; i < deals.size(); ++i) {
            if (!marketNames.contains(deals.get(i).getMarketName())) {
                marketNames.add(deals.get(i).getMarketName());
            }
        }
        //Unite "Buy" deals.
        for (int i = 0; i < marketNames.size(); ++i) {

            Deal curDeal = new Deal(DealType.BUY, marketNames.get(i), 0.0, 0.0);

            for (int j = 0; j < deals.size(); ++j) {
                if (deals.get(j).getMarketName().equals(marketNames.get(i))
                        && deals.get(i).getType().equals(DealType.BUY)) {

                    curDeal.setAmount(curDeal.getAmount() + deals.get(i).getAmount());
                    curDeal.setPrice(Math.max(curDeal.getPrice(), deals.get(i).getPrice()));
                }
            }
            //
            if (!curDeal.getPrice().equals(0.0) && !curDeal.getAmount().equals(0.0)) {
                newDealList.add(curDeal);
            }
        }
        //Unite "Sell" deals.
        for (int i = 0; i < marketNames.size(); ++i) {

            Deal curDeal = new Deal(DealType.SELL, marketNames.get(i), 0.0, impossiblyHugePrice);

            for (int j = 0; j < deals.size(); ++j) {
                if (deals.get(j).getMarketName().equals(marketNames.get(i))
                        && deals.get(i).getType().equals(DealType.SELL)) {

                    curDeal.setAmount(curDeal.getAmount() + deals.get(i).getAmount());
                    curDeal.setPrice(Math.min(curDeal.getPrice(), deals.get(i).getPrice()));
                }
            }

            if (!curDeal.getPrice().equals(impossiblyHugePrice) && !curDeal.getAmount().equals(0.0)) {
                newDealList.add(curDeal);
            }
        }

        //Total amount of "Buy" deals may be not equal to amount of "Sell" deals due to rounding.
        //This should be fixed.
        double buyAmount = 0;
        double sellAmount = 0;
        //Calculate buy and sell amounts.
        for (int i = 0; i < newDealList.size(); ++i) {
            if (newDealList.get(i).getType().equals(DealType.BUY)) {
                buyAmount += newDealList.get(i).getAmount();
            } else {
                sellAmount += newDealList.get(i).getAmount();
            }
        }

        if (buyAmount > sellAmount) {
            Double disbalanse = buyAmount - sellAmount;
            for (int i = 0; i < newDealList.size(); ++i) {
                if (newDealList.get(i).getType().equals(DealType.SELL)) {
                    Double curDealBuyAmount = newDealList.get(i).getAmount();
                    if (curDealBuyAmount >= disbalanse) {
                        newDealList.get(i).setAmount(curDealBuyAmount - disbalanse);
                        break;
                    } else {
                        newDealList.get(i).setAmount(0.0);
                        disbalanse -= curDealBuyAmount;
                        newDealList.remove(i);
                        i--;
                    }
                }
            }
        } else if (buyAmount < sellAmount) {
            Double disbalanse = sellAmount - buyAmount;
            for (int i = 0; i < newDealList.size(); ++i) {
                if (newDealList.get(i).getType().equals(DealType.SELL)) {
                    Double curDealSellAmount = newDealList.get(i).getAmount();
                    if (curDealSellAmount >= disbalanse) {
                        newDealList.get(i).setAmount(curDealSellAmount - disbalanse);
                        break;
                    } else {
                        newDealList.get(i).setAmount(0.0);
                        disbalanse -= curDealSellAmount;
                        newDealList.remove(i);
                        i--;
                    }
                }
            }
        }

        deals = newDealList;
    }

    public List<Double> getAmountPoints() {
        return amountPoints;
    }

    public void setAmountPoints(List<Double> amountPoints) {
        this.amountPoints = amountPoints;
    }

    public List<Double> getProfitPoints() {
        return profitPoints;
    }

    public void setProfitPoints(List<Double> profitPoints) {
        this.profitPoints = profitPoints;
    }

    public List<Deal> getDeals() {
        return deals;
    }

    public void setDeals(List<Deal> deals) {
        this.deals = deals;
    }


    public Double getProfit() {
        return profit;
    }

    public void setProfit(Double profit) {
        this.profit = profit;
    }

    public Double getOptimalProfit() {
        return optimalProfit;
    }

    public void setOptimalProfit(Double optimalProfit) {
        this.optimalProfit = optimalProfit;
    }

    public String getSecondCurrency() {
        return secondCurrency;
    }

    public void setSecondCurrency(String secondCurrency) {
        this.secondCurrency = secondCurrency;
    }

    public String getFirstCurrency() {
        return firstCurrency;
    }

    public void setFirstCurrency(String firstCurrency) {
        this.firstCurrency = firstCurrency;
    }

    public Double getFirstCurrencyAmount() {
        return firstCurrencyAmount;
    }

    public void setFirstCurrencyAmount(Double firstCurrencyAmount) {
        this.firstCurrencyAmount = firstCurrencyAmount;
    }

    public Double getSecondCurrencyAmount() {
        return secondCurrencyAmount;
    }

    public void setSecondCurrencyAmount(Double secondCurrencyAmount) {
        this.secondCurrencyAmount = secondCurrencyAmount;
    }

    public List<Double> getBidAmountPoints() {
        return bidAmountPoints;
    }

    public void setBidAmountPoints(List<Double> bidAmountPoints) {
        this.bidAmountPoints = bidAmountPoints;
    }

    public List<Double> getAskAmountPoints() {
        return askAmountPoints;
    }

    public void setAskAmountPoints(List<Double> askAmountPoints) {
        this.askAmountPoints = askAmountPoints;
    }

    public List<Double> getBidPricePoints() {
        return bidPricePoints;
    }

    public void setBidPricePoints(List<Double> bidPricePoints) {
        this.bidPricePoints = bidPricePoints;
    }

    public List<Double> getAskPricePoints() {
        return askPricePoints;
    }

    public void setAskPricePoints(List<Double> askPricePoints) {
        this.askPricePoints = askPricePoints;
    }

    public Double getRealFirstCurrencyAmount() {
        return realFirstCurrencyAmount;
    }

    public void setRealFirstCurrencyAmount(Double realFirstCurrencyAmount) {
        this.realFirstCurrencyAmount = realFirstCurrencyAmount;
    }

    public Double getRealProfit() {
        return realProfit;
    }

    public void setRealProfit(Double realProfit) {
        this.realProfit = realProfit;
    }

    public MinimizerResult getMinimizerResult() {
        return minimizerResult;
    }

    public void setMinimizerResult(MinimizerResult minimizerResult) {
        this.minimizerResult = minimizerResult;
    }

    public EstimatorResult getEstimate() {
        return estimate;
    }

    public void setEstimate(EstimatorResult estimate) {
        this.estimate = estimate;
    }
}
