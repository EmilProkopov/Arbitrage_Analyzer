package com.course_project.arbitrage_analyzer.model.disbalance_minimization.minimizers.table_minimizers;

import com.course_project.arbitrage_analyzer.model.CompiledOrderBook;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BayesLaplaceMinimizer extends TableMinimizer {

    public BayesLaplaceMinimizer(short timeHistoryMaxLength, double riskConst) {
        super(timeHistoryMaxLength, riskConst);
    }


    @Override
    public double findOptimalV(CompiledOrderBook ob, double maxV_t) {

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

        Double max = Collections.max(estimates);
        int index = estimates.indexOf(max);
        return possiblePoints.get(index);
    }
}
