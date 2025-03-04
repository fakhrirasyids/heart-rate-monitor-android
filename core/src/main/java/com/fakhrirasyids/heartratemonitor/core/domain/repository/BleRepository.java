package com.fakhrirasyids.heartratemonitor.core.domain.repository;

import android.content.Context;

public interface BleRepository {
    void scanAndConnect(Context context);
    void sendData(Context context,int heartRate);
}
