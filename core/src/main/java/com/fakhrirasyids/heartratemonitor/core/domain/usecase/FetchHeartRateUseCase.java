package com.fakhrirasyids.heartratemonitor.core.domain.usecase;

import com.fakhrirasyids.heartratemonitor.core.domain.model.HeartRateData;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;

public interface FetchHeartRateUseCase {
    public Observable<HeartRateData> execute();
}

