package com.fakhrirasyids.heartratemonitor.core.data.repository;

import com.fakhrirasyids.heartratemonitor.core.data.datasource.remote.HealthApiService;
import com.fakhrirasyids.heartratemonitor.core.domain.model.HeartRateData;
import com.fakhrirasyids.heartratemonitor.core.domain.repository.HealthRepository;
import com.fakhrirasyids.heartratemonitor.core.data.mapper.HeartRateMapper;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;

public class HealthRepositoryImpl implements HealthRepository {
    private final HealthApiService apiService;

    @Inject
    public HealthRepositoryImpl(HealthApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    public Observable<HeartRateData> fetchHeartRate() {
        return Observable.interval(1, TimeUnit.SECONDS)
                .flatMap(map -> apiService.getHeartRate())
                .map(HeartRateMapper::mapToHeartRateData);
    }
}
