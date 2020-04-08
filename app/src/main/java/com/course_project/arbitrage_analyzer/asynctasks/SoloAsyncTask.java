package com.course_project.arbitrage_analyzer.asynctasks;

import android.os.AsyncTask;
import android.util.Log;

import com.course_project.arbitrage_analyzer.interfaces.ArbitragePresenter;
import com.course_project.arbitrage_analyzer.model.CompiledOrderBook;
import com.course_project.arbitrage_analyzer.model.Deal;
import com.course_project.arbitrage_analyzer.model.DealType;
import com.course_project.arbitrage_analyzer.model.MarketInfoGetter;
import com.course_project.arbitrage_analyzer.model.OrderBookGetter;
import com.course_project.arbitrage_analyzer.model.OutputDataSet;
import com.course_project.arbitrage_analyzer.model.PriceAmountPair;
import com.course_project.arbitrage_analyzer.model.SettingsContainer;
import com.course_project.arbitrage_analyzer.model.disbalance_minimization.DisbalanceEstimator;
import com.course_project.arbitrage_analyzer.model.disbalance_minimization.EstimatorResult;
import com.course_project.arbitrage_analyzer.model.disbalance_minimization.MinimizerResult;
import com.course_project.arbitrage_analyzer.model.disbalance_minimization.minimizers.BayesLaplaceMinimizer;
import com.course_project.arbitrage_analyzer.model.disbalance_minimization.minimizers.DisbalanceMinimizer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

//Creates new thread, gets data from markets and displays it.
public class SoloAsyncTask extends AsyncTask<Void, OutputDataSet, OutputDataSet> {

    private static final String LOGTAG = "SoloAsyncTask";

    private ArbitragePresenter presenter;
    private SettingsContainer settings;
    private String firstCurrency;
    private String secondCurrency; //Second currency in the pair.
    private OrderBookGetter orderBookGetter;
    private MarketInfoGetter infoGetter;


    public SoloAsyncTask(OrderBookGetter.OrderBookGetterProgressListener orderBookListener,
                         ArbitragePresenter presenter) {

        Log.e(LOGTAG, "SOLO ASYNCTASK CREATED");
        this.presenter = presenter;
        settings = new SettingsContainer();
        orderBookGetter = new OrderBookGetter(orderBookListener);
        infoGetter = new MarketInfoGetter();
    }

    public void updateSettings(SettingsContainer newSettings) {
        this.settings = newSettings;
        firstCurrency = settings.getCurrencyPare().split("/")[0];
        secondCurrency = settings.getCurrencyPare().split("/")[1];
    }


    private void fillAskBidChartPoints(List<Double> bidAmountPoints ,
                                       List<Double> askAmountPoints ,
                                       List<Double> bidPricePoints ,
                                       List<Double> askPricePoints ,
                                       CompiledOrderBook orderBook) {

        List<PriceAmountPair> asks = orderBook.getAsks();
        List<PriceAmountPair> bids = orderBook.getBids();

        double y = 0.0;

        for (int i = 0; i < asks.size(); ++i) {
            if (asks.get(i).getAmount() > 0) {
                y += asks.get(i).getAmount() * asks.get(i).getPrice();
                askAmountPoints.add(y);
                askPricePoints.add(asks.get(i).getPrice());
            }
        }

        y = 0.0;
        for (int i = 0; i < bids.size(); ++i) {
            if (bids.get(i).getAmount() > 0) {
                y += bids.get(i).getAmount() * bids.get(i).getPrice();
                bidAmountPoints.add(y);
                bidPricePoints.add(bids.get(i).getPrice());
            }
        }
    }

    private OutputDataSet analyzeMarkets() {

        double trps = infoGetter.getTradeRatePerSeoond(settings);
        //DisbalanceMinimizer minimizer = new SimpleMinimizer();
        DisbalanceMinimizer minimizer = new BayesLaplaceMinimizer((short)10, 1.0);
        minimizer.setTradeRatePerSecond(trps);

        DisbalanceEstimator estimator = new DisbalanceEstimator();

        //Get orderBook with top orders from all markets.
        // CompiledOrderBook orderBook = orderBookGetter.getCompiledOrderBook(settings, true);
         CompiledOrderBook orderBook = genTestOB1();
        CompiledOrderBook orderBookCopy = orderBook.clone();

        MinimizerResult minResult = minimizer.getResult(orderBookCopy);
        //CompiledOrderBook actualOrderBook = orderBookGetter.getCompiledOrderBook(settings, false);
         CompiledOrderBook actualOrderBook = genTestOB1();
        EstimatorResult estimate = estimator.getEstimate(actualOrderBook, minResult.getResultOrderBook());

        return formOutputDataSet(orderBook, minResult.getOptimalV(), estimate.getUsedSecondCurrencyAmount());
    }

    private OutputDataSet formOutputDataSet(CompiledOrderBook orderBook, double optimalV, double realV) {

        double profit = 0.0; //Profit that we can get.
        Double firstCurrencyAmount = 0.0;
        Double secondCurrencyAmount = 0.0;
        //Points of the plot.
        ArrayList<Double> profitPoints = new ArrayList<>();
        ArrayList<Double> amountPoints = new ArrayList<>();
        //List of deals to make.
        ArrayList<Deal> deals = new ArrayList<>(); //List of deals that should be made.
        double optimalFirstCurrencyAmount = 0.0;
        double optimalProfit = 0.0;

        double realFirstCurrencyAmount = 0.0;
        double realProfit = 0.0;

        boolean optimalPointPassed = false;
        //iterators.
        int bx = 0, ax = 0;

        OutputDataSet outputDataSet = new OutputDataSet();

        while ((ax < orderBook.getAsks().size())
                && (bx < orderBook.getBids().size())
                //While we can make profit from the deal.
                && (orderBook.getBids().get(bx).getPrice() > orderBook.getAsks().get(ax).getPrice())) {


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

            //Check if we have achieved the optimal point.
            if (secondCurrencyAmount + orderBook.getAsks().get(ax).getPrice() * m >= optimalV) {

                optimalPointPassed = true;
                double deltaSecond = optimalV - secondCurrencyAmount;
                double deltaFirst = deltaSecond / orderBook.getAsks().get(ax).getPrice();
                optimalFirstCurrencyAmount = firstCurrencyAmount + deltaFirst;
                optimalProfit = profit + (orderBook.getBids().get(bx).getPrice()
                        - orderBook.getAsks().get(ax).getPrice()) * deltaFirst;

                deals.add(new Deal(DealType.BUY, orderBook.getAsks().get(ax).getMarketName()
                        , deltaFirst, orderBook.getAsks().get(ax).getPrice()));
                deals.add(new Deal(DealType.SELL, orderBook.getBids().get(bx).getMarketName()
                        , deltaFirst, orderBook.getBids().get(bx).getPrice()));
            }

            if (secondCurrencyAmount + orderBook.getAsks().get(ax).getPrice() * m >= realV) {

                double deltaSecond = realV - secondCurrencyAmount;
                double deltaFirst = deltaSecond / orderBook.getAsks().get(ax).getPrice();
                realFirstCurrencyAmount = firstCurrencyAmount + deltaFirst;
                realProfit = profit + (orderBook.getBids().get(bx).getPrice()
                        - orderBook.getAsks().get(ax).getPrice()) * deltaFirst;
            }

            Double currentProfit = (orderBook.getBids().get(bx).getPrice()
                    - orderBook.getAsks().get(ax).getPrice()) * m;

            profit += currentProfit;
            secondCurrencyAmount += orderBook.getAsks().get(ax).getPrice() * m;
            firstCurrencyAmount += m;

            profitPoints.add(profit);
            amountPoints.add(secondCurrencyAmount);

            if (!optimalPointPassed) {
                deals.add(new Deal(DealType.BUY, orderBook.getAsks().get(ax).getMarketName()
                        , m, orderBook.getAsks().get(ax).getPrice()));
                deals.add(new Deal(DealType.SELL, orderBook.getBids().get(bx).getMarketName()
                        , m, orderBook.getBids().get(bx).getPrice()));
            }

            //Take into account that we have made a deal and top bid and ask are changed.
            Double oldBidAmount = orderBook.getBids().get(bx).getAmount();
            Double oldAskAmount = orderBook.getAsks().get(ax).getAmount();
            orderBook.getBids().get(bx).setAmount(oldBidAmount - m);
            orderBook.getAsks().get(ax).setAmount(oldAskAmount - m);
        }

        ArrayList<Double> bitAmountPoints = new ArrayList<>();
        ArrayList<Double> askAmountPoints = new ArrayList<>();
        ArrayList<Double> bidPricePoints = new ArrayList<>();
        ArrayList<Double> askPricePoints = new ArrayList<>();

        fillAskBidChartPoints(bitAmountPoints, askAmountPoints, bidPricePoints, askPricePoints,
                orderBook);

        //Put data into the resulting data set.
        outputDataSet.setProfit(profit);
        outputDataSet.setOptimalSecondCurrencyAmount(optimalV);
        outputDataSet.setOptimalFirstCurrencyAmount(optimalFirstCurrencyAmount);
        outputDataSet.setOptimalProfit(optimalProfit);

        outputDataSet.setRealFirstCurrencyAmount(realFirstCurrencyAmount);
        outputDataSet.setRealSecondCurrencyAmount(realV);
        outputDataSet.setRealProfit(realProfit);

        outputDataSet.setAmountPoints(amountPoints);
        outputDataSet.setProfitPoints(profitPoints);
        outputDataSet.setDeals(deals);
        outputDataSet.setFirstCurrency(firstCurrency);
        outputDataSet.setSecondCurrency(secondCurrency);

        outputDataSet.setFirstCurrencyAmount(firstCurrencyAmount);
        outputDataSet.setSecondCurrencyAmount(secondCurrencyAmount);

        outputDataSet.setBidAmountPoints(bitAmountPoints);
        outputDataSet.setAskAmountPoints(askAmountPoints);
        outputDataSet.setBidPricePoints(bidPricePoints);
        outputDataSet.setAskPricePoints(askPricePoints);

        //Unite buy and sell deals made on same market into one deal.
        outputDataSet.uniteDealsMadeOnSameMarkets();

        return outputDataSet;
    }


    @Override
    protected OutputDataSet doInBackground(Void... params) {

        while (!isCancelled()) {

            OutputDataSet outputDataSet = analyzeMarkets();

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
///*
    private CompiledOrderBook genTestOB1() {
        CompiledOrderBook ob = new CompiledOrderBook();
        double[] askAmounts = new double[] {10, 4, 2};
        double[] askPrices = new double[] {2, 3, 4};
        double[] bidAmounts = new double[] {5, 3, 1};
        double[] bidPrices = new double[] {4, 3.1, 2};

        List<PriceAmountPair> asks = new ArrayList<>();
        List<PriceAmountPair> bids = new ArrayList<>();

        for (int i = 0; i < askAmounts.length; ++i) {
            asks.add(new PriceAmountPair(askPrices[i], askAmounts[i], "A"));
        }
        for (int i = 0; i < bidAmounts.length; ++i) {
            bids.add(new PriceAmountPair(bidPrices[i], bidAmounts[i], "B"));
        }

        ob.setAsks(asks);
        ob.setBids(bids);
        ob.sort();
        return ob;
    }
/*
    private CompiledOrderBook genTestOB1() {
        CompiledOrderBook ob = new CompiledOrderBook();
        double[] askAmounts = new double[] {20, 10, 4, 2, 0.5};
        double[] askPrices = new double[] {1, 2, 3, 4, 5};
        double[] bidAmounts = new double[] {15, 5, 3, 1, 0.1};
        double[] bidPrices = new double[] {5, 4, 3.1, 2, 1};

        List<PriceAmountPair> asks = new ArrayList<>();
        List<PriceAmountPair> bids = new ArrayList<>();

        for (int i = 0; i < askAmounts.length; ++i) {
            asks.add(new PriceAmountPair(askPrices[i], askAmounts[i], "A"));
        }
        for (int i = 0; i < bidAmounts.length; ++i) {
            bids.add(new PriceAmountPair(bidPrices[i], bidAmounts[i], "B"));
        }

        ob.setAsks(asks);
        ob.setBids(bids);
        ob.sort();
        return ob;
    }
*/
}
