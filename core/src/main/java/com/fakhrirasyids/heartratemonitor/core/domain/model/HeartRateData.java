package com.fakhrirasyids.heartratemonitor.core.domain.model;

public class HeartRateData {

    private final int heartRateBpm;

    public HeartRateData(int heartRateBpm) {
        this.heartRateBpm = heartRateBpm;
    }

    public int getHeartRateBpm() {
        return heartRateBpm;
    }
}