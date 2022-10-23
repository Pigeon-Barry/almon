package com.capgemini.bedwards.almon.almonmonitoringapi.models;

import com.capgemini.bedwards.almon.almoncore.util.ValidatorUtil;
import com.capgemini.bedwards.almon.almondatastore.util.Constants;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;

import javax.validation.constraints.*;
import java.util.Map;

@Data
public class CreateAPIMonitorRequestBody {

    @NotBlank
    @Pattern(regexp = Constants.MONITOR_KEY_REGEX, message = Constants.MONITOR_KEY_REGEX_INVALID_MESSAGE)
    private String key;


    @NotBlank
    private String url;

    @NotNull
    @Min(100)
    @Max(999)
    private Integer expectedStatusCode;


    private Map<String, String> header;

    private Map<String, String> jsonPathValidations;

    private Map<String, String> xPathValidations;


    public static CreateAPIMonitorRequestBody from(ObjectNode objectNode) {
        return ValidatorUtil.convertAndValidate(objectNode, CreateAPIMonitorRequestBody.class);
    }
}
