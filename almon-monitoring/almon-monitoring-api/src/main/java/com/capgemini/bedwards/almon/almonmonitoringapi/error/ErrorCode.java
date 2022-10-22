package com.capgemini.bedwards.almon.almonmonitoringapi.error;

import org.springframework.http.ResponseEntity;

public enum ErrorCode {
    NOT_FOUND("Not Found"),
    INTERNAL_SERVER_ERROR("Please contact system administrators"),

    UNAUTHORISED_WEB("You are not authorised to view this page"),
    UNAUTHORISED_API("You are not authorised");;

    private final String ERROR_DESCRIPTION;

    ErrorCode( String errorDescription) {
        this.ERROR_DESCRIPTION = errorDescription;
    }

    public String getErrorDescription() {
        return this.ERROR_DESCRIPTION;
    }


}
