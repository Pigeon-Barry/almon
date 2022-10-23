package com.capgemini.bedwards.almon.almoncore.intergrations.api.error;

import lombok.Getter;

@Getter

public class InternalServerErrorResponse {
    private ErrorCode code = ErrorCode.INTERNAL_SERVER_ERROR;
    private String description = code.getErrorDescription();

}
