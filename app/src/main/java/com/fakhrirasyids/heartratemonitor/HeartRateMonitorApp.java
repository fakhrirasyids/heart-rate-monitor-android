package com.fakhrirasyids.heartratemonitor;

import android.app.Application;

import com.fakhrirasyids.heartratemonitor.di.DaggerAppComponent;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasAndroidInjector;

public class HeartRateMonitorApp extends Application implements HasAndroidInjector {

    @Inject
    DispatchingAndroidInjector<Object> androidInjector;

    @Override
    public void onCreate() {
        super.onCreate();

        DaggerAppComponent.builder()
                .application(this)
                .build()
                .inject(this);
    }

    @Override
    public AndroidInjector<Object> androidInjector() {
        return androidInjector;
    }
}

