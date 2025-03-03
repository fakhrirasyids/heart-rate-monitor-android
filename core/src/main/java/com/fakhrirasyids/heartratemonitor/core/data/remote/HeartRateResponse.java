package com.fakhrirasyids.heartratemonitor.core.data.remote;

import com.google.gson.annotations.SerializedName;

public class HeartRateResponse {

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("unit")
    private String unit;

    @SerializedName("humanId")
    private String humanId;

    @SerializedName("tzOffset")
    private String tzOffset;

    @SerializedName("id")
    private String id;

    @SerializedName("source")
    private String source;

    @SerializedName("userId")
    private String userId;

    @SerializedName("value")
    private int value;

    @SerializedName("timestamp")
    private String timestamp;

    @SerializedName("updatedAt")
    private String updatedAt;

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUnit() {
        return unit;
    }

    public String getHumanId() {
        return humanId;
    }

    public String getTzOffset() {
        return tzOffset;
    }

    public String getId() {
        return id;
    }

    public String getSource() {
        return source;
    }

    public String getUserId() {
        return userId;
    }

    public int getValue() {
        return value;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }
}