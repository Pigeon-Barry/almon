package com.capgemini.bedwards.almon.almonmonitoringapi.models;

import com.capgemini.bedwards.almon.almoncore.util.MappingUtil;
import com.capgemini.bedwards.almon.almoncore.validators.CronExpression;
import com.capgemini.bedwards.almon.almondatastore.models.monitor.MonitoringType;
import com.capgemini.bedwards.almon.almondatastore.models.service.Service;
import com.capgemini.bedwards.almon.almondatastore.util.Constants;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;

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

    @CronExpression
    protected String cronExpression;

    protected String description;

    @NotNull
    private HttpMethod method;
    @NotBlank
    private String url;

    private String body;

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
                //Scheduled Class
                .cronExpression(cronExpression)
                //API Monitor Class
                .url(url)
                .method(method)
                .body(body)
                .headers(headers)
                .jsonPathValidations(jsonPathValidations)
                .expectedStatus(expectedStatusCode)
                .build();
    }
}
