package com.course_project.arbitrage_analyzer.model.disbalance_minimization;

import com.course_project.arbitrage_analyzer.model.CompiledOrderBook;
import com.course_project.arbitrage_analyzer.model.Util;

public class DisbalanceEstimator {

    // Returns sum of amounts of orders that do overlap
    public double getEstimate(CompiledOrderBook actualOrderBook, CompiledOrderBook userOrders) {

        double v1 = Util.calculateOBOverlapAmount(actualOrderBook.getAsks(), userOrders.getBids());
        double v2 = Util.calculateOBOverlapAmount(userOrders.getAsks(), actualOrderBook.getBids());
        return v1 + v2;
    }
}
