package com.fakhrirasyids.heartratemonitor.ui.main;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fakhrirasyids.heartratemonitor.core.domain.model.ProcessedHeartRate;
import com.fakhrirasyids.heartratemonitor.core.domain.usecase.blesendandconnect.BleSendAndConnectUseCase;
import com.fakhrirasyids.heartratemonitor.core.domain.usecase.fetchheartrate.FetchHeartRateUseCase;
import com.fakhrirasyids.heartratemonitor.core.domain.usecase.processnotification.ProcessNotificationUseCase;
import com.fakhrirasyids.heartratemonitor.utils.ErrorHandling;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class MainViewModel extends ViewModel {
    private final FetchHeartRateUseCase fetchHeartRateUseCase;
    private final BleSendAndConnectUseCase bleSendAndConnectUseCase;
    private final ProcessNotificationUseCase processNotificationUseCase;

    private final MutableLiveData<ProcessedHeartRate> _heartRateLiveData = new MutableLiveData<>();
    public LiveData<ProcessedHeartRate> heartRateLiveData = _heartRateLiveData;

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
            BleSendAndConnectUseCase bleSendAndConnectUseCase,
            ProcessNotificationUseCase processNotificationUseCase
    ) {
        this.fetchHeartRateUseCase = fetchHeartRateUseCase;
        this.bleSendAndConnectUseCase = bleSendAndConnectUseCase;
        this.processNotificationUseCase = processNotificationUseCase;
    }

    public void startFetchingHeartRate() {
        _isLoading.postValue(true);
        _errorMessage.postValue(null);
        disposables.add(fetchHeartRateUseCase.execute()
                .subscribeOn(Schedulers.trampoline())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::processHeartRateData, this::handleError));
    }

    private void processHeartRateData(ProcessedHeartRate data) {
        _heartRateLiveData.postValue(data);

        _errorMessage.postValue(null);
        _isLoading.postValue(false);
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
        bleSendAndConnectUseCase.executeScanAndConnect(context);
    }

    public void sendHeartRateData(Context context, ProcessedHeartRate heartRate) {
        bleSendAndConnectUseCase.executeSendData(context, heartRate);
    }

    public void processAlertNotification() {
        processNotificationUseCase.execute(heartRateLiveData.getValue());
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        stopFetchingHeartRate();
    }
}
