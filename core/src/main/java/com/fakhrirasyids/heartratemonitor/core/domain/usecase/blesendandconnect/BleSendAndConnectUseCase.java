package com.fakhrirasyids.heartratemonitor.core.domain.usecase.blesendandconnect;

import android.content.Context;

import com.fakhrirasyids.heartratemonitor.core.domain.model.HeartRateData;

import io.reactivex.rxjava3.core.Observable;

public interface BleSendAndConnectUseCase {
    void scanAndConnect(Context context);
    void sendData(Context context,int heartRate);
}

