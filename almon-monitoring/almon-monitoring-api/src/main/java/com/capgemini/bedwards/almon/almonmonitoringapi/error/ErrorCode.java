package com.capgemini.bedwards.almon.almonmonitoringapi.error;

public enum ErrorCode {
    NOT_FOUND("Not Found"),
    INTERNAL_SERVER_ERROR("Please contact system administrators"),

    UNAUTHORISED("You are not authorised to view this page");

    private final String ERROR_DESCRIPTION;

    ErrorCode( String errorDescription) {
        this.ERROR_DESCRIPTION = errorDescription;
    }

    public String getErrorDescription() {
        return this.ERROR_DESCRIPTION;
    }

}
