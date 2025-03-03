package com.fakhrirasyids.heartratemonitor.ui.main;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.fakhrirasyids.heartratemonitor.R;
import com.fakhrirasyids.heartratemonitor.databinding.ActivityMainBinding;
import com.fakhrirasyids.heartratemonitor.utils.ErrorDialog;
import com.fakhrirasyids.heartratemonitor.utils.LoadingDialog;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

public class MainActivity extends DaggerAppCompatActivity {
    private ActivityMainBinding binding;

    @Inject
    MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        observeViewModel();
        mainViewModel.startFetchingHeartRate();
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
                binding.layoutHeartRate.setVisibility(View.VISIBLE);

                binding.tvHeartRate.setText(getString(R.string.heart_rate_display, heartRateData.getHeartRateBpm()));
                binding.heartbeat.setDurationBasedOnBPM(heartRateData.getHeartRateBpm());

                if (heartRateData.getHeartRateBpm() > 100 || heartRateData.getHeartRateBpm() < 60) {
                    showAbnormalNotification();
                }
            } else {
                binding.layoutHeartRate.setVisibility(View.GONE);
            }
        });

        mainViewModel.heartRateZone.observe(this, heartRateZone -> {
            binding.tvHeartRateZones.setText(heartRateZone);
        });

        mainViewModel.errorMessage.observe(this, errorMessage -> {
            if (errorMessage != null) {
                ErrorDialog.show(this, errorMessage, () -> mainViewModel.startFetchingHeartRate());
            }
        });
    }

    private void showAbnormalNotification() {
        Toast.makeText(this, "Warning: Abnormal Heart Rate!", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.heartbeat.toggle();
        mainViewModel.startFetchingHeartRate();
    }

    @Override
    protected void onPause() {
        super.onPause();
        binding.heartbeat.toggle();
        mainViewModel.stopFetchingHeartRate();
    }
}
