package com.fakhrirasyids.heartratemonitor.core.domain.repository;

import com.fakhrirasyids.heartratemonitor.core.domain.model.HeartRateData;

import io.reactivex.rxjava3.core.Observable;

public interface HealthRepository {
    Observable<HeartRateData> fetchHeartRate();
}