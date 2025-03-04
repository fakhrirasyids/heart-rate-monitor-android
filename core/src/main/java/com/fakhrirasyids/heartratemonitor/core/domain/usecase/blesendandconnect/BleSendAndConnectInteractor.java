package com.fakhrirasyids.heartratemonitor.core.domain.usecase.blesendandconnect;

import android.content.Context;

import com.fakhrirasyids.heartratemonitor.core.domain.model.HeartRateData;
import com.fakhrirasyids.heartratemonitor.core.domain.repository.BleRepository;
import com.fakhrirasyids.heartratemonitor.core.domain.repository.HealthRepository;
import com.fakhrirasyids.heartratemonitor.core.domain.usecase.fetchheartrate.FetchHeartRateUseCase;
import com.fakhrirasyids.heartratemonitor.core.utils.enums.HeartRateZones;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;

public class BleSendAndConnectInteractor implements BleSendAndConnectUseCase {
    private final BleRepository repository;

    @Inject
    public BleSendAndConnectInteractor(BleRepository repository) {
        this.repository = repository;
    }

    @Override
    public void scanAndConnect(Context context) {
        repository.scanAndConnect(context);
    }

    @Override
    public void sendData(Context context, int heartRate, HeartRateZones zones) {
        repository.sendData(context, heartRate, zones);
    }
}

