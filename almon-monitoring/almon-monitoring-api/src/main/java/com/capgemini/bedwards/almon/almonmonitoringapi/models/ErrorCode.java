package com.capgemini.bedwards.almon.almonmonitoringapi.models;

public enum ErrorCode {
    BAD_REQUEST("Desc");

    private final String ERROR_DESCRIPTION;

    ErrorCode( String errorDescription) {
        this.ERROR_DESCRIPTION = errorDescription;
    }

    public String getErrorDescription() {
        return this.ERROR_DESCRIPTION;
    }
}
