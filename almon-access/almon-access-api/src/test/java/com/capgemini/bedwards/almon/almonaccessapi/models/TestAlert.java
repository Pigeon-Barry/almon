package com.capgemini.bedwards.almon.almonaccessapi.models;

import com.capgemini.bedwards.almon.almondatastore.models.alert.Alert;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class TestAlert extends Alert<TestMonitor> {
    @Override
    public String getLongMessage() {
        return getMessage();
    }

    @Override
    public String getHTMLMessage() {
        return "<b>" + getMessage() + "</b>";
    }
}
