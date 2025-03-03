package com.fakhrirasyids.heartratemonitor.core.domain.usecase;

import com.fakhrirasyids.heartratemonitor.core.domain.model.HeartRateData;
import com.fakhrirasyids.heartratemonitor.core.domain.repository.HealthRepository;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;

public class FetchHeartRateInteractor implements FetchHeartRateUseCase {
    private final HealthRepository repository;

    @Inject
    public FetchHeartRateInteractor(HealthRepository repository) {
        this.repository = repository;
    }

    public Observable<HeartRateData> execute() {
        return repository.fetchHeartRate();
    }
}

