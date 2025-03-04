package com.fakhrirasyids.heartratemonitor.viewmodel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;

import com.fakhrirasyids.heartratemonitor.core.domain.model.HeartRateData;
import com.fakhrirasyids.heartratemonitor.core.domain.model.ProcessedHeartRate;
import com.fakhrirasyids.heartratemonitor.core.domain.usecase.blesendandconnect.BleSendAndConnectUseCase;
import com.fakhrirasyids.heartratemonitor.core.domain.usecase.fetchheartrate.FetchHeartRateUseCase;
import com.fakhrirasyids.heartratemonitor.ui.main.MainViewModel;
import com.fakhrirasyids.heartratemonitor.utils.LiveDataTestUtils;
import com.fakhrirasyids.heartratemonitor.utils.RxImmediateSchedulerRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.concurrent.TimeoutException;

import io.reactivex.rxjava3.core.Observable;

@RunWith(MockitoJUnitRunner.class)
public class MainViewModelTest {

    @Rule
    public RxImmediateSchedulerRule rxImmediateSchedulerRule = new RxImmediateSchedulerRule();

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock Context context;
    @Mock FetchHeartRateUseCase fetchHeartRateUseCase;
    @Mock BleSendAndConnectUseCase bleSendAndConnectUseCase;
    @Mock Observer<ProcessedHeartRate> heartRateObserver;
    @Mock Observer<String> errorObserver;
    @Mock Observer<Boolean> loadingObserver;

    @InjectMocks MainViewModel mainViewModel;

    @Before
    public void setUp() {
        mainViewModel.heartRateLiveData.observeForever(heartRateObserver);
        mainViewModel.errorMessage.observeForever(errorObserver);
        mainViewModel.isLoading.observeForever(loadingObserver);
    }

    @After
    public void tearDown() {
        mainViewModel.heartRateLiveData.removeObserver(heartRateObserver);
        mainViewModel.errorMessage.removeObserver(errorObserver);
        mainViewModel.isLoading.removeObserver(loadingObserver);
    }

    @Test
    public void testStartFetchingHeartRate_Success() {
        // Arrange
        ProcessedHeartRate mockData = new ProcessedHeartRate(new HeartRateData(90));
        when(fetchHeartRateUseCase.execute()).thenReturn(Observable.just(mockData));

        // Act
        mainViewModel.startFetchingHeartRate();

        // Assert
        InOrder inOrder = inOrder(loadingObserver, fetchHeartRateUseCase, heartRateObserver);
        inOrder.verify(loadingObserver).onChanged(true);
        inOrder.verify(fetchHeartRateUseCase).execute();
        inOrder.verify(heartRateObserver).onChanged(mockData);
        inOrder.verify(loadingObserver).onChanged(false);

        assertEquals(mockData, mainViewModel.heartRateLiveData.getValue());
    }

    @Test
    public void testStartFetchingHeartRate_Error() {
        // Arrange
        Throwable apiError = new RuntimeException("API Error");
        when(fetchHeartRateUseCase.execute()).thenReturn(Observable.error(apiError));

        // Act
        mainViewModel.startFetchingHeartRate();

        // Assert
        InOrder inOrder = inOrder(loadingObserver, fetchHeartRateUseCase);
        inOrder.verify(loadingObserver).onChanged(true);
        inOrder.verify(fetchHeartRateUseCase).execute();
        inOrder.verify(loadingObserver).onChanged(false);

        assertNotNull(mainViewModel.errorMessage.getValue());
    }

    @Test
    public void testSendHeartRateData() {
        // Arrange
        ProcessedHeartRate mockData = new ProcessedHeartRate(new HeartRateData(90));

        // Act
        mainViewModel.sendHeartRateData(context, mockData);

        // Assert
        ArgumentCaptor<ProcessedHeartRate> captor = ArgumentCaptor.forClass(ProcessedHeartRate.class);
        verify(bleSendAndConnectUseCase).executeSendData(eq(context), captor.capture());
        assertEquals(mockData, captor.getValue());
    }
}
