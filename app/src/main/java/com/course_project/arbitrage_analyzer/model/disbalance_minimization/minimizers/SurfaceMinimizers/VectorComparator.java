package com.course_project.arbitrage_analyzer.model.disbalance_minimization.minimizers.SurfaceMinimizers;

import com.course_project.arbitrage_analyzer.model.disbalance_minimization.minimizers.target_functions.TargetFunction;

import java.util.ArrayList;
import java.util.Comparator;

public class VectorComparator implements Comparator<ArrayList<Double>> {

    private TargetFunction tf;

    VectorComparator(TargetFunction tf) {
        this.tf = tf;
    }

    @Override
    public int compare(ArrayList<Double> o1, ArrayList<Double> o2) {
        return (int)Math.signum(tf.calculate(o1.get(0), o1.get(1)) - tf.calculate(o2.get(0), o2.get(1)));
    }
}