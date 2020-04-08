package com.course_project.arbitrage_analyzer.api;


import com.course_project.arbitrage_analyzer.network.bitfinex.BitfinexResponse;
import com.course_project.arbitrage_analyzer.network.cex.CexResponse;
import com.course_project.arbitrage_analyzer.network.cex.CexTicker;
import com.course_project.arbitrage_analyzer.network.exmo.ExmoResponse;
import com.course_project.arbitrage_analyzer.network.exmo.ExmoTickers;
import com.course_project.arbitrage_analyzer.network.gdax.GdaxResponse;
import com.course_project.arbitrage_analyzer.network.gdax.GdaxTicker;
import com.course_project.arbitrage_analyzer.network.kucoin.KucoinResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

//Used to form a Retrofit request.
public interface MarketApi {

    @GET("/v1/book/{cur_pair}")
    Call<BitfinexResponse> getBitfinexOrderBook(@Path("cur_pair") String cur_pair,
                                                @Query("limit_bids") String limitBids,
                                                @Query("limit_asks") String limitAsks,
                                                @Query("group") String group);

    @GET("/v2/ticker/{symbol}")
    Call<ArrayList<Object>> getBitfinexTicket(@Path("symbol") String curPair);



    @GET("/api/order_book/{cur_1}/{cur_2}/")
    Call<CexResponse> getCexPartOrderBook(@Path("cur_1") String cur_1,
                                          @Path("cur_2") String cur_2,
                                          @Query("depth") String depth);

    @GET("/api/ticker/{cur_1}/{cur_2}/")
    Call<CexTicker> getCexTicker(@Path("cur_1") String cur_1, @Path("cur_2") String cur_2);


    @GET("/v1/order_book/")
    Call<ExmoResponse> getExmoOrderBook(@Query("pair") String pair, @Query("limit") String limit);

    @GET("/v1/ticker/")
    Call<ExmoTickers> getExmoTickers();



    @GET("/products/{cur_pair}/book")
    Call<GdaxResponse> getGdaxOrderBook(@Path("cur_pair") String cur_pair,
                                        @Query("level") String level);

    @GET("/products/{cur_pair}/stats")
    Call<GdaxTicker> getGdaxTicker(@Path("cur_pair") String cur_pair);

    @GET("/v1/open/orders")
    Call<KucoinResponse> getKucoinOrderBook(@Query("symbol") String symbol,
                                            @Query("limit") String limit);

}
