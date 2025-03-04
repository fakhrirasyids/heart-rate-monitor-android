package com.fakhrirasyids.heartratemonitor.core.domain.usecase.processnotification;

import com.fakhrirasyids.heartratemonitor.core.domain.model.ProcessedHeartRate;
import com.fakhrirasyids.heartratemonitor.core.utils.helper.NotificationHelper;

public class ProcessNotificationInteractor implements ProcessNotificationUseCase {
    private final NotificationHelper notificationHelper;

    public ProcessNotificationInteractor(NotificationHelper notificationHelper) {
        this.notificationHelper = notificationHelper;
    }

    @Override
    public void execute(ProcessedHeartRate processedHeartRate) {
        notificationHelper.showNotification(
                "Heart Rate Alert!",
                "Your heart rate is too high: " + processedHeartRate.getBpm() + " BPM"
        );
    }
}
