package com.fakhrirasyids.heartratemonitor.core.domain.model;

public class HeartRateData {

    private final int heartRateBpm;
    private final HeartRateZones zone;
    private final boolean isAbnormal;

    public HeartRateData(int heartRateBpm) {
        this.heartRateBpm = heartRateBpm;
        this.zone = classifyHeartRateZone(heartRateBpm);
        this.isAbnormal = (heartRateBpm > 100 || heartRateBpm < 60);
    }

    public int getHeartRateBpm() {
        return heartRateBpm;
    }

    public HeartRateZones getZone() {
        return zone;
    }

    public boolean isAbnormal() {
        return isAbnormal;
    }

    private static HeartRateZones classifyHeartRateZone(int bpm) {
        if (bpm <= 60) return HeartRateZones.RESTING_ZONE;
        if (bpm <= 100) return HeartRateZones.MODERATE_ZONE;
        return HeartRateZones.HIGH_ZONE;
    }
}