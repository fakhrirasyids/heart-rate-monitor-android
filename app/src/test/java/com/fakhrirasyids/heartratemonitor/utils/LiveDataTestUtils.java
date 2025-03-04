package com.fakhrirasyids.heartratemonitor.utils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class LiveDataTestUtils {

    public static <T> T getOrAwaitValue(LiveData<T> liveData) throws TimeoutException, InterruptedException {
        return getOrAwaitValue(liveData, 2, TimeUnit.SECONDS);
    }

    public static <T> T getOrAwaitValue(LiveData<T> liveData, long time, TimeUnit timeUnit)
            throws TimeoutException, InterruptedException {

        final Object[] data = new Object[1];
        final CountDownLatch latch = new CountDownLatch(1);

        Observer<T> observer = new Observer<T>() {
            @Override
            public void onChanged(T value) {
                data[0] = value;
                latch.countDown();
                liveData.removeObserver(this);
            }
        };

        liveData.observeForever(observer);

        try {
            if (!latch.await(time, timeUnit)) {
                throw new TimeoutException("LiveData value was never set.");
            }
        } finally {
            liveData.removeObserver(observer);
        }

        return (T) data[0];
    }
}
