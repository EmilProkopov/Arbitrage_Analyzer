package com.course_project.arbitrage_analyzer.model;

import android.util.Log;

import com.course_project.arbitrage_analyzer.api.MarketApi;
import com.course_project.arbitrage_analyzer.network.cex.CexTicker;
import com.course_project.arbitrage_analyzer.network.exmo.ExmoSingleTicker;
import com.course_project.arbitrage_analyzer.network.exmo.ExmoTickers;
import com.course_project.arbitrage_analyzer.network.gdax.GdaxTicker;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MarketInfoGetter {

    private static final String LOGTAG = "OrderBookGetter";

    private MarketApi api;
    private Retrofit retrofit;


    private double getBitfinexTradeRate(String currencyPair) {

        double rate = 0.0;

        retrofit = new Retrofit.Builder()
                .baseUrl("https://api-pub.bitfinex.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(MarketApi.class);


        Call<ArrayList<Object>> responseCall = null;
        if (currencyPair.equals("BTC/USD")) {
            responseCall = api.getBitfinexTicket("tBTCUSD");
        } else if (currencyPair.equals("ETH/USD")) {
            responseCall = api.getBitfinexTicket("tETHUSD");
        }

        Response<ArrayList<Object>> res;

        ArrayList<Object> response = null;
        try {
            res = responseCall.execute();
            response = res.body();
        } catch (Exception e) {
            Log.d(LOGTAG, "Request error");
        }

        if (response != null && response.size() > 0) {
            double avgPrice = ((Double)response.get(0) + (Double)response.get(2)) / 2;
            rate = ((Double) response.get(7) / (24*60*60)) * avgPrice;
        }

        return rate;
    }


    private double getCexTradeRate(String currencyPair) {

        double rate = 0.0;

        retrofit = new Retrofit.Builder()
                .baseUrl("https://cex.io")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(MarketApi.class);


        Call<CexTicker> responseCall = null;
        if (currencyPair.equals("BTC/USD")) {
            responseCall = api.getCexTicker("BTC", "USD");
        } else if (currencyPair.equals("ETH/USD")) {
            responseCall = api.getCexTicker("ETH", "USD");
        }

        Response<CexTicker> res;

        CexTicker response = null;
        try {
            res = responseCall.execute();
            response = res.body();
        } catch (Exception e) {
            Log.d(LOGTAG, "Request error");
        }

        if (response != null) {
            double avgPrice = (response.getAsk() + response.getBid()) / 2;
            rate = (Double.parseDouble(response.getVolume()) / (24*60*60)) * avgPrice;
        }

        return rate;
    }

    private double getExmoTradeRate(String currencyPair) {

        double rate = 0.0;

        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.exmo.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(MarketApi.class);


        Call<ExmoTickers> responseCall = api.getExmoTickers();

        Response<ExmoTickers> res;

        ExmoTickers response = null;
        try {
            res = responseCall.execute();
            response = res.body();
        } catch (IOException e) {
            Log.d(LOGTAG, "IO");
        }

        if (response != null) {
            ExmoSingleTicker ticker = null;
            if (currencyPair.equals("BTC/USD")) {
                ticker = response.getBTCUSDTicker();
            } else if(currencyPair.equals("ETH/USD")) {
                ticker = response.getETHUSDTicker();
            }
            rate = Double.parseDouble(ticker.getAvg())
                    * Double.parseDouble(ticker.getVol())
                    / (24*60*60);
        }

        return rate;
    }


    private double getGdaxTradeRate(String currencyPair) {

        double rate = 0.0;

        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.pro.coinbase.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(MarketApi.class);


        Call<GdaxTicker> responseCall = null;
        if (currencyPair.equals("BTC/USD")) {
            responseCall = api.getGdaxTicker("BTC-USD");
        } else if (currencyPair.equals("ETH/USD")) {
            responseCall = api.getGdaxTicker("ETH-USD");
        }

        Response<GdaxTicker> res;

        GdaxTicker response = null;
        try {
            res = responseCall.execute();
            response = res.body();
        } catch (IOException e) {
            Log.d(LOGTAG, "IO");
        }

        if (response != null) {
            double avgPrice = (Double.parseDouble(response.getHigh())
                    + Double.parseDouble(response.getLow())) / 2;
            rate = (Double.parseDouble(response.getVolume()) / (24*60*60)) * avgPrice;
        }

        return rate;
    }



    public double getTradeRatePerSeoond(SettingsContainer settings) {

        boolean bitfinex = settings.getBitfinex();
        boolean cex = settings.getCex();
        boolean exmo = settings.getExmo();
        boolean gdax = settings.getGdax();
        String currencyPair = settings.getCurrencyPare();

        ArrayList<Double> tradeRates = new ArrayList<>();
        double tr;
        if (bitfinex) {
            tr = getBitfinexTradeRate(currencyPair);
            if (tr != 0.0) {
                tradeRates.add(tr);
            }
        }
        if (cex) {
            tr = getCexTradeRate(currencyPair);
            if (tr != 0.0) {
                tradeRates.add(tr);
            }
        }
        if (exmo) {
            tr = getExmoTradeRate(currencyPair);
            if (tr != 0.0) {
                tradeRates.add(tr);
            }
        }

        if (gdax) {
            tr = getGdaxTradeRate(currencyPair);
            if (tr != 0.0) {
                tradeRates.add(tr);
            }
        }

        double avg = 0.0;
        for (Double item : tradeRates) {
            avg += item;
        }
        avg /= tradeRates.size();
        return avg;
    }
}
