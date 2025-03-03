package com.fakhrirasyids.heartratemonitor.core.di;

import com.fakhrirasyids.heartratemonitor.core.BuildConfig;
import com.fakhrirasyids.heartratemonitor.core.data.HealthRepositoryImpl;
import com.fakhrirasyids.heartratemonitor.core.data.remote.HealthApiService;
import com.fakhrirasyids.heartratemonitor.core.domain.repository.HealthRepository;
import com.fakhrirasyids.heartratemonitor.core.domain.usecase.FetchHeartRateInteractor;
import com.fakhrirasyids.heartratemonitor.core.domain.usecase.FetchHeartRateUseCase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class CoreModule {
    @Provides
    @Singleton
    HealthApiService provideHealthApiService(OkHttpClient okHttpClient, Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder
                .baseUrl(BuildConfig.APP_ENDPOINT)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build()
                .create(HealthApiService.class);
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(HttpLoggingInterceptor loggingInterceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(chain -> {
                    Request request = chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer demo")
                            .build();
                    return chain.proceed(request);
                })
                .build();
    }

    @Provides
    @Singleton
    HttpLoggingInterceptor provideLoggingInterceptor() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
        return loggingInterceptor;
    }

    @Provides
    @Singleton
    Retrofit.Builder provideRetrofitBuilder() {
        return new Retrofit.Builder();
    }

    @Provides
    @Singleton
    public static HealthRepository provideHealthRepository(HealthApiService apiService) {
        return new HealthRepositoryImpl(apiService);
    }

    @Provides
    @Singleton
    public static FetchHeartRateUseCase provideFetchHeartRateUseCase(HealthRepository healthRepository) {
        return new FetchHeartRateInteractor(healthRepository);
    }
}
