package com.fakhrirasyids.heartratemonitor.core.usecase;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.fakhrirasyids.heartratemonitor.core.domain.model.HeartRateData;
import com.fakhrirasyids.heartratemonitor.core.domain.model.ProcessedHeartRate;
import com.fakhrirasyids.heartratemonitor.core.domain.repository.HealthRepository;
import com.fakhrirasyids.heartratemonitor.core.domain.usecase.fetchheartrate.FetchHeartRateInteractor;
import com.fakhrirasyids.heartratemonitor.core.domain.usecase.fetchheartrate.FetchHeartRateUseCase;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class FetchHeartRateUseCaseTest {

    @Rule
    public InstantTaskExecutorRule rule = new InstantTaskExecutorRule();

    @Mock
    private HealthRepository healthRepository;

    private FetchHeartRateUseCase fetchHeartRateUseCase;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        fetchHeartRateUseCase = new FetchHeartRateInteractor(healthRepository);
    }

    @Test
    public void fetchHeartRate_Success() {
        // Arrange
        HeartRateData mockHeartRateData = new HeartRateData(75);
        when(healthRepository.fetchHeartRate()).thenReturn(Observable.just(mockHeartRateData));

        // Act
        ProcessedHeartRate processedHeartRate = fetchHeartRateUseCase.execute()
                .subscribeOn(Schedulers.trampoline())
                .blockingFirst();

        // Assert
        assertNotNull(processedHeartRate);
        assertEquals(75, processedHeartRate.getBpm());
        assertFalse(processedHeartRate.isAbnormal());
    }

    @Test
    public void fetchHeartRate_ApiError() {
        // Arrange
        when(healthRepository.fetchHeartRate()).thenReturn(Observable.error(new RuntimeException("API Error")));

        // Act & Assert
        try {
            fetchHeartRateUseCase.execute()
                    .subscribeOn(Schedulers.trampoline())
                    .blockingFirst();
            fail("Expected an exception");
        } catch (Exception e) {
            assertEquals("API Error", e.getMessage());
        }
    }
}

