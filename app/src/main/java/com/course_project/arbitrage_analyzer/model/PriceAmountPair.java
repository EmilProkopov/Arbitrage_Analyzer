package com.course_project.arbitrage_analyzer.model;

//Stores information about one bid/ask.
public class PriceAmountPair implements Cloneable {

    private Double price;
    private Double amount;
    private String marketName;

    public Object clone() {

        PriceAmountPair copy = new PriceAmountPair();
        copy.setAmount(amount);
        copy.setPrice(price);
        copy.setMarketName(marketName);
        return copy;
    }

    public Double getPrice() {return price;}

    public void setPrice(Double price) {
        this.price = price;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getMarketName() {
        return marketName;
    }

    public void setMarketName(String marketName) {
        this.marketName = marketName;
    }
}
