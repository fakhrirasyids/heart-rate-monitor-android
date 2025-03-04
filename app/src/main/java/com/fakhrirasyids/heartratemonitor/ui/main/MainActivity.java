package com.fakhrirasyids.heartratemonitor.ui.main;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;

import com.fakhrirasyids.heartratemonitor.R;
import com.fakhrirasyids.heartratemonitor.databinding.ActivityMainBinding;
import com.fakhrirasyids.heartratemonitor.ui.customview.dialog.CustomDialog;
import com.fakhrirasyids.heartratemonitor.ui.customview.dialog.LoadingDialog;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

public class MainActivity extends DaggerAppCompatActivity {
    private ActivityMainBinding binding;

    @Inject
    MainViewModel mainViewModel;

    private final ActivityResultLauncher<String[]> permissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestMultiplePermissions(),
            result -> {
                boolean allGranted = true;
                for (Boolean granted : result.values()) {
                    if (!granted) {
                        allGranted = false;
                        break;
                    }
                }
                mainViewModel.updatePermissionStatus(allGranted);
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        observeViewModel();
        checkPermissions();
    }

    private void observeViewModel() {
        mainViewModel.isLoading.observe(this, isLoading -> {
            if (isLoading) {
                LoadingDialog.show(this);
            } else {
                LoadingDialog.hide();
            }
        });

        mainViewModel.heartRateLiveData.observe(this, heartRateData -> {
            if (heartRateData != null) {
                int heartRateBpm = heartRateData.getBpm();
                String heartRateZones = heartRateData.getZone().getDisplayName();
                boolean heartRateAbnormality = heartRateData.isAbnormal();

                binding.layoutHeartRate.setVisibility(View.VISIBLE);

                binding.tvHeartRate.setText(getString(R.string.heart_rate_display, heartRateBpm));
                binding.heartbeat.setDurationBasedOnBPM(heartRateBpm);
                binding.tvHeartRateZones.setText(heartRateZones);

                if (heartRateAbnormality) {
                    mainViewModel.processAlertNotification();
                }

                mainViewModel.sendHeartRateData(this, heartRateData);
            } else {
                binding.layoutHeartRate.setVisibility(View.GONE);
            }
        });

        mainViewModel.errorMessage.observe(this, errorMessage -> {
            if (errorMessage != null) {
                binding.layoutHeartRate.setVisibility(View.GONE);
                CustomDialog.show(
                        this,
                        ContextCompat.getString(this, R.string.error_text),
                        errorMessage,
                        ContextCompat.getString(this, R.string.retry_text),
                        mainViewModel::startFetchingHeartRate
                );
            }
        });

        mainViewModel.isPermissionGranted.observe(this, isGranted -> {
            if (isGranted) {
                mainViewModel.scanAndConnectToBleDevice(this);
            }
        });
    }

    private void checkPermissions() {
        List<String> permissions = new ArrayList<>();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.POST_NOTIFICATIONS);
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADVERTISE) != PackageManager.PERMISSION_GRANTED) {

                permissions.add(Manifest.permission.BLUETOOTH_CONNECT);
                permissions.add(Manifest.permission.BLUETOOTH_SCAN);
                permissions.add(Manifest.permission.BLUETOOTH_ADVERTISE);
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {

                permissions.add(Manifest.permission.BLUETOOTH);
                permissions.add(Manifest.permission.BLUETOOTH_ADMIN);
            }
        }

        if (!permissions.isEmpty()) {
            permissionLauncher.launch(permissions.toArray(new String[0]));
        } else {
            mainViewModel.updatePermissionStatus(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mainViewModel.errorMessage.getValue() == null) {
            binding.heartbeat.start();
            mainViewModel.startFetchingHeartRate();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        binding.heartbeat.stop();
        mainViewModel.stopFetchingHeartRate();
    }
}
