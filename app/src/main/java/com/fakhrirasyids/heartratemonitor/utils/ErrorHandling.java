package com.fakhrirasyids.heartratemonitor.utils;

import android.bluetooth.BluetoothSocketException;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import retrofit2.HttpException;

public class ErrorHandling {
    public static String parseError(Throwable throwable) {
        String errorMessage;

        if (throwable instanceof HttpException) {
            int errorCode = ((HttpException) throwable).code();
            switch (errorCode) {
                case 400:
                    errorMessage = "Bad request. Please check your request format.";
                    break;
                case 401:
                    errorMessage = "Unauthorized access. Please log in again.";
                    break;
                case 403:
                    errorMessage = "Access forbidden. You don’t have permission.";
                    break;
                case 404:
                    errorMessage = "Data not found. Please check the device or API.";
                    break;
                case 500:
                    errorMessage = "Server error. Try again later.";
                    break;
                default:
                    errorMessage = "Unexpected error occurred. Code: " + errorCode;
                    break;
            }
        } else if (throwable instanceof IOException) {
            errorMessage = "Network error. Please check your connection.";
        } else if (throwable instanceof TimeoutException) {
            errorMessage = "Request timeout. Try again later.";
        } else {
            errorMessage = "Unknown error occurred.";
        }

        return errorMessage;
    }
}
