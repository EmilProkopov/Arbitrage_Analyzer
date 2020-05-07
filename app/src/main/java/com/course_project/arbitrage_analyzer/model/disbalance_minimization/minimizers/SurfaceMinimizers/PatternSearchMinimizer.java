package com.course_project.arbitrage_analyzer.model.disbalance_minimization.minimizers.SurfaceMinimizers;

import com.course_project.arbitrage_analyzer.model.disbalance_minimization.minimizers.target_functions.TargetFunction;

import java.util.ArrayList;
import java.util.List;

public class PatternSearchMinimizer extends SurfaceMinimizer {


    public PatternSearchMinimizer(short maxRoundsCount, short timeHistoryMaxLength
            , int maxIterationsCount) {

        super(maxRoundsCount, timeHistoryMaxLength, maxIterationsCount);
    }


    private double getBrazSearchTargFuncValue(TargetFunction tf , List<Double> curC, List<Double> s, double alpha) {
        return tf.calculate(curC.get(0) + alpha*s.get(0), curC.get(1) + alpha*s.get(1));
    }


    private double brazSearchSingDemMinimization(TargetFunction tf , List<Double> curY, List<Double> s, double precision, double maxIter) {

        double curLB = 1e6;
        double curRB = -1e6;
        double curAlpha = (curRB+curLB)/2;
        double curIter = 0;

        double l;
        double r;

        double lFuncVal;
        double rFuncVal;

        while (curRB - curLB > precision) {
            l = curAlpha - precision;
            r = curAlpha + precision;

            curIter++;
            if (curIter > maxIter) {
                break;
            }

            lFuncVal = getBrazSearchTargFuncValue(tf, curY, s, l);
            rFuncVal = getBrazSearchTargFuncValue(tf, curY, s, r);

            if (lFuncVal > rFuncVal) {
                curLB = l;
            } else {
                curRB = r;
            }

            curAlpha = (curRB+curLB)/2;
        }

        return curAlpha;
    }



    private List<Double> patternSearch(TargetFunction tf, List<Double> startPoint, double precision, double maxIter, double lBorder, double rBorder) {

        List<Double> x = new ArrayList<>(startPoint);
        List<Double> y = new ArrayList<>(startPoint);
        List<Double> s = new ArrayList<>(startPoint);

        int k = 1;
        int j;
        double alpha;

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

            s.set(0, y.get(0)-x.get(0));
            s.set(1, y.get(1)-x.get(1));

            alpha = brazSearchSingDemMinimization(tf, y, s, precision, maxIter);
            y.set(0, y.get(0) + alpha*s.get(0));
            y.set(1, y.get(1) + alpha*s.get(1));

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

        return patternSearch(tf, startPoint, precision, maxIter, lBorder, rBorder);
    }

}
