package com.course_project.arbitrage_analyzer.model.disbalance_minimization.minimizers.table_minimizers;

import com.course_project.arbitrage_analyzer.model.CompiledOrderBook;
import com.course_project.arbitrage_analyzer.model.disbalance_minimization.minimizers.DisbalanceMinimizer;
import com.course_project.arbitrage_analyzer.model.disbalance_minimization.minimizers.target_functions.TableTargetFunction;

import java.util.ArrayList;
import java.util.List;

abstract class TableMinimizer extends DisbalanceMinimizer {

    TableMinimizer(short timeHistoryMaxLength, double riskConst) {
        super(new TableTargetFunction(riskConst), (short)-1, timeHistoryMaxLength, -1);
    }


    List<Double> calcPossibleVPoints(CompiledOrderBook obO) {

        CompiledOrderBook ob = obO.clone();

        List<Double> possiblePoints = new ArrayList<>();
        double curV = 0.0;
        int bx = 0, ax = 0;

        while ((ax < ob.getAsks().size())
                && (bx < ob.getBids().size())
                //While we can make profit from the deal.
                && (ob.getBids().get(bx).getPrice() > ob.getAsks().get(ax).getPrice())) {

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
            curV += ob.getAsks().get(ax).getPrice() * m;
            possiblePoints.add(curV);

            Double oldBidAmount = ob.getBids().get(bx).getAmount();
            Double oldAskAmount = ob.getAsks().get(ax).getAmount();
            ob.getBids().get(bx).setAmount(oldBidAmount - m);
            ob.getAsks().get(ax).setAmount(oldAskAmount - m);
        }
        return possiblePoints;
    }

    List<List<Double>> formTable(List<Double> possiblePoints) {

        List<List<Double>> table = new ArrayList<>();
        for (int i = 0; i < possiblePoints.size(); ++i) {
            List<Double> row = new ArrayList<>();
            for (int j = 0; j < possiblePoints.size(); ++j) {
                row.add(targetFunction.calculate(possiblePoints.get(i), possiblePoints.get(j)));
            }
            table.add(row);
        }

        return table;
    }
}
