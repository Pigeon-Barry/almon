package com.capgemini.bedwards.almon.almonapi.error;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BadRequestResponse {
    private String field;
    private String error;
}
