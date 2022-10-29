package com.capgemini.bedwards.almon.almondatastore.models.alert;

public enum Status {
    PASS(false), FAIL(true), ERROR(true);

    private boolean SHOULD_SEND_ALERT;

    Status(boolean shouldSendAlert) {
        this.SHOULD_SEND_ALERT = shouldSendAlert;
    }

    public boolean shouldSendAlert() {
        return SHOULD_SEND_ALERT;
    }
}
