package com.fakhrirasyids.heartratemonitor.core.utils.enums;

public enum HeartRateZones {
    RESTING_ZONE("Resting Zone", 0),
    MODERATE_ZONE("Moderate Zone", 1),
    HIGH_ZONE("High Zone", 2);

    private final String displayName;
    private final int idx;

    HeartRateZones(String displayName, int idx) {
        this.displayName = displayName;
        this.idx = idx;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getIdx() {
        return idx;
    }
}
