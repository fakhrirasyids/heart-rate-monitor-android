package com.fakhrirasyids.heartratemonitor.core.domain.repository;

import android.content.Context;

import com.fakhrirasyids.heartratemonitor.core.utils.enums.HeartRateZones;

public interface BleRepository {
    void scanAndConnect(Context context);
    void sendData(Context context,int heartRate, HeartRateZones zones);
}
