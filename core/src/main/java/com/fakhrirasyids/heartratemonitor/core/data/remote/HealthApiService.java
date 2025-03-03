package com.fakhrirasyids.heartratemonitor.core.data.remote;


import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;

public interface HealthApiService {
    @GET("heart_rate")
    Observable<HeartRateResponse> getHeartRate();
}