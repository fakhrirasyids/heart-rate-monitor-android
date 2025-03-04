package com.fakhrirasyids.heartratemonitor.ui.main;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.fakhrirasyids.heartratemonitor.R;
import com.fakhrirasyids.heartratemonitor.databinding.ActivityMainBinding;
import com.fakhrirasyids.heartratemonitor.utils.CustomDialog;
import com.fakhrirasyids.heartratemonitor.utils.LoadingDialog;

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
                int heartRateBpm = 110;

                binding.layoutHeartRate.setVisibility(View.VISIBLE);

                binding.tvHeartRate.setText(getString(R.string.heart_rate_display, heartRateBpm));
                binding.heartbeat.setDurationBasedOnBPM(heartRateBpm);

                if (heartRateData.getHeartRateBpm() > 100 || heartRateData.getHeartRateBpm() < 60) {
                    showAbnormalHeartRateNotification(heartRateData.getHeartRateBpm());
                }

                mainViewModel.sendHeartRateData(this, heartRateBpm);
            } else {
                binding.layoutHeartRate.setVisibility(View.GONE);
            }
        });

        mainViewModel.heartRateZone.observe(this, heartRateZone -> binding.tvHeartRateZones.setText(heartRateZone));

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

    private void showAbnormalHeartRateNotification(int bpm) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = "heart_rate_alert";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Heart Rate Alerts", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_heart)
                .setContentTitle("Abnormal Heart Rate!")
                .setContentText("Heart rate detected: " + bpm + " BPM")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        notificationManager.notify(1, builder.build());
    }

    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADVERTISE) != PackageManager.PERMISSION_GRANTED
            ) {

                permissionLauncher.launch(new String[]{
                        Manifest.permission.BLUETOOTH_CONNECT,
                        Manifest.permission.BLUETOOTH_SCAN,
                        Manifest.permission.BLUETOOTH_ADVERTISE,
                });
            } else {
                mainViewModel.updatePermissionStatus(true);
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {

                permissionLauncher.launch(new String[]{
                        Manifest.permission.BLUETOOTH,
                        Manifest.permission.BLUETOOTH_ADMIN
                });
            } else {
                mainViewModel.updatePermissionStatus(true);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.heartbeat.start();

        if (mainViewModel.errorMessage.getValue() == null) {
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
