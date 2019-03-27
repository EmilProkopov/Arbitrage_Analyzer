package com.course_project.arbitrage_analyzer.model;


import java.util.ArrayList;
import java.util.List;

//Set of data collected in doInBackground() of SoloAsyncTask.
public class OutputDataSet {

    private List<Double> amountPoints;
    private List<Double> profitPoints;
    private List<Deal> deals;
    private Double profit;
    private Double amount;
    private Double optimalAmount;
    private Double optimalProfit;

    private static final double impossiblyHugePrice = 1e9;

    public OutputDataSet() {
        amountPoints = new ArrayList<>();
        profitPoints = new ArrayList<>();
        deals = new ArrayList<>();
        profit = 0.0;
        optimalAmount = 0.0;
        optimalProfit = 0.0;
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

            Deal curDeal = new Deal("Buy", marketNames.get(i), 0.0, 0.0);

            for (int j = 0; j < deals.size(); ++j) {
                if (deals.get(j).getMarketName().equals(marketNames.get(i))
                        && deals.get(i).getType().equals("Buy")) {

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

            Deal curDeal = new Deal("Sell", marketNames.get(i), 0.0, impossiblyHugePrice);

            for (int j = 0; j < deals.size(); ++j) {
                if (deals.get(j).getMarketName().equals(marketNames.get(i))
                        && deals.get(i).getType().equals("Sell")) {

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
            if (newDealList.get(i).getType().equals("Buy")) {
                buyAmount += newDealList.get(i).getAmount();
            } else {
                sellAmount += newDealList.get(i).getAmount();
            }
        }

        if (buyAmount > sellAmount) {
            Double disbalanse = buyAmount - sellAmount;
            for (int i = 0; i < newDealList.size(); ++i) {
                if (newDealList.get(i).getType().equals("Buy")) {
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
                if (newDealList.get(i).getType().equals("Sell")) {
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

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
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

    public Double getOptimalAmount() {
        return optimalAmount;
    }

    public void setOptimalAmount(Double optimalAmount) {
        this.optimalAmount = optimalAmount;
    }

    public Double getOptimalProfit() {
        return optimalProfit;
    }

    public void setOptimalProfit(Double optimalProfit) {
        this.optimalProfit = optimalProfit;
    }

}
