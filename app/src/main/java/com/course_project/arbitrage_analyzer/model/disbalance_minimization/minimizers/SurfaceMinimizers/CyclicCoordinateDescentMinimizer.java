package com.course_project.arbitrage_analyzer.model.disbalance_minimization.minimizers.SurfaceMinimizers;

import com.course_project.arbitrage_analyzer.model.disbalance_minimization.minimizers.target_functions.TargetFunction;

import java.util.ArrayList;
import java.util.List;

public class CyclicCoordinateDescentMinimizer extends SurfaceMinimizer {


    public CyclicCoordinateDescentMinimizer(short maxRoundsCount, short timeHistoryMaxLength
            , int maxIterationsCount) {

        super(maxRoundsCount, timeHistoryMaxLength, maxIterationsCount);
    }


    private List<Double> cyclCoordDescent(TargetFunction tf, List<Double> startPoint,
                                                double precision,
                                                double maxIter,
                                                double lBorder,
                                                double rBorder) {

        List<Double> x = new ArrayList<>(startPoint);
        List<Double> y = new ArrayList<>(startPoint);

        int k = 1;
        int j;

        while (k <= maxIter) {

            j = 0;

            while (j < startPoint.size()) {

                int secondParamInd = (j == 0) ? 1:0;
                double newY_j = singDemMinimization(tf, j, y.get(secondParamInd), lBorder, rBorder, precision, maxIter);
                y.set(j, newY_j);
                j++;
            }

            if (avgSquare(x, y) < precision) {
                break;
            }
            x = new ArrayList<>(y);
            k++;
        }

        return y;
    }


    @Override
    List<Double> getSingleLaunchResult(TargetFunction tf
            , List<Double> startPoint
            , double precision
            , double maxIter
            , double lBorder
            , double rBorder) {

        return cyclCoordDescent(tf, startPoint, precision, maxIter, lBorder, rBorder);
    }

}
