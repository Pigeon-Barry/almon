package com.capgemini.bedwards.almon.almonmonitoringactiveapi.models;

import com.capgemini.bedwards.almon.almoncore.util.MappingUtil;
import com.capgemini.bedwards.almon.almoncore.validators.ValidJsonPath;
import com.capgemini.bedwards.almon.almondatastore.models.service.Service;
import com.capgemini.bedwards.almon.almonmonitoringcore.models.CreateScheduledMonitorRequestBody;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Map;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class CreateActiveAPIMonitorRequestBody extends CreateScheduledMonitorRequestBody {


    @NotNull
    private HttpMethod method;
    @NotBlank
    private String url;

    private String body;

    @Min(100)
    @Max(999)
    @NotNull
    private Integer expectedStatusCode;

    @Min(1)
    @Max(Integer.MAX_VALUE)
    @NotNull
    private Integer maxResponseTime = 500;
    private Map<String, String> headers;

    private Map<String, @ValidJsonPath String> jsonPathValidations;

    @SneakyThrows
    public static CreateActiveAPIMonitorRequestBody from(ObjectNode objectNode) {
        return MappingUtil.getObjectMapper().treeToValue(objectNode, CreateActiveAPIMonitorRequestBody.class);
    }

    public ActiveAPIMonitor toAPIMonitor(Service service) {
        return toScheduledMonitor(ActiveAPIMonitor.builder(), service)
                .url(url)
                .method(method)
                .body(body)
                .headers(headers)
                .jsonPathValidations(jsonPathValidations)
                .expectedStatus(expectedStatusCode)
                .maxResponseTime(maxResponseTime)
                .build();
    }
}
