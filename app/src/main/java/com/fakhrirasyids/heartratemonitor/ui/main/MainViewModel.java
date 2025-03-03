package com.fakhrirasyids.heartratemonitor.ui.main;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fakhrirasyids.heartratemonitor.core.domain.model.HeartRateData;
import com.fakhrirasyids.heartratemonitor.core.domain.usecase.FetchHeartRateUseCase;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class MainViewModel extends ViewModel {
    private final FetchHeartRateUseCase fetchHeartRateUseCase;

    private final MutableLiveData<HeartRateData> heartRateLiveData = new MutableLiveData<>();
    private final CompositeDisposable disposables = new CompositeDisposable();

    @Inject
    public MainViewModel(FetchHeartRateUseCase fetchHeartRateUseCase) {
        this.fetchHeartRateUseCase = fetchHeartRateUseCase;

        startFetchingHeartRate();
    }

    public void startFetchingHeartRate() {
        disposables.add(fetchHeartRateUseCase.execute()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> {
                    heartRateLiveData.setValue(data);
                    checkForAbnormalValue(data);
                }, throwable -> {
                    Log.e("HealthViewModel", "Error fetching data", throwable);
                }));
    }

    public void stopFetchingHeartRate() {
        disposables.clear();
    }

    private void checkForAbnormalValue(HeartRateData data) {
        if (data.getHeartRateBpm() > 100 || data.getHeartRateBpm() < 60) {
            // Notification Implementation
        }
    }

    public LiveData<HeartRateData> getHeartRateLiveData() {
        return heartRateLiveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        stopFetchingHeartRate();
    }
}
