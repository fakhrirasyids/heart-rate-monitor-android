package com.fakhrirasyids.heartratemonitor.core.usecase;

import static org.mockito.Mockito.*;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;

import com.fakhrirasyids.heartratemonitor.core.domain.model.HeartRateData;
import com.fakhrirasyids.heartratemonitor.core.domain.model.ProcessedHeartRate;
import com.fakhrirasyids.heartratemonitor.core.domain.repository.BleRepository;
import com.fakhrirasyids.heartratemonitor.core.domain.usecase.blesendandconnect.BleSendAndConnectInteractor;
import com.fakhrirasyids.heartratemonitor.core.domain.usecase.blesendandconnect.BleSendAndConnectUseCase;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


public class BleSendAndConnectUseCaseTest {

    @Mock
    private BleRepository bleRepository;

    @Mock
    private Context context;

    private BleSendAndConnectUseCase bleUseCase;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        bleUseCase = new BleSendAndConnectInteractor(bleRepository);
    }

    @Test
    public void testScanAndConnect() {
        // Act
        bleUseCase.executeScanAndConnect(context);

        // Assert
        verify(bleRepository).scanAndConnect(context);
    }

    @Test
    public void testSendData() {
        // Arrange
        ProcessedHeartRate mockHeartRate = new ProcessedHeartRate(new HeartRateData(80));

        // Act
        bleUseCase.executeSendData(context, mockHeartRate);

        // Assert
        verify(bleRepository).sendData(context, mockHeartRate);
    }
}
