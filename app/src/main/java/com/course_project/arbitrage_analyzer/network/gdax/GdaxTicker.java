package com.course_project.arbitrage_analyzer.network.gdax;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GdaxTicker {

    @SerializedName("open")
    @Expose
    private String open;

    @SerializedName("high")
    @Expose
    private String high;

    @SerializedName("low")
    @Expose
    private String low;

    @SerializedName("volume")
    @Expose
    private String volume;

    @SerializedName("last")
    @Expose
    private String last;

    @SerializedName("volume_30day")
    @Expose
    private String volume30day;

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public String getVolume30day() {
        return volume30day;
    }

    public void setVolume30day(String volume30day) {
        this.volume30day = volume30day;
    }
}
