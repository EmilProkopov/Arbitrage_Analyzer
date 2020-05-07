package com.course_project.arbitrage_analyzer.model.disbalance_minimization.minimizers;

import com.course_project.arbitrage_analyzer.model.CompiledOrderBook;

public class SimpleMinimizer extends DisbalanceMinimizer {

    public SimpleMinimizer() {
        super(null, (short)-1, (short)1, -1);
    }

    @Override
    public double findOptimalV(CompiledOrderBook ob, double maxV_t) {

        int bx = 0, ax = 0;
        Integer num = -1;   //Number of deals to make
        Double prevAmount = 0.0;
        Double prevProfit = 0.0;
        Double firstK = -1.0; //First value of K.
        Double profit = 0.0; //Profit that we can get.
        Double secondCurrencyAmount = 0.0;
        Double curK; //Current K.
        final double alpha = 0.1;
        double optimalV = -1;

        while ((ax < ob.getAsks().size())
                && (bx < ob.getBids().size())
                //While we can make profit from the deal.
                && (ob.getBids().get(bx).getPrice() > ob.getAsks().get(ax).getPrice())) {

            num += 1;

            Double bidAmount = ob.getBids().get(bx).getAmount(); //Amount of top bid.
            Double askAmount = ob.getAsks().get(ax).getAmount(); //Amount of top ask.
            if (bidAmount.equals(0.0)) {
                bx += 1;
                continue;
            }
            if (askAmount.equals(0.0)) {
                ax += 1;
                continue;
            }
            //Amount of currency to buy (sell).
            Double m = Math.min(bidAmount, askAmount);

            Double currentProfit = (ob.getBids().get(bx).getPrice()
                    - ob.getAsks().get(ax).getPrice()) * m;

            profit += currentProfit;
            secondCurrencyAmount += ob.getAsks().get(ax).getPrice() * m;

            //Take into account that we have made a deal and top bid and ask are changed.
            Double oldBidAmount = ob.getBids().get(bx).getAmount();
            Double oldAskAmount = ob.getAsks().get(ax).getAmount();
            ob.getBids().get(bx).setAmount(oldBidAmount - m);
            ob.getAsks().get(ax).setAmount(oldAskAmount - m);

            //Check if we have achieved the optimal point.
            if (num.equals(2)) {
                firstK = (profit - prevProfit) / (secondCurrencyAmount - prevAmount);
            } else if (num > 1) {
                curK = (profit - prevProfit) / (secondCurrencyAmount - prevAmount);
                if (curK / firstK >= alpha) {
                    optimalV = secondCurrencyAmount;
                }
            }
            prevAmount = secondCurrencyAmount;
            prevProfit = profit;
        }
        if (optimalV == -1) {
            optimalV = secondCurrencyAmount;
        }
        return optimalV;
    }
}
