package com.course_project.arbitrage_analyzer.model.disbalance_minimization.minimizers.SurfaceMinimizers;

import com.course_project.arbitrage_analyzer.model.CompiledOrderBook;
import com.course_project.arbitrage_analyzer.model.disbalance_minimization.minimizers.DisbalanceMinimizer;
import com.course_project.arbitrage_analyzer.model.disbalance_minimization.minimizers.target_functions.TargetFunction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public abstract class SurfaceMinimizer extends DisbalanceMinimizer {

    SurfaceMinimizer(short maxRoundsCount, short timeHistoryMaxLength
            , int maxIterationsCount) {

        super(null, maxRoundsCount, timeHistoryMaxLength, maxIterationsCount);
    }


    abstract List<Double> getSingleLaunchResult(TargetFunction tf
            , List<Double> startPoint
            , double precision
            , double maxIter
            , double lBorder
            , double rBorder);



    double avgSquare (List<Double> a, List<Double> b) {

        double res = 0;
        for (int i = 0; i < a.size(); ++i) {
            double a_i = a.get(i);
            double b_i = b.get(i);
            res += (a_i-b_i)*(a_i-b_i);
        }

        return Math.sqrt(res);
    }


    private double getTargFuncValue (TargetFunction tf
            , int targetParamInd
            , double targParamVal
            , double secondParamVal) {

        if (targetParamInd == 0) {
            return tf.calculate(targParamVal, secondParamVal);
        } else {
            return tf.calculate(secondParamVal, targParamVal);
        }
    }


    double singDemMinimization(TargetFunction tf
            , int targetParamInd
            , double secondParamVal
            , double lBorder
            , double rBorder
            , double precision
            , double maxIter) {

        if (lBorder > rBorder) {
            double tmp = lBorder;
            lBorder = rBorder;
            rBorder = tmp;
        }

        double curLB = lBorder;
        double curRB = rBorder;
        double curC = (curRB+curLB)/2;
        double curIter = 0;

        double l;
        double r;

        double lFuncVal;
        double rFuncVal;

        while (curRB - curLB > precision) {
            l = curC - precision;
            r = curC + precision;

            curIter++;
            if (curIter > maxIter) {
                break;
            }

            lFuncVal = getTargFuncValue(tf, targetParamInd, l, secondParamVal);
            rFuncVal = getTargFuncValue(tf, targetParamInd, r, secondParamVal);

            if (lFuncVal > rFuncVal) {
                curLB = l;
            } else {
                curRB = r;
            }

            curC = (curRB+curLB)/2;
        }

        return curC;
    }


    private static List<List<Double>> generateStartPoints(double from, double to, int count) {

        List<List<Double>> startPoints = new ArrayList<>(count);
        Random rand = new Random();

        for (int i=0; i<count; ++i) {
            List<Double> point = new ArrayList<>(2);
            point.add(rand.nextDouble());
            point.add(rand.nextDouble());
            startPoints.add(point);
        }

        return startPoints;
    }


    @Override
    public double findOptimalV(CompiledOrderBook ob, double maxV_t) {

        List<List<Double>> startPoints = generateStartPoints(0, maxV_t, getMaxRoundsCount());
        List<List<Double>> resPoints = new ArrayList<>(getMaxRoundsCount());
        List<Double> resVals = new ArrayList<>(getMaxRoundsCount());

        for(int i = 0; i<startPoints.size(); ++i) {

            List<Double> sp = startPoints.get(i);
            List<Double> resP = getSingleLaunchResult(getTargetFunction()
                    , sp
                    , 1e-8
                    , getMaxIterationsCount()
                    , 0
                    , maxV_t);

            resPoints.add(resP);
            resVals.add(targetFunction.calculate(resP.get(0), resP.get(1)));
        }

        int minInd = resVals.indexOf(Collections.min(resVals));
        return resPoints.get(minInd).get(0);
    }
}
