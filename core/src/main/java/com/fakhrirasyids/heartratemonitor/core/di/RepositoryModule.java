package com.fakhrirasyids.heartratemonitor.core.di;

import android.bluetooth.BluetoothAdapter;

import com.fakhrirasyids.heartratemonitor.core.data.datasource.remote.HealthApiService;
import com.fakhrirasyids.heartratemonitor.core.data.repository.BleRepositoryImpl;
import com.fakhrirasyids.heartratemonitor.core.data.repository.HealthRepositoryImpl;
import com.fakhrirasyids.heartratemonitor.core.domain.repository.BleRepository;
import com.fakhrirasyids.heartratemonitor.core.domain.repository.HealthRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RepositoryModule {
    @Provides
    @Singleton
    public static HealthRepository provideHealthRepository(HealthApiService apiService) {
        return new HealthRepositoryImpl(apiService);
    }

    @Provides
    @Singleton
    BleRepository provideBleRepository(BluetoothAdapter bluetoothAdapter) {
        return new BleRepositoryImpl(bluetoothAdapter);
    }
}
