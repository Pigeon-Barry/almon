package com.capgemini.bedwards.almon.almonmonitoringapi.models;

import com.capgemini.bedwards.almon.almoncore.util.MappingUtil;
import com.capgemini.bedwards.almon.almoncore.validators.ValidJsonPath;
import com.capgemini.bedwards.almon.almondatastore.models.service.Service;
import com.capgemini.bedwards.almon.almonmonitoringcore.models.CreateScheduledMonitorRequestBody;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Map;

@Data
@Slf4j
public class CreateAPIMonitorRequestBody extends CreateScheduledMonitorRequestBody {


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

    private Map<String, @ValidJsonPath String> jsonPathValidations;

    @SneakyThrows
    public static CreateAPIMonitorRequestBody from(ObjectNode objectNode) {
        return MappingUtil.getObjectMapper().treeToValue(objectNode, CreateAPIMonitorRequestBody.class);
    }

    public APIMonitor toAPIMonitor(Service service) {
        return toScheduledMonitor(APIMonitor.builder(), service)
                .url(url)
                .method(method)
                .body(body)
                .headers(headers)
                .jsonPathValidations(jsonPathValidations)
                .expectedStatus(expectedStatusCode)
                .build();
    }
}
