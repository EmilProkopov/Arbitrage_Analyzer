package com.course_project.arbitrage_analyzer.network.cryptopia;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CryptopiaResponse {

    @SerializedName("Success")
    @Expose
    private Boolean success;
    @SerializedName("Message")
    @Expose
    private Object message;
    @SerializedName("Data")
    @Expose
    private Data data;
    @SerializedName("Error")
    @Expose
    private Object error;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }

}