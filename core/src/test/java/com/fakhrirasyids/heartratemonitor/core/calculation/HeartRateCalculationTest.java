package com.fakhrirasyids.heartratemonitor.core.calculation;

import static org.junit.Assert.*;

import com.fakhrirasyids.heartratemonitor.core.domain.model.HeartRateData;
import com.fakhrirasyids.heartratemonitor.core.domain.model.HeartRateZones;
import com.fakhrirasyids.heartratemonitor.core.domain.model.ProcessedHeartRate;

import org.junit.Test;

public class HeartRateCalculationTest {

    @Test
    public void testHeartRateClassification() {
        ProcessedHeartRate lowRate = new ProcessedHeartRate(new HeartRateData(50));
        assertEquals(HeartRateZones.RESTING_ZONE, lowRate.getZone());
        assertTrue(lowRate.isAbnormal());

        ProcessedHeartRate moderateRate = new ProcessedHeartRate(new HeartRateData(85));
        assertEquals(HeartRateZones.MODERATE_ZONE, moderateRate.getZone());
        assertFalse(moderateRate.isAbnormal());

        ProcessedHeartRate highRate = new ProcessedHeartRate(new HeartRateData(120));
        assertEquals(HeartRateZones.HIGH_ZONE, highRate.getZone());
        assertTrue(highRate.isAbnormal());
    }
}
