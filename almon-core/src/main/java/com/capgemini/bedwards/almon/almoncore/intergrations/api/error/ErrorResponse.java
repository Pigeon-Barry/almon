package com.capgemini.bedwards.almon.almoncore.intergrations.api.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {
    private ErrorCode code;
    private String description;


    public ErrorResponse(ErrorCode errorCode) {
        this.code = errorCode;
        this.description = this.code.getErrorDescription();
    }
}
