package com.fakhrirasyids.heartratemonitor.core.domain.usecase.fetchheartrate;

import com.fakhrirasyids.heartratemonitor.core.domain.model.ProcessedHeartRate;

import io.reactivex.rxjava3.core.Observable;

public interface FetchHeartRateUseCase {
    Observable<ProcessedHeartRate> execute();
}

