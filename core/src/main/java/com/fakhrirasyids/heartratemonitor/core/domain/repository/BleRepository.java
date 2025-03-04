package com.fakhrirasyids.heartratemonitor.core.domain.repository;

import android.content.Context;

import com.fakhrirasyids.heartratemonitor.core.domain.model.HeartRateZones;
import com.fakhrirasyids.heartratemonitor.core.domain.model.ProcessedHeartRate;

public interface BleRepository {
    void scanAndConnect(Context context);

    void sendData(Context context, ProcessedHeartRate heartRate);
}
