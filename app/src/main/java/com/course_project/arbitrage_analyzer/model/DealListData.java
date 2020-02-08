package com.course_project.arbitrage_analyzer.model;

import java.util.ArrayList;
import java.util.List;

//Data about deals that is sent to list adapter and showed in the bottom part of the screen.
public class DealListData {

    private List<Double> prices;
    private List<Double> amounts;
    private List<String> names;
    private List<DealType> types;

    //Convert data obtained from SoloAsyncTask into DealListData.
    public DealListData(OutputDataSet dataSet) {

        prices = new ArrayList<>();
        names = new ArrayList<>();
        amounts = new ArrayList<>();
        types = new ArrayList<>();

        for(int i = 0; i < dataSet.getDeals().size(); ++i) {

            prices.add(dataSet.getDeals().get(i).getPrice());
            types.add(dataSet.getDeals().get(i).getType());
            amounts.add(dataSet.getDeals().get(i).getAmount());
            names.add(dataSet.getDeals().get(i).getMarketName());
        }
    }
    //Convert data obtained from LoggerAsyncTask into DealListData.


    public List<Double> getPrices() {
        return prices;
    }

    public List<Double> getAmounts() {
        return amounts;
    }

    public List<String> getNames() {
        return names;
    }

    public List<DealType> getTypes() {
        return types;
    }
}
