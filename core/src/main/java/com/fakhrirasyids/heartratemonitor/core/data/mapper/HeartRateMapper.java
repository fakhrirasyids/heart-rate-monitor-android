package com.fakhrirasyids.heartratemonitor.core.data.mapper;

import com.fakhrirasyids.heartratemonitor.core.domain.model.HeartRateData;
import com.fakhrirasyids.heartratemonitor.core.data.datasource.model.HeartRateResponse;

public class HeartRateMapper {
    public static HeartRateData mapToHeartRateData(HeartRateResponse response) {
        return new HeartRateData(response.getValue());
    }
}