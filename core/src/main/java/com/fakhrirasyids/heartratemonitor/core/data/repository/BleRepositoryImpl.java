package com.fakhrirasyids.heartratemonitor.core.data.repository;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.fakhrirasyids.heartratemonitor.core.BuildConfig;
import com.fakhrirasyids.heartratemonitor.core.domain.model.ProcessedHeartRate;
import com.fakhrirasyids.heartratemonitor.core.domain.repository.BleRepository;
import com.fakhrirasyids.heartratemonitor.core.domain.model.HeartRateZones;

import java.util.UUID;

import javax.inject.Inject;

public class BleRepositoryImpl implements BleRepository {
    private BluetoothGatt bluetoothGatt;
    private final BluetoothAdapter bluetoothAdapter;
    private final BluetoothLeScanner scanner;
    private ScanCallback scanCallback;

    @Inject
    public BleRepositoryImpl(BluetoothAdapter bluetoothAdapter) {
        this.bluetoothAdapter = bluetoothAdapter;
        this.scanner = bluetoothAdapter.getBluetoothLeScanner();
    }

    @Override
    public void scanAndConnect(Context context) {
        if (scanner == null) {
            Log.e("BLE", "BLE Scanner not available!");
            return;
        }

        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Log.e("BLE", "Bluetooth is disabled!");
            return;
        }

        scanCallback = new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult result) {
                BluetoothDevice device = result.getDevice();

                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    Log.e("BLE", "Bluetooth connect permission not granted!");
                    return;
                }

                if (BuildConfig.BLE_DEVICE_NAME.equals(device.getName())) {
                    Log.d("BLE", BuildConfig.BLE_DEVICE_NAME + " found, stopping scan and connecting...");
                    scanner.stopScan(this);
                    connectToDevice(context, device);
                }
            }

            @Override
            public void onScanFailed(int errorCode) {
                Log.e("BLE", "Scan failed with error: " + errorCode);
            }
        };

        scanner.startScan(scanCallback);
        Log.d("BLE", "Scanning for devices...");
    }

    public void connectToDevice(Context context, BluetoothDevice device) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            Log.e("BLE", "Permission not granted!");
            return;
        }

        bluetoothGatt = device.connectGatt(context, false, new BluetoothGattCallback() {
            @Override
            public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }

                    Log.d("BLE", "Connected to device: " + device.getName());
                    gatt.discoverServices();
                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    Log.e("BLE", "Disconnected! Re-scanning...");
                    scanner.startScan(scanCallback);
                }
            }
        });
    }

    @Override
    public void sendData(Context context, ProcessedHeartRate heartRate) {
        if (bluetoothGatt == null) {
            Log.e("BLE", "Bluetooth GATT is not connected!");
            simulateDeviceCommunication(String.valueOf(heartRate.getBpm()), heartRate.getZone().getDisplayName());
            return;
        }

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            Log.e("BLE", "Permission not granted!");
            simulateDeviceCommunication(String.valueOf(heartRate.getBpm()), heartRate.getZone().getDisplayName());
            return;
        }

        UUID serviceUUID = UUID.fromString(BuildConfig.HEART_RATE_SERVICE_UUID);
        UUID characteristicUUID = UUID.fromString(BuildConfig.HEART_RATE_CHARACTERISTIC_UUID);

        BluetoothGattService service = bluetoothGatt.getService(serviceUUID);
        if (service == null) {
            Log.e("BLE", "Heart Rate Service not found!");
            simulateDeviceCommunication(String.valueOf(heartRate.getBpm()), heartRate.getZone().getDisplayName());
            return;
        }

        BluetoothGattCharacteristic characteristic = service.getCharacteristic(characteristicUUID);
        if (characteristic == null) {
            Log.e("BLE", "Heart Rate Characteristic not found!");
            simulateDeviceCommunication(String.valueOf(heartRate.getBpm()), heartRate.getZone().getDisplayName());
            return;
        }

        byte[] heartRateData = new byte[]{(byte) heartRate.getBpm(), (byte) heartRate.getZone().getIdx()};
        characteristic.setValue(heartRateData);

        boolean success = bluetoothGatt.writeCharacteristic(characteristic);
        if (success) {
            Log.d("BLE", "Heart Rate sent: " + heartRate);
        } else {
            Log.e("BLE", "Failed to send Heart Rate!");
            simulateDeviceCommunication(String.valueOf(heartRate.getBpm()), heartRate.getZone().getDisplayName());
        }
    }

    /**
     * Simulate Bluetooth Connection with log if Bluetooth is not available.
     */
    private void simulateDeviceCommunication(String heartRate, String zones) {
        Log.w("BLE", "Simulating device communication: (Heart Rate sent: " + heartRate + ", " + zones + ")");
    }
}
