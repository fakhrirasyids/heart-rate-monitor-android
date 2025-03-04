package com.fakhrirasyids.heartratemonitor.core.di;

import android.bluetooth.BluetoothAdapter;

import com.fakhrirasyids.heartratemonitor.core.data.datasource.remote.HealthApiService;
import com.fakhrirasyids.heartratemonitor.core.data.repository.BleRepositoryImpl;
import com.fakhrirasyids.heartratemonitor.core.data.repository.HealthRepositoryImpl;
import com.fakhrirasyids.heartratemonitor.core.domain.repository.BleRepository;
import com.fakhrirasyids.heartratemonitor.core.domain.repository.HealthRepository;
import com.fakhrirasyids.heartratemonitor.core.domain.usecase.blesendandconnect.BleSendAndConnectInteractor;
import com.fakhrirasyids.heartratemonitor.core.domain.usecase.blesendandconnect.BleSendAndConnectUseCase;
import com.fakhrirasyids.heartratemonitor.core.domain.usecase.fetchheartrate.FetchHeartRateInteractor;
import com.fakhrirasyids.heartratemonitor.core.domain.usecase.fetchheartrate.FetchHeartRateUseCase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class UseCaseModule {
    @Provides
    @Singleton
    public static FetchHeartRateUseCase provideFetchHeartRateUseCase(HealthRepository healthRepository) {
        return new FetchHeartRateInteractor(healthRepository);
    }

    @Provides
    @Singleton
    public static BleSendAndConnectUseCase provideBleSendAndConnectUseCase(BleRepository bleRepository) {
        return new BleSendAndConnectInteractor(bleRepository);
    }
}
