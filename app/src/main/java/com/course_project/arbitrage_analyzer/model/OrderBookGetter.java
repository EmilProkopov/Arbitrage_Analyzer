package com.course_project.arbitrage_analyzer.model;

import android.util.Log;

import com.course_project.arbitrage_analyzer.api.MarketApi;
import com.course_project.arbitrage_analyzer.network.bitfinex.BitfinexResponse;
import com.course_project.arbitrage_analyzer.network.cex.CexResponse;
import com.course_project.arbitrage_analyzer.network.exmo.ExmoResponse;
import com.course_project.arbitrage_analyzer.network.gdax.GdaxResponse;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//Gets data from markets and compiles it into the CompiledOrderBook.
public class OrderBookGetter {

    private static final String LOGTAG = "OrderBookGetter";

    private MarketApi api;
    private Retrofit retrofit;
    private OrderBookGetterProgressListener progressListener;


    public OrderBookGetter(OrderBookGetterProgressListener pl) {
        this.progressListener = pl;
    }


    private BitfinexResponse getBitfinexResponse (int limit, String currencyPair) {

        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.bitfinex.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(MarketApi.class);

        String strLimit = String.valueOf(limit);

        Call<BitfinexResponse> responseCall = null;
        if (currencyPair.equals("BTC/USD")) {
            responseCall = api.getBitfinexOrderBook("btcusd", strLimit,
                    strLimit, "1");
        } else if (currencyPair.equals("ETH/USD")) {
            responseCall = api.getBitfinexOrderBook("ethusd", strLimit,
                    strLimit, "1");
        }

        Response<BitfinexResponse> res;

        BitfinexResponse bitfinexResponse = null;
        try {
            res = responseCall.execute();
            bitfinexResponse = res.body();
        } catch (IOException e) {
            Log.d(LOGTAG, "IO");
        }

        return bitfinexResponse;
    }
    //Convert Bitfinex responce into CompiledOrderBook.
    private CompiledOrderBook getBitfinexCleanOrderBook(int limit, String currencyPair) {

        BitfinexResponse response = getBitfinexResponse(limit, currencyPair);

        if(response != null) {
            Log.d(LOGTAG, "Bitfinex OK");
        } else {
            Log.d(LOGTAG, "Bitfinex FAIL");
            return new CompiledOrderBook();
        }

        CompiledOrderBook res = new CompiledOrderBook();
        ArrayList <PriceAmountPair> curAsks = new ArrayList<>();
        ArrayList <PriceAmountPair> curBids = new ArrayList<>();

        for(int i = 0; i < response.getAsks().size(); ++i) {

            PriceAmountPair curPriceQtyPair = new PriceAmountPair();

            curPriceQtyPair.setPrice(Double.parseDouble(response.getAsks().get(i).getPrice()));
            curPriceQtyPair.setAmount(Double.parseDouble(response.getAsks().get(i).getAmount()));
            curPriceQtyPair.setMarketName("Bitfenix");

            curAsks.add(curPriceQtyPair);
        }
        res.setAsks(curAsks);

        for(int i = 0; i < response.getBids().size(); ++i) {

            PriceAmountPair curPriceQtyPair = new PriceAmountPair();

            curPriceQtyPair.setPrice(Double.parseDouble(response.getBids().get(i).getPrice()));
            curPriceQtyPair.setAmount(Double.parseDouble(response.getBids().get(i).getAmount()));
            curPriceQtyPair.setMarketName("Bitfenix");

            curBids.add(curPriceQtyPair);
        }
        res.setBids(curBids);

        return res;
    }


    private CexResponse getCexPartResponse(int limit, String currencyPair) {

        retrofit = new Retrofit.Builder()
                .baseUrl("https://cex.io")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(MarketApi.class);

        String strLimit = String.valueOf(limit);

        Call<CexResponse> responseCall = null;
        if (currencyPair.equals("BTC/USD")) {
           responseCall = api.getCexPartOrderBook("BTC", "USD", strLimit);
        } else if (currencyPair.equals("ETH/USD")) {
            responseCall = api.getCexPartOrderBook("ETH", "USD", strLimit);
        }

        Response<CexResponse> res;

        CexResponse cexResponse = null;
        try {
            res = responseCall.execute();
            cexResponse = res.body();
        } catch (IOException e) {
            Log.d(LOGTAG, e.toString());
        }

        return cexResponse;
    }
    //Convert Cex response into CompiledOrderBook.
    private CompiledOrderBook getCexPartCleanOrderBook(int limit, String currencyPair) {

        CexResponse response = getCexPartResponse(limit, currencyPair);

        if(response != null) {
            Log.d(LOGTAG, "Cex OK");
        } else {
            Log.d(LOGTAG, "Cex FAIL");
            return new CompiledOrderBook();
        }

        CompiledOrderBook res = new CompiledOrderBook();
        ArrayList <PriceAmountPair> curAsks = new ArrayList<>();
        ArrayList <PriceAmountPair> curBids = new ArrayList<>();

        for(int i = 0; i < response.getAsks().size(); ++i) {

            PriceAmountPair curPriceQtyPair = new PriceAmountPair();

            curPriceQtyPair.setPrice(response.getAsks().get(i).get(0));
            curPriceQtyPair.setAmount(response.getAsks().get(i).get(1));
            curPriceQtyPair.setMarketName("Cex");

            curAsks.add(curPriceQtyPair);
        }
        res.setAsks(curAsks);

        for(int i = 0; i < response.getBids().size(); ++i) {

            PriceAmountPair curPriceQtyPair = new PriceAmountPair();

            curPriceQtyPair.setPrice(response.getBids().get(i).get(0));
            curPriceQtyPair.setAmount(response.getBids().get(i).get(1));
            curPriceQtyPair.setMarketName("Cex");

            curBids.add(curPriceQtyPair);
        }
        res.setBids(curBids);

        return res;
    }


    private ExmoResponse getExmoResponseBTCUSTD (int limit, String currencyPair) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.exmo.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(MarketApi.class);

        String strLimit = String.valueOf(limit);

        Call<ExmoResponse> responseCall = null;
        if (currencyPair.equals("BTC/USD")) {
            responseCall = api.getExmoOrderBook("BTC_USD", strLimit);
        } else if(currencyPair.equals("ETH/USD")) {
            responseCall = api.getExmoOrderBook("ETH_USD", strLimit);
        }

        Response<ExmoResponse> res;

        ExmoResponse exmoResponse = null;
        try {
            res = responseCall.execute();
            exmoResponse = res.body();
        } catch (IOException e) {
            Log.d(LOGTAG, "IO");
        }

        return exmoResponse;
    }
    //Convert Exmo response into CompiledOrderBook.
    private CompiledOrderBook getExmoCleanOrderBook(int limit, String currencyPair) {

        ExmoResponse response;
        response = getExmoResponseBTCUSTD(limit, currencyPair);

        if(response != null) {
            Log.d(LOGTAG, "Exmo OK");
        } else {
            Log.d(LOGTAG, "Exmo FAIL");
            return new CompiledOrderBook();
        }

        CompiledOrderBook res = new CompiledOrderBook();
        ArrayList <PriceAmountPair> curAsks = new ArrayList<>();
        ArrayList <PriceAmountPair> curBids = new ArrayList<>();

        if (currencyPair.equals("BCT/USD")) {

            for (int i = 0; i < response.getBTCUSD().getAsk().size(); ++i) {

                PriceAmountPair curPriceQtyPair = new PriceAmountPair();

                curPriceQtyPair.setPrice(Double.parseDouble(response.getBTCUSD().getAsk().get(i).get(0)));
                curPriceQtyPair.setAmount(Double.parseDouble(response.getBTCUSD().getAsk().get(i).get(1)));
                curPriceQtyPair.setMarketName("Exmo");

                curAsks.add(curPriceQtyPair);
            }
            res.setAsks(curAsks);

            for (int i = 0; i < response.getBTCUSD().getBid().size(); ++i) {

                PriceAmountPair curPriceQtyPair = new PriceAmountPair();

                curPriceQtyPair.setPrice(Double.parseDouble(response.getBTCUSD().getBid().get(i).get(0)));
                curPriceQtyPair.setAmount(Double.parseDouble(response.getBTCUSD().getBid().get(i).get(1)));
                curPriceQtyPair.setMarketName("Exmo");

                curBids.add(curPriceQtyPair);
            }
            res.setBids(curBids);
        }
        else if (currencyPair.equals("ETH/USD")) {

            for (int i = 0; i < response.getETHUSD().getAsk().size(); ++i) {

                PriceAmountPair curPriceQtyPair = new PriceAmountPair();

                curPriceQtyPair.setPrice(Double.parseDouble(response.getETHUSD().getAsk().get(i).get(0)));
                curPriceQtyPair.setAmount(Double.parseDouble(response.getETHUSD().getAsk().get(i).get(1)));
                curPriceQtyPair.setMarketName("Exmo");

                curAsks.add(curPriceQtyPair);
            }
            res.setAsks(curAsks);

            for (int i = 0; i < response.getETHUSD().getBid().size(); ++i) {

                PriceAmountPair curPriceQtyPair = new PriceAmountPair();

                curPriceQtyPair.setPrice(Double.parseDouble(response.getETHUSD().getBid().get(i).get(0)));
                curPriceQtyPair.setAmount(Double.parseDouble(response.getETHUSD().getBid().get(i).get(1)));
                curPriceQtyPair.setMarketName("Exmo");

                curBids.add(curPriceQtyPair);
            }
            res.setBids(curBids);
        }

        return res;
    }


    private GdaxResponse getGdaxResponseTop50 (String currencyPair) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.gdax.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(MarketApi.class);

        Call<GdaxResponse> responseCall = null;
        if (currencyPair.equals("BTC/USD")) {
            responseCall = api.getGdaxOrderBook("BTC-USD", "2");
        } else if (currencyPair.equals("ETH/USD")) {
            responseCall = api.getGdaxOrderBook("ETH-USD", "2");
        }

        Response<GdaxResponse> res;

        GdaxResponse gdaxResponse = null;
        try {
            res = responseCall.execute();
            gdaxResponse = res.body();
        } catch (IOException e) {
            Log.d(LOGTAG, "IO");
        }

        return gdaxResponse;
    }
    //Convert Gdax response into CompiledOrderBook.
    private CompiledOrderBook getGdaxTop50CleanOrderBook(String currencyPair) {

        GdaxResponse responce;
        responce = getGdaxResponseTop50(currencyPair);

        if(responce != null) {
            Log.d(LOGTAG, "Gdax OK");
        } else {
            Log.d(LOGTAG, "Gdax FAIL");
            return new CompiledOrderBook();
        }

        CompiledOrderBook res = new CompiledOrderBook();
        ArrayList <PriceAmountPair> curAsks = new ArrayList<>();
        ArrayList <PriceAmountPair> curBids = new ArrayList<>();

        for(int i = 0; i < responce.getAsks().size(); ++i) {

            PriceAmountPair curPriceQtyPair = new PriceAmountPair();

            curPriceQtyPair.setPrice(Double.parseDouble(responce.getAsks().get(i).get(0)));
            curPriceQtyPair.setAmount(Double.parseDouble(responce.getAsks().get(i).get(1)));
            curPriceQtyPair.setMarketName("Gdax");

            curAsks.add(curPriceQtyPair);
        }
        res.setAsks(curAsks);

        for(int i = 0; i < responce.getBids().size(); ++i) {

            PriceAmountPair curPriceQtyPair = new PriceAmountPair();

            curPriceQtyPair.setPrice(Double.parseDouble(responce.getBids().get(i).get(0)));
            curPriceQtyPair.setAmount(Double.parseDouble(responce.getBids().get(i).get(1)));
            curPriceQtyPair.setMarketName("Gdax");

            curBids.add(curPriceQtyPair);
        }
        res.setBids(curBids);

        return res;
    }


    /*private KucoinResponse getKucoinResponseBTCUSTD (int limit, String currencyPair) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.kucoin.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(MarketApi.class);

        String strLimit = String.valueOf(limit);

        Call<KucoinResponse> responseCall = null;
        if (currencyPair.equals("BTC/USD")) {
            responseCall = api.getKucoinOrderBook("BTC-USDT", strLimit);
        } else if (currencyPair.equals("ETH/USD")) {
            responseCall = api.getKucoinOrderBook("ETH-USDT", strLimit);
        }

        Response<KucoinResponse> res;

        KucoinResponse kucoinResponse = null;
        try {
            res = responseCall.execute();
            kucoinResponse = res.body();
        } catch (IOException e) {
            Log.e(LOGTAG, "IO");
        }

        return kucoinResponse;
    }
    //Convert KuCoin response into CompiledOrderBook.
    private CompiledOrderBook getKucoinCleanOrderBook(int limit, String currencyPair) {

        KucoinResponse response;
        response = getKucoinResponseBTCUSTD(limit, currencyPair);

        if(response != null) {
            Log.d(LOGTAG, "Kucoin OK");
        } else {
            Log.d(LOGTAG, "Kucoin FAIL");
            return new CompiledOrderBook();
        }

        CompiledOrderBook res = new CompiledOrderBook();
        ArrayList <PriceAmountPair> curAsks = new ArrayList<>();
        ArrayList <PriceAmountPair> curBids = new ArrayList<>();

        for(int i = 0; i < response.getData().getBUY().size(); ++i) {

            PriceAmountPair curPriceQtyPair = new PriceAmountPair();

            curPriceQtyPair.setPrice(response.getData().getBUY().get(i).get(0));
            curPriceQtyPair.setAmount(response.getData().getBUY().get(i).get(1));
            curPriceQtyPair.setMarketName("Kucoin");

            curAsks.add(curPriceQtyPair);
        }
        res.setAsks(curAsks);

        for(int i = 0; i < response.getData().getSELL().size(); ++i) {

            PriceAmountPair curPriceQtyPair = new PriceAmountPair();

            curPriceQtyPair.setPrice(response.getData().getSELL().get(i).get(0));
            curPriceQtyPair.setAmount(response.getData().getSELL().get(i).get(1));
            curPriceQtyPair.setMarketName("Kucoin");

            curBids.add(curPriceQtyPair);
        }
        res.setBids(curBids);

        return res;
    }*/

    //Get Order books from all markets and unite them into one.
    public CompiledOrderBook getCompiledOrderBook(SettingsContainer settings) {

        boolean bitfinex = settings.getBitfinex();
        boolean cex = settings.getCex();
        boolean exmo = settings.getExmo();
        boolean gdax = settings.getGdax();
        int limit = settings.getDepthLimit();
        String currencyPair = settings.getCurrencyPare();


        int exchangeCount = 0;
        if (bitfinex) exchangeCount++;
        if (cex) exchangeCount++;
        if (exmo) exchangeCount++;
        if (gdax) exchangeCount++;

        //To avoid NullPointerException
        if (exchangeCount == 0) {
            exchangeCount++;
        }

        int processedExchangeCount = 0;

        CompiledOrderBook result = new CompiledOrderBook();

        if (bitfinex) {
            processedExchangeCount++;
            progressListener.onUpdateOrderBookGetterProgress(
                    Math.round(100*processedExchangeCount/exchangeCount));
            //Add all it's orders into the order book.
            result.addAll(getBitfinexCleanOrderBook(limit, currencyPair));
        }
        if (cex) {
            processedExchangeCount++;
            progressListener.onUpdateOrderBookGetterProgress(
                    Math.round(100*processedExchangeCount/exchangeCount));
            result.addAll(getCexPartCleanOrderBook(limit, currencyPair));
        }
        if (exmo) {
            processedExchangeCount++;
            progressListener.onUpdateOrderBookGetterProgress(
                    Math.round(100*processedExchangeCount/exchangeCount));
            result.addAll(getExmoCleanOrderBook(limit, currencyPair));
        }
        if (gdax) {
            processedExchangeCount++;
            progressListener.onUpdateOrderBookGetterProgress(
                    Math.round(100*processedExchangeCount/exchangeCount));
            result.addAll(getGdaxTop50CleanOrderBook(currencyPair));
        }
        /*if (sp.getBoolean("kucoin", true)) {
            result.addAll(getKucoinCleanOrderBook(limit, currencyPair));
        }*/
        //Sort the order book.
        //Bids sorted in descending order by price.
        //Asks sorted in ascending order by price.
        result.sort();

        progressListener.onUpdateOrderBookGetterProgress(0);

        Log.e(LOGTAG, result.toString());
        return result;
    }


    public interface OrderBookGetterProgressListener {

        void onUpdateOrderBookGetterProgress(Integer progress);
    }

}
