package com.course_project.arbitrage_analyzer.model.disbalance_minimization.minimizers.SurfaceMinimizers;

import com.course_project.arbitrage_analyzer.model.disbalance_minimization.minimizers.target_functions.TargetFunction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NelderMeadMinimizer extends SurfaceMinimizer {


    public NelderMeadMinimizer(short maxRoundsCount, short timeHistoryMaxLength
            , int maxIterationsCount) {

        super(maxRoundsCount, timeHistoryMaxLength, maxIterationsCount);
    }



    private List<Double> nelderMead(TargetFunction tf, List<Double> startPoint, double precision, double maxIter) {

        int iterCount = 0;
        double alpha = 1;
        double beta = 0.5;
        double gamma = 2;
        VectorComparator comparator = new VectorComparator(tf);
        // step 1
        double x0 = startPoint.get(0);
        double y0 = startPoint.get(1);
        double x1 = x0+1;
        double y1 = y0;
        double x2 = x0+1;
        double y2 = y0+1;

        List<Double> ph = new ArrayList<>(2);
        ph.add(x0);
        ph.add(y0);
        List<Double> pg = new ArrayList<>(2);
        pg.add(x1);
        pg.add(y1);
        List<Double> pl = new ArrayList<>(2);
        pl.add(x2);
        pl.add(y2);

        Double fh, fg, fl, fr, fs;

        List<Double> pc = new ArrayList<>(2);
        pc.add(0.0);
        pc.add(0.0);

        List<Double> pr = new ArrayList<>(2);
        pr.add(0.0);
        pr.add(0.0);

        List<Double> ps = new ArrayList<>(2);
        ps.add(0.0);
        ps.add(0.0);

        while(iterCount < maxIter) {

            iterCount++;
            // step2
            List<ArrayList<Double>> points = new ArrayList<>();
            points.add(new ArrayList<>(ph));
            points.add(new ArrayList<>(pg));
            points.add(new ArrayList<>(pl));

            Collections.sort(points, comparator);

            ph = points.get(2);
            pg = points.get(1);
            pl = points.get(0);

            fh = tf.calculate(ph.get(0), ph.get(1));
            fg = tf.calculate(pg.get(0), pg.get(1));
            fl = tf.calculate(pl.get(0), pl.get(1));
            // step 3
            pc.set(0, (pg.get(0)+pl.get(0))/2);
            pc.set(1, (pg.get(1)+pl.get(1))/2);
            // step 4
            pr.set(0, pc.get(0) + alpha*(pc.get(0)-ph.get(0)));
            pr.set(1, pc.get(1) + alpha*(pc.get(1)-ph.get(1)));
            fr = tf.calculate(pr.get(0), pr.get(1));
            // step 5
            if (fr < fg) {
                ph = new ArrayList<>(pr);
                fh = fr;
            } else {
                if (fr < fh) {
                    ph = new ArrayList<>(pr);
                    fh = fr;
                }
                ps.set(0, (ph.get(0)+pc.get(0))/2);
                ps.set(1, (ph.get(1)+pc.get(1))/2);
                fs = tf.calculate(ps.get(0), ps.get(1));
                if (fs < fh) {
                    ph = new ArrayList<>(ps);
                    fh = fs;
                }
            }
            if (fr < fl) {
                // expansion
                ps.set(0, pc.get(0) + gamma*(fr-pc.get(0)));
                ps.set(1, pc.get(1) + gamma*(fr-pc.get(1)));
                fs = tf.calculate(ps.get(0), ps.get(1));

                if (fs < fr) {
                    ph = new ArrayList<>(ps);
                    fh = fs;
                } else {
                    ph = new ArrayList<>(pr);
                    fh = fr;
                }
            }
            if (fr > fg) {
                // contraction
                ps.set(0, pc.get(0) + beta*(ph.get(0)-pc.get(0)));
                ps.set(1, pc.get(1) + beta*(ph.get(1)-pc.get(1)));
                fs = tf.calculate(ps.get(0), ps.get(1));

                if (fs < fh) {
                    ph = new ArrayList<>(pr);
                    fh = fr;
                }
            }

            if (avgSquare(pl, pg) < precision
                    && avgSquare(pl, ph) < precision
                    && avgSquare(pg, ph) < precision) {

                break;
            }
        }

        pl.set(0, Math.max(pl.get(0), 0.0));
        pl.set(1, Math.max(pl.get(1), 0.0));

        return pl;
    }



    @Override
    List<Double> getSingleLaunchResult(TargetFunction tf
            , List<Double> startPoint
            , double precision
            , double maxIter
            , double lBorder
            , double rBorder) {

        return nelderMead(tf, startPoint, precision, maxIter);
    }

}
