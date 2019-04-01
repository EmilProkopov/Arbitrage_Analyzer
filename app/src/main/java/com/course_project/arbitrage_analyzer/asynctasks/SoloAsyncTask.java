package com.course_project.arbitrage_analyzer.asynctasks;

import android.os.AsyncTask;
import android.util.Log;

import com.course_project.arbitrage_analyzer.interfaces.ArbitragePresenter;
import com.course_project.arbitrage_analyzer.model.CompiledOrderBook;
import com.course_project.arbitrage_analyzer.model.Deal;
import com.course_project.arbitrage_analyzer.model.OrderBookGetter;
import com.course_project.arbitrage_analyzer.model.OutputDataSet;
import com.course_project.arbitrage_analyzer.model.SettingsContainer;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

//Creates new thread, gets data from markets and displays it.
public class SoloAsyncTask extends AsyncTask<Void, OutputDataSet, OutputDataSet> {

    private static final String LOGTAG = "SoloAsyncTask";

    private ArbitragePresenter presenter;
    private SettingsContainer settings;
    private String secondCurrency; //Second currency in the pair.
    private OrderBookGetter orderBookGetter;


    public SoloAsyncTask(OrderBookGetter.OrderBookGetterProgressListener orderBookListener,
                         ArbitragePresenter presenter) {

        Log.e(LOGTAG, "SOLO ASYNCTASK CREATED");
        this.presenter = presenter;
        settings = new SettingsContainer();
        orderBookGetter = new OrderBookGetter(orderBookListener);
    }

    public void updateSettings(SettingsContainer newSettings) {
        this.settings = newSettings;
        secondCurrency = settings.getCurrencyPare().split("/")[1];
    }


    //Calculate the profit
    private OutputDataSet formOutputDataSet(CompiledOrderBook orderBook) {

        Double profit = 0.0; //Profit that we can get.
        Double amount = 0.0; //Amount of money necessary to do it.
        //Points of the plot.
        ArrayList<Double> profitPoints = new ArrayList<>();
        ArrayList<Double> amountPoints = new ArrayList<>();
        //List of deals to make.
        ArrayList<Deal> deals = new ArrayList<>(); //List of deals that should be made.
        final Double alpha = 0.1;
        Double optimalAmount = 0.0;
        Double optimalProfit = 0.0;
        Integer num = -1;   //Number of deals to make.
        Double curK; //Current K.

        Double prevAmount = 0.0;
        Double prevProfit = 0.0;
        Double firstK = -1.0; //First value of K.
        //iterators.
        int bx = 0, ax = 0;

        OutputDataSet outputDataSet = new OutputDataSet();

        while ((ax < orderBook.getAsks().size())
                && (bx < orderBook.getBids().size())
                //While we can make profit from the deal.
                && (orderBook.getBids().get(bx).getPrice() > orderBook.getAsks().get(ax).getPrice())) {

            num += 1;

            Double bidAmount = orderBook.getBids().get(bx).getAmount(); //Amount of top bid.
            Double askAmount = orderBook.getAsks().get(ax).getAmount(); //Amount of top ask.
            if (bidAmount.equals(0.0)) {
                bx += 1;
                continue;
            }
            if (askAmount.equals(0.0)) {
                ax += 1;
                continue;
            }
            //Amount of currency to buy (sell).
            Double m = Math.min(bidAmount, askAmount);

            Double currentProfit = (orderBook.getBids().get(bx).getPrice()
                    - orderBook.getAsks().get(ax).getPrice()) * m;

            profit += currentProfit;
            amount += orderBook.getAsks().get(ax).getPrice() * m;

            profitPoints.add(profit);
            amountPoints.add(amount);

            deals.add(new Deal("Buy", orderBook.getAsks().get(ax).getMarketName()
                    , m, orderBook.getAsks().get(ax).getPrice()));
            deals.add(new Deal("Sell", orderBook.getBids().get(bx).getMarketName()
                    , m, orderBook.getBids().get(bx).getPrice()));

            //Take into account that we have made a deal and top bid and ask are changed.
            Double oldBidAmount = orderBook.getBids().get(bx).getAmount();
            Double oldAskAmount = orderBook.getAsks().get(ax).getAmount();
            orderBook.getBids().get(bx).setAmount(oldBidAmount - m);
            orderBook.getAsks().get(ax).setAmount(oldAskAmount - m);

            //Check if we have achieved the optimal point.
            if (num.equals(2)) {
                firstK = (profit - prevProfit) / (amount - prevAmount);
            } else if (num > 1) {
                curK = (profit - prevProfit) / (amount - prevAmount);
                if (curK / firstK >= alpha) {
                    optimalAmount = amount;
                    optimalProfit = profit;
                }
            }
            prevAmount = amount;
            prevProfit = profit;
        }

        //Put data into the resulting data set.
        outputDataSet.setProfit(profit);
        outputDataSet.setAmount(amount);
        outputDataSet.setOptimalAmount(optimalAmount);
        outputDataSet.setOptimalProfit(optimalProfit);
        outputDataSet.setAmountPoints(amountPoints);
        outputDataSet.setProfitPoints(profitPoints);
        outputDataSet.setDeals(deals);
        outputDataSet.setSecondCurrency(secondCurrency);

        //Unite buy and sell deals made on same market into one deal.
        outputDataSet.uniteDealsMadeOnSameMarkets();

        return outputDataSet;
    }


    @Override
    protected OutputDataSet doInBackground(Void... params) {

        while (!isCancelled()) {

            //Get orderBook with top orders from all markets.
            CompiledOrderBook orderBook = orderBookGetter.getCompiledOrderBook(settings);

            OutputDataSet outputDataSet = formOutputDataSet(orderBook);

            //Display data.
            publishProgress(outputDataSet);

            //Wait before next data updating.
            try {
                TimeUnit.SECONDS.sleep(settings.getUpdateRateSeconds());
            } catch (InterruptedException e) {
                Log.e(LOGTAG, e.getMessage());
            }
        }
        return null;
    }


    @Override
    protected void onProgressUpdate(OutputDataSet... params) {

        super.onProgressUpdate(params);
        OutputDataSet dataSet = params[0];

        Log.d(LOGTAG, "SoloAsyncTask RUNNING");
        Log.e(LOGTAG, "Publishing progress");

        if (dataSet.getDeals().size() <= 1) {
            presenter.showToast("No profit can be made.\nCheck Internet connection\n" +
                    "and number of active markets.");
        } else {
            presenter.onWorkerResult(dataSet);
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        this.presenter = null;
    }
}
