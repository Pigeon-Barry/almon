package com.capgemini.bedwards.almon.almoncore.intergrations.api.error;

public enum ErrorCode {
    NOT_FOUND("Not Found"),
    INVALID_CORRELATIONID("Invalid CorrelationId"),
    NOT_SUBSCRIBED("You are not subscribed"),
    ALREADY_SUBSCRIBED("You are already subscribed"),
    INTERNAL_SERVER_ERROR("Please contact system administrators"),

    UNAUTHORISED_WEB("You are not authorised to view this page"),
    UNAUTHORISED_API("You are not authorised"),
    INVALID_PAYLOAD("Payload is invalid");

    private final String ERROR_DESCRIPTION;

    ErrorCode(String errorDescription) {
        this.ERROR_DESCRIPTION = errorDescription;
    }

    public String getErrorDescription() {
        return this.ERROR_DESCRIPTION;
    }


}
