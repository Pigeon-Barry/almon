package com.capgemini.bedwards.almon.almonmonitoringapi.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter

public class InternalServerErrorResponse {
    private ErrorCode code = ErrorCode.INTERNAL_SERVER_ERROR;
    private String description = code.getErrorDescription();

}
