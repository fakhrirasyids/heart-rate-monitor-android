package com.fakhrirasyids.heartratemonitor.di;

import android.app.Application;

import com.fakhrirasyids.heartratemonitor.HeartRateMonitorApp;
import com.fakhrirasyids.heartratemonitor.core.di.CoreModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;

@Singleton
@Component(modules = {AndroidInjectionModule.class, CoreModule.class, ActivityModule.class})
public interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }

    void inject(HeartRateMonitorApp heartRateMonitorApp);
}
