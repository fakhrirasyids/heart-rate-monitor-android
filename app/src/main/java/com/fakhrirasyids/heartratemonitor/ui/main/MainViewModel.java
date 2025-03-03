package com.fakhrirasyids.heartratemonitor.ui.main;

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

    private final MutableLiveData<HeartRateData> _heartRateLiveData = new MutableLiveData<>();
    public LiveData<HeartRateData> heartRateLiveData = _heartRateLiveData;

    private final MutableLiveData<String> _heartRateZone = new MutableLiveData<>();
    public LiveData<String> heartRateZone = _heartRateZone;

    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>(true);
    public LiveData<Boolean> isLoading = _isLoading;

    private final MutableLiveData<String> _errorMessage = new MutableLiveData<>();
    public LiveData<String> errorMessage = _errorMessage;

    private final CompositeDisposable disposables = new CompositeDisposable();

    @Inject
    public MainViewModel(FetchHeartRateUseCase fetchHeartRateUseCase) {
        this.fetchHeartRateUseCase = fetchHeartRateUseCase;
    }

    public void startFetchingHeartRate() {
        _isLoading.postValue(true);
        disposables.add(fetchHeartRateUseCase.execute()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::processHeartRateData, this::handleError));
    }

    private void processHeartRateData(HeartRateData data) {
        _heartRateLiveData.postValue(data);
        classifyHeartRateZone(data.getHeartRateBpm());
        _isLoading.postValue(false);
    }

    private void classifyHeartRateZone(int bpm) {
        String zone;
        if (bpm <= 60) {
            zone = "Resting Zone";
        } else if (bpm <= 100) {
            zone = "Moderate Zone";
        } else {
            zone = "High Zone";
        }
        _heartRateZone.postValue(zone);
    }

    private void handleError(Throwable throwable) {
        _isLoading.postValue(false);
        _errorMessage.postValue("Failed to fetch heart rate data. Please check your connection.");

        Log.e("MainViewModel", "Error fetching data", throwable);
    }

    public void stopFetchingHeartRate() {
        disposables.clear();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        stopFetchingHeartRate();
    }
}
