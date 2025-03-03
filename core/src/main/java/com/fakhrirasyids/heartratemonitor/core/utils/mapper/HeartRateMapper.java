package com.fakhrirasyids.heartratemonitor.core.utils.mapper;

import com.fakhrirasyids.heartratemonitor.core.domain.model.HeartRateData;
import com.fakhrirasyids.heartratemonitor.core.domain.model.HeartRateResponse;

public class HeartRateMapper {
    public static HeartRateData mapToHeartRateData(HeartRateResponse response) {
        return new HeartRateData(response.getValue());
    }
}