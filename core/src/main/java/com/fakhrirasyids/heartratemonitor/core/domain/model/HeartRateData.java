package com.fakhrirasyids.heartratemonitor.core.domain.model;

import com.google.gson.annotations.SerializedName;

public class HeartRateData {

    public HeartRateData(int heartRateBpm) {
        this.heartRateBpm = heartRateBpm;
    }

    public int getHeartRateBpm() {
        return heartRateBpm;
    }

    private int heartRateBpm;
}