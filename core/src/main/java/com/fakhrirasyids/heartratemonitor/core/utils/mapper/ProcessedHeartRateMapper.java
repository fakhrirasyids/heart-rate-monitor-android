package com.fakhrirasyids.heartratemonitor.core.utils.mapper;

import com.fakhrirasyids.heartratemonitor.core.domain.model.HeartRateData;
import com.fakhrirasyids.heartratemonitor.core.domain.model.ProcessedHeartRate;

public class ProcessedHeartRateMapper {
    public static ProcessedHeartRate mapToProcessedHeartRate(HeartRateData data) {
        return new ProcessedHeartRate(data);
    }
}

