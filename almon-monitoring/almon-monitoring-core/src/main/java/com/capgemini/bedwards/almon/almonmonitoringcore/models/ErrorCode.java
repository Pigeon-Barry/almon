package com.capgemini.bedwards.almon.almonmonitoringcore.models;

public enum ErrorCode {
    NOT_FOUND("Not Found");

    private final String ERROR_DESCRIPTION;

    ErrorCode( String errorDescription) {
        this.ERROR_DESCRIPTION = errorDescription;
    }

    public String getErrorDescription() {
        return this.ERROR_DESCRIPTION;
    }
}
