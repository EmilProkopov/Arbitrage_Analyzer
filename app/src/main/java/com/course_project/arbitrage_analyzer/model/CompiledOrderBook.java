package com.course_project.arbitrage_analyzer.model;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

//Array of asks, array of bids and some methods to work with them.
public class CompiledOrderBook {

    private List<PriceAmountPair> bids;
    private List<PriceAmountPair> asks;

    public CompiledOrderBook() {
        bids = new ArrayList<>();
        asks = new ArrayList<>();
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



}
