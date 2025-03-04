package com.fakhrirasyids.heartratemonitor.core.domain.usecase.blesendandconnect;

import android.content.Context;

import com.fakhrirasyids.heartratemonitor.core.domain.model.ProcessedHeartRate;
import com.fakhrirasyids.heartratemonitor.core.domain.repository.BleRepository;

import javax.inject.Inject;

public class BleSendAndConnectInteractor implements BleSendAndConnectUseCase {
    private final BleRepository repository;

    @Inject
    public BleSendAndConnectInteractor(BleRepository repository) {
        this.repository = repository;
    }

    @Override
    public void executeScanAndConnect(Context context) {
        repository.scanAndConnect(context);
    }

    @Override
    public void executeSendData(Context context, ProcessedHeartRate heartRate) {
        repository.sendData(context, heartRate);
    }
}

