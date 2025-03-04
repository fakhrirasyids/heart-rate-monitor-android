package com.fakhrirasyids.heartratemonitor.core.di;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class BluetoothModule {
    @Provides
    @Singleton
    BluetoothManager provideBluetoothManager(Context context) {
        return (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
    }

    @Provides
    @Singleton
    BluetoothAdapter provideBluetoothAdapter(BluetoothManager bluetoothManager) {
        return bluetoothManager.getAdapter();
    }
}
