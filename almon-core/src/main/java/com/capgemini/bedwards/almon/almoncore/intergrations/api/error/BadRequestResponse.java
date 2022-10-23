package com.capgemini.bedwards.almon.almoncore.intergrations.api.error;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BadRequestResponse {
    private String field;
    private String error;
}
