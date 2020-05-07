package com.course_project.arbitrage_analyzer.model;

import java.util.ArrayList;
import java.util.List;

public class Util {

    private static List<PriceAmountPair> clonePAPList(List<PriceAmountPair> l) {
        List<PriceAmountPair> cloned = new ArrayList<>();
        for (PriceAmountPair pair : l) {
            cloned.add(pair.clone());
        }

        return cloned;
    }

    public static double calculateOBOverlapV(List<PriceAmountPair> asksO, List<PriceAmountPair> bidsO) {

        List<PriceAmountPair> asks = clonePAPList(asksO);
        List<PriceAmountPair> bids = clonePAPList(bidsO);

        double deltaAmount;
        double maxV_t = 0;

        int askInd = 0;
        int bidInd = 0;

        while ((askInd < asks.size()) && (bidInd < bids.size())
                && (asks.get(askInd).getPrice() <= bids.get(bidInd).getPrice())) {


            double bidAmount = bids.get(bidInd).getAmount();
            double askAmount = asks.get(askInd).getAmount();

            if (asks.get(askInd).getAmount() == 0.0) {
                askInd++;
                continue;
            }
            if (bids.get(bidInd).getAmount() == 0.0) {
                bidInd++;
                continue;
            }

            if (askAmount > bidAmount) {
                deltaAmount = bidAmount;
            }
            else {
                deltaAmount = askAmount;
            }

            asks.get(askInd).setAmount(askAmount - deltaAmount);
            bids.get(bidInd).setAmount(bidAmount - deltaAmount);

            maxV_t += asks.get(askInd).getPrice() * deltaAmount;
        }

        return maxV_t;
    }


    public static double calculateOBOverlapAmount(List<PriceAmountPair> asksO
                                                , List<PriceAmountPair> bidsO) {
        return calculateOBOverlapAmount(asksO, bidsO, false);
    }

    public static double calculateOBOverlapAmount(List<PriceAmountPair> asksO
            , List<PriceAmountPair> bidsO
            , boolean strictInequality) {

        return calculateOBOverlapAmount(asksO, bidsO, strictInequality, null);
    }


    public static double calculateOBOverlapAmount(List<PriceAmountPair> asksO
                                                , List<PriceAmountPair> bidsO
                                                , boolean strictInequality
                                                , Double VLimit) {

        List<PriceAmountPair> asks = clonePAPList(asksO);
        List<PriceAmountPair> bids = clonePAPList(bidsO);

        double deltaAmount;
        double curAmount = 0;
        double curV = 0.0;

        int askInd = 0;
        int bidInd = 0;

        while ((askInd < asks.size()) && (bidInd < bids.size())
                && (asks.get(askInd).getPrice() <= bids.get(bidInd).getPrice())) {

            if (strictInequality) {
                if (asks.get(askInd).getPrice() >= bids.get(bidInd).getPrice()) {
                    break;
                }
            }

            double bidAmount = bids.get(bidInd).getAmount();
            double askAmount = asks.get(askInd).getAmount();

            if (asks.get(askInd).getAmount() == 0.0) {
                askInd++;
                continue;
            }
            if (bids.get(bidInd).getAmount() == 0.0) {
                bidInd++;
                continue;
            }

            if (askAmount > bidAmount) {
                deltaAmount = bidAmount;
            }
            else {
                deltaAmount = askAmount;
            }

            if ((VLimit != null) && (curV + deltaAmount > VLimit)) {
                deltaAmount = (VLimit - curV > 0) ? deltaAmount*(VLimit - curV)/VLimit : 0;
                curAmount += deltaAmount;
                break;
            }

            asks.get(askInd).setAmount(askAmount - deltaAmount);
            bids.get(bidInd).setAmount(bidAmount - deltaAmount);

            curAmount += deltaAmount;
            curV += asks.get(askInd).getPrice() * deltaAmount;
        }

        return curAmount;
    }
}
