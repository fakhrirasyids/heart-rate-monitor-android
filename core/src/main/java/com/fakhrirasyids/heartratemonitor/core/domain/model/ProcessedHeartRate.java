package com.fakhrirasyids.heartratemonitor.core.domain.model;

public class ProcessedHeartRate {
    private final int bpm;
    private final HeartRateZones zone;
    private final boolean isAbnormal;

    public ProcessedHeartRate(HeartRateData data) {
        this.bpm = data.getHeartRateBpm();
        this.zone = classifyHeartRateZone(this.bpm);
        this.isAbnormal = (this.bpm > 100 || this.bpm < 60);
    }

    public int getBpm() {
        return bpm;
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
