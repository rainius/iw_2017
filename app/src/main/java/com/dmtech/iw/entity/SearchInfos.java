package com.dmtech.iw.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchInfos {
    @SerializedName("basic")
    private List<Basic> basics;
    private String status;

    public List<Basic> getBasics() {
        return basics;
    }

    public void setBasics(List<Basic> basics) {
        this.basics = basics;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
