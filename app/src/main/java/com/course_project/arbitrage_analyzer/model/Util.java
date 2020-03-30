package com.course_project.arbitrage_analyzer.model;

import java.util.List;

public class Util {

    public static double calculateOBOverlapV(List<PriceAmountPair> asks, List<PriceAmountPair> bids) {
        double deltaAmount;
        double maxV_t = 0;

        int askInd = 0;
        int bidInd = 0;

        while ((askInd < asks.size()) && (bidInd < bids.size())
                && (asks.get(askInd).getPrice() < bids.get(bidInd).getPrice())) {


            double bidAmount = bids.get(bidInd).getAmount();
            double askAmount = asks.get(askInd).getAmount();

            if (askAmount > bidAmount) {
                deltaAmount = bidAmount;
                bidInd++;
                asks.get(askInd).setAmount(askAmount - deltaAmount);
            }
            else if (askAmount < bidAmount) {
                deltaAmount = askAmount;
                askInd++;
                bids.get(bidInd).setAmount(bidAmount - deltaAmount);
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
}
