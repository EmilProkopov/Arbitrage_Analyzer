package com.course_project.arbitrage_analyzer.model.disbalance_minimization.minimizers;

import com.course_project.arbitrage_analyzer.model.CompiledOrderBook;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExpectedRegretMinimizer extends TableMinimizer {

    public ExpectedRegretMinimizer(short timeHistoryMaxLength, double riskConst) {
        super(timeHistoryMaxLength, riskConst);
    }


    private List<Double> findMaxColsEl(List<List<Double>> table) {

        List<Double> maxEls = new ArrayList<>(table.size());

        for(int j = 0; j < table.size(); ++j) {
            double tmp = table.get(0).get(j);
            for (int i = 0; i < table.size(); ++i) {
                double item = table.get(i).get(j);
                tmp = Math.max(tmp, item);
            }
            maxEls.add(tmp);
        }

        return maxEls;
    }


    private List<List<Double>> formDerivateTable(List<Double> possiblePoints, List<List<Double>> table) {

        List<List<Double>> derivTable = new ArrayList<>();

        List<Double> maxEls = findMaxColsEl(table);

        for (int i = 0; i < possiblePoints.size(); ++i) {
            List<Double> row = new ArrayList<>();
            for (int j = 0; j < possiblePoints.size(); ++j) {
                row.add(maxEls.get(j) - table.get(i).get(j));
            }
            derivTable.add(row);
        }

        return derivTable;
    }


    @Override
    double findOptimalV(CompiledOrderBook ob, double maxV_t) {

        List<Double> possiblePoints = calcPossibleVPoints(ob);
        List<List<Double>> table = formTable(possiblePoints);

        List<Double> estimates = new ArrayList<>(possiblePoints.size());
        for(int j = 0; j < table.size(); ++j) {
            double tmp = 0;
            for (int i = 0; i < table.size(); ++i) {
                double item = table.get(i).get(j);
                tmp += q(possiblePoints.get(j)) * item;
            }
            estimates.add(tmp);
        }

        Double min = Collections.min(estimates);
        int index = estimates.indexOf(min);
        return possiblePoints.get(index);
    }
}
