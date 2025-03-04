package com.fakhrirasyids.heartratemonitor.core.domain.usecase.processnotification;

import com.fakhrirasyids.heartratemonitor.core.domain.model.ProcessedHeartRate;

public interface ProcessNotificationUseCase {
    void execute(ProcessedHeartRate processedHeartRate);
}
