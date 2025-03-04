package com.fakhrirasyids.heartratemonitor.core.di;

import android.content.Context;

import com.fakhrirasyids.heartratemonitor.core.domain.repository.BleRepository;
import com.fakhrirasyids.heartratemonitor.core.domain.repository.HealthRepository;
import com.fakhrirasyids.heartratemonitor.core.domain.usecase.blesendandconnect.BleSendAndConnectInteractor;
import com.fakhrirasyids.heartratemonitor.core.domain.usecase.blesendandconnect.BleSendAndConnectUseCase;
import com.fakhrirasyids.heartratemonitor.core.domain.usecase.fetchheartrate.FetchHeartRateInteractor;
import com.fakhrirasyids.heartratemonitor.core.domain.usecase.fetchheartrate.FetchHeartRateUseCase;
import com.fakhrirasyids.heartratemonitor.core.domain.usecase.processnotification.ProcessNotificationInteractor;
import com.fakhrirasyids.heartratemonitor.core.domain.usecase.processnotification.ProcessNotificationUseCase;
import com.fakhrirasyids.heartratemonitor.core.utils.helper.NotificationHelper;

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

    @Provides
    @Singleton
    public static ProcessNotificationUseCase provideProcessNotificationUseCase(NotificationHelper notificationHelper) {
        return new ProcessNotificationInteractor(notificationHelper);
    }

    @Provides
    @Singleton
    NotificationHelper provideNotificationHelper(Context context) {
        return new NotificationHelper(context);
    }
}
