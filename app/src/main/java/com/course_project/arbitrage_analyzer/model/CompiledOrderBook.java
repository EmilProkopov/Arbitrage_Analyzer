package com.course_project.arbitrage_analyzer.model;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

//Array of asks, array of bids and some methods to work with them.
public class CompiledOrderBook implements Cloneable {

    private List<PriceAmountPair> bids;
    private List<PriceAmountPair> asks;

    private double bitfinexFee = 0.1e-2;
    private double cexFee = 0.16e-2;
    private double exmoFee = 0.2e-2;
    private double gdaxFee = 0.5e-2;

    public CompiledOrderBook() {
        bids = new ArrayList<>();
        asks = new ArrayList<>();
    }

    public CompiledOrderBook clone() {

        CompiledOrderBook copy = new CompiledOrderBook();

        List<PriceAmountPair> newBids = new ArrayList<>();
        List<PriceAmountPair> newAsks = new ArrayList<>();
        for (PriceAmountPair pair : this.bids) {
            newBids.add(pair.clone());
        }
        for (PriceAmountPair pair : this.asks) {
            newAsks.add(pair.clone());
        }
        copy.setBids(newBids);
        copy.setAsks(newAsks);
        return copy;
    }

    public List<PriceAmountPair> getBids() {return bids;}

    public void setBids(List<PriceAmountPair> bids) {
        this.bids = bids;
    }

    public List<PriceAmountPair> getAsks() {
        return asks;
    }

    public void setAsks(List<PriceAmountPair> asks) {
        this.asks = asks;
    }

    //Unite two CompiledOrderBooks.
    public void addAll(CompiledOrderBook otherOrderBook) {
        this.bids.addAll(otherOrderBook.getBids());
        this.asks.addAll(otherOrderBook.getAsks());
    }

    //Comparators.
    private class PriceAmountPairComparator implements Comparator<PriceAmountPair> {

        @Override
        public int compare(PriceAmountPair o1, PriceAmountPair o2) {
            return o1.getPrice().compareTo(o2.getPrice());
        }
    }

    private class PriceAmountPairReverseComparator implements Comparator<PriceAmountPair> {

        @Override
        public int compare(PriceAmountPair o1, PriceAmountPair o2) {
            return (-1) * o1.getPrice().compareTo(o2.getPrice());
        }
    }

    public void sort() {
        //bids sorted in descending order by price
        Collections.sort(bids, new PriceAmountPairReverseComparator());
        //asks sorted in ascending order by price
        Collections.sort(asks, new PriceAmountPairComparator());

    }


    private double getCommissionSize(String marketName) {

        switch (marketName) {
            case ("Bitfenix"):
                return bitfinexFee;
            case ("Cex") :
                return cexFee;
            case ("Exmo"):
                return exmoFee;
            case ("Gdax"):
                return gdaxFee;

                default:
                    return 0.0;
        }
    }


    public void applyCommissions() {

        double comission;

        for (PriceAmountPair pap : asks) {
            comission = getCommissionSize(pap.getMarketName());
            pap.setPrice(pap.getPrice() + pap.getPrice()*comission);
        }
        for (PriceAmountPair pap : bids) {
            comission = getCommissionSize(pap.getMarketName());
            pap.setPrice(pap.getPrice() - pap.getPrice()*comission);
        }
    }


    public CompiledOrderBook getTopNOrders(int n) {

        ArrayList<PriceAmountPair> newBids = new ArrayList<>(n);
        ArrayList<PriceAmountPair> newAsks = new ArrayList<>(n);

        for (int i = 0; i < Math.min(n, asks.size()); ++i) {
            newAsks.add(asks.get(i));
        }
        for (int i = 0; i < Math.min(n, bids.size()); ++i) {
            newBids.add(bids.get(i));
        }

        CompiledOrderBook newOB = new CompiledOrderBook();
        newOB.setBids(newBids);
        newOB.setAsks(newAsks);

        return newOB;
    }

}
