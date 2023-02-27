package com.capgemini.bedwards.almon.almonmonitoringpassiveapi.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PassiveAPIMonitorTest {
    @Test
    public void positive_getLongMessage() {
        PassiveAPIMonitor monitor = PassiveAPIMonitor.builder().build();
        PassiveAPIAlert alert = PassiveAPIAlert.builder()
                .monitor(monitor)
                .message("message")
                .build();
        assertEquals(alert.getMessage(), alert.getLongMessage());
    }

    @Test
    public void positive_getHTMLMessage() {
        PassiveAPIMonitor monitor = PassiveAPIMonitor.builder().build();
        PassiveAPIAlert alert = PassiveAPIAlert.builder()
                .monitor(monitor)
                .message("message")
                .build();
        assertEquals("<p><h3>Message: </h3>" + alert.getMessage() + "</p>", alert.getHTMLMessage());
    }

}
