package com.fakhrirasyids.heartratemonitor.di;

import com.fakhrirasyids.heartratemonitor.ui.main.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
abstract class ActivityModule {
    @ContributesAndroidInjector
    abstract MainActivity contributeMainActivity();
}
