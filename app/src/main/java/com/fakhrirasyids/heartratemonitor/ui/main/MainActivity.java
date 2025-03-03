package com.fakhrirasyids.heartratemonitor.ui.main;

import android.os.Bundle;

import com.fakhrirasyids.heartratemonitor.databinding.ActivityMainBinding;

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

        mainViewModel.getHeartRateLiveData().observe(this, heartRateData -> {
            binding.tvHeartRate.setText(String.valueOf(heartRateData.getHeartRateBpm()));
        });
    }
}