package com.fakhrirasyids.heartratemonitor.core.domain.usecase.blesendandconnect;

import android.content.Context;

import com.fakhrirasyids.heartratemonitor.core.domain.model.ProcessedHeartRate;

public interface BleSendAndConnectUseCase {
    void executeScanAndConnect(Context context);

    void executeSendData(Context context, ProcessedHeartRate heartRate);
}

