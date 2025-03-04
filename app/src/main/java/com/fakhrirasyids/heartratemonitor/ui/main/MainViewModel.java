package com.fakhrirasyids.heartratemonitor.ui.main;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fakhrirasyids.heartratemonitor.core.domain.model.HeartRateData;
import com.fakhrirasyids.heartratemonitor.core.domain.usecase.blesendandconnect.BleSendAndConnectUseCase;
import com.fakhrirasyids.heartratemonitor.core.domain.usecase.fetchheartrate.FetchHeartRateUseCase;
import com.fakhrirasyids.heartratemonitor.core.utils.enums.HeartRateZones;
import com.fakhrirasyids.heartratemonitor.utils.ErrorHandling;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class MainViewModel extends ViewModel {
    private final FetchHeartRateUseCase fetchHeartRateUseCase;
    private final BleSendAndConnectUseCase bleSendAndConnectUseCase;

    private final MutableLiveData<HeartRateData> _heartRateLiveData = new MutableLiveData<>();
    public LiveData<HeartRateData> heartRateLiveData = _heartRateLiveData;

    private final MutableLiveData<HeartRateZones> _heartRateZone = new MutableLiveData<>();
    public LiveData<HeartRateZones> heartRateZone = _heartRateZone;

    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>(false);
    public LiveData<Boolean> isLoading = _isLoading;

    private final MutableLiveData<String> _errorMessage = new MutableLiveData<>();
    public LiveData<String> errorMessage = _errorMessage;

    private final MutableLiveData<Boolean> _isPermissionGranted = new MutableLiveData<>(false);
    public LiveData<Boolean> isPermissionGranted = _isPermissionGranted;

    private final CompositeDisposable disposables = new CompositeDisposable();

    @Inject
    public MainViewModel(
            FetchHeartRateUseCase fetchHeartRateUseCase,
            BleSendAndConnectUseCase bleSendAndConnectUseCase
    ) {
        this.fetchHeartRateUseCase = fetchHeartRateUseCase;
        this.bleSendAndConnectUseCase = bleSendAndConnectUseCase;
    }

    public void startFetchingHeartRate() {
        _isLoading.postValue(true);
        disposables.add(fetchHeartRateUseCase.execute()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::processHeartRateData, this::handleError));
    }

    private void processHeartRateData(HeartRateData data) {
        classifyHeartRateZone(data.getHeartRateBpm());
        _heartRateLiveData.postValue(data);

        _errorMessage.postValue(null);
        _isLoading.postValue(false);
    }

    private void classifyHeartRateZone(int bpm) {
        HeartRateZones zone = (bpm <= 60) ? HeartRateZones.RESTING_ZONE : (bpm <= 100) ? HeartRateZones.MODERATE_ZONE : HeartRateZones.HIGH_ZONE;
        _heartRateZone.postValue(zone);
    }

    private void handleError(Throwable throwable) {
        _isLoading.postValue(false);
        _errorMessage.postValue(ErrorHandling.parseError(throwable));

        Log.e("MainViewModel", "Error fetching data", throwable);
    }

    public void stopFetchingHeartRate() {
        disposables.clear();
    }

    public void updatePermissionStatus(boolean isGranted) {
        _isPermissionGranted.setValue(isGranted);
    }

    public void scanAndConnectToBleDevice(Context context) {
        bleSendAndConnectUseCase.scanAndConnect(context);
    }

    public void sendHeartRateData(Context context, int bpm, HeartRateZones zones) {
        bleSendAndConnectUseCase.sendData(context, bpm, zones);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        stopFetchingHeartRate();
    }
}
