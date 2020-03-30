package com.course_project.arbitrage_analyzer.model.disbalance_minimization;

import com.course_project.arbitrage_analyzer.model.CompiledOrderBook;
import com.course_project.arbitrage_analyzer.model.PriceAmountPair;
import com.course_project.arbitrage_analyzer.model.Util;

import java.util.ArrayList;
import java.util.List;

public class DisbalanceEstimator {

    private List<CompiledOrderBook> splitOBbyMarketNames(CompiledOrderBook ob) {

        List<CompiledOrderBook> obList = new ArrayList<>();

        ArrayList<String> marketNames = new ArrayList<>();
        //Fill list of market names.
        for (int i = 0; i < ob.getBids().size(); ++i) {
            String name = ob.getBids().get(i).getMarketName();
            if (!marketNames.contains(name)) {
                marketNames.add(name);
            }
        }
        for (int i = 0; i < ob.getAsks().size(); ++i) {
            String name = ob.getAsks().get(i).getMarketName();
            if (!marketNames.contains(name)) {
                marketNames.add(name);
            }
        }

        for (String name : marketNames) {

            CompiledOrderBook curMarketOB = new CompiledOrderBook();
            for (int i = 0; i < ob.getBids().size(); ++i) {
                PriceAmountPair curOrder = ob.getBids().get(i);
                if (name.equals(curOrder.getMarketName())) {
                    curMarketOB.getBids().add(curOrder.clone());
                }
            }
            for (int i = 0; i < ob.getAsks().size(); ++i) {
                PriceAmountPair curOrder = ob.getAsks().get(i);
                if (name.equals(curOrder.getMarketName())) {
                    curMarketOB.getAsks().add(curOrder.clone());
                }
            }

            obList.add(curMarketOB);
        }

        return obList;
    }

    // Returns sum of amounts of orders that do overlap
    public double getEstimate(CompiledOrderBook actualOrderBook, CompiledOrderBook userOrders) {

        List<CompiledOrderBook> actualOBList = splitOBbyMarketNames(actualOrderBook);
        List<CompiledOrderBook> userOBList = splitOBbyMarketNames(userOrders);

        double res = 0.0;

        for (CompiledOrderBook curActualOB : actualOBList) {
            for (CompiledOrderBook curUserOB : userOBList) {

                String actualOBName = "";
                if (curActualOB.getBids().size() > 0) {
                    actualOBName = curActualOB.getBids().get(0).getMarketName();
                } else if (curActualOB.getAsks().size() > 0) {
                    actualOBName = curActualOB.getAsks().get(0).getMarketName();
                }

                String userOBName = "";
                if (curUserOB.getBids().size() > 0) {
                    userOBName = curUserOB.getBids().get(0).getMarketName();
                } else if (curUserOB.getAsks().size() > 0) {
                    userOBName = curUserOB.getAsks().get(0).getMarketName();
                }

                if (userOBName.equals("") || actualOBName.equals("")) {
                    continue;
                }

                if (userOBName.equals(actualOBName)) {

                    curActualOB.sort();
                    curUserOB.sort();
                    res += Util.calculateOBOverlapV(curActualOB.getAsks(), curUserOB.getBids());
                    res += Util.calculateOBOverlapV(curUserOB.getAsks(), curActualOB.getBids());
                }
            }
        }

        return res;
    }
}
