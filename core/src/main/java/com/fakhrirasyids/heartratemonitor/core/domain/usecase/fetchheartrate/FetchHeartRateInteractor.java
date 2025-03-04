package com.fakhrirasyids.heartratemonitor.core.domain.usecase.fetchheartrate;

import com.fakhrirasyids.heartratemonitor.core.domain.model.ProcessedHeartRate;
import com.fakhrirasyids.heartratemonitor.core.domain.repository.HealthRepository;
import com.fakhrirasyids.heartratemonitor.core.utils.mapper.ProcessedHeartRateMapper;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;

public class FetchHeartRateInteractor implements FetchHeartRateUseCase {
    private final HealthRepository repository;

    @Inject
    public FetchHeartRateInteractor(HealthRepository repository) {
        this.repository = repository;
    }

    @Override
    public Observable<ProcessedHeartRate> execute() {
        return repository.fetchHeartRate()
                .map(ProcessedHeartRateMapper::mapToProcessedHeartRate)
                .onErrorResumeNext(Observable::error);
    }
}

