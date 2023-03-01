package com.capgemini.bedwards.almon.almonaccessapi.models;

import com.capgemini.bedwards.almon.almondatastore.models.alert.ScheduledAlert;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class TestScheduledAlert extends ScheduledAlert<TestScheduledMonitor> {
    @Override
    public String getLongMessage() {
        return getMessage();
    }

    @Override
    public String getHTMLMessage() {
        return "<b>" + getMessage() + "</b>";
    }
}
