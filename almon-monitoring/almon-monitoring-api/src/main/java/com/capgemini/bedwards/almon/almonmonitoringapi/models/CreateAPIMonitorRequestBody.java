package com.capgemini.bedwards.almon.almonmonitoringapi.models;

import com.capgemini.bedwards.almon.almoncore.util.MappingUtil;
import com.capgemini.bedwards.almon.almondatastore.models.alert.MonitoringType;
import com.capgemini.bedwards.almon.almondatastore.models.service.Service;
import com.capgemini.bedwards.almon.almondatastore.util.Constants;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.*;
import java.util.Map;

@Data
@Slf4j
public class CreateAPIMonitorRequestBody {

    @NotBlank
    @Pattern(regexp = Constants.MONITOR_KEY_REGEX, message = Constants.MONITOR_KEY_REGEX_INVALID_MESSAGE)
    private String key;
    @NotBlank
    protected String name;

    protected String description;
    @NotBlank
    private String url;

    @Min(100)
    @Max(999)
    @NotNull
    private Integer expectedStatusCode;

    private Map<String, String> headers;

    private Map<String, String> jsonPathValidations;
//
//    private Map<String, String> xPathValidations;


    @SneakyThrows
    public static CreateAPIMonitorRequestBody from(ObjectNode objectNode) {
        return MappingUtil.getObjectMapper().treeToValue(objectNode, CreateAPIMonitorRequestBody.class);
    }

    public APIMonitoringType toAPIMonitoringType(Service service) {
        return APIMonitoringType.builder()
                //Base class
                .id(new MonitoringType.MonitoringTypeId(key, service))
                .name(name)
                .description(description)
                //API Monitor Class
                .url(url)
                .headers(headers)
                .jsonPathValidations(jsonPathValidations)
                .expectedStatus(expectedStatusCode)
                .build();
    }
}
