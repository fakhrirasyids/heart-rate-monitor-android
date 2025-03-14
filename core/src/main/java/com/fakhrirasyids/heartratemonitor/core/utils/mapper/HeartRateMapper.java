package com.fakhrirasyids.heartratemonitor.core.utils.mapper;

import com.fakhrirasyids.heartratemonitor.core.data.datasource.model.HeartRateResponse;
import com.fakhrirasyids.heartratemonitor.core.domain.model.HeartRateData;

public class HeartRateMapper {
    public static HeartRateData mapToHeartRateData(HeartRateResponse response) {
        return new HeartRateData(response.getValue());
    }
}