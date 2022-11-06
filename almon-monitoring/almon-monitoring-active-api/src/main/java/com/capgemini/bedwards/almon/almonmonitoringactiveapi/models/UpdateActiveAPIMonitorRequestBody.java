package com.capgemini.bedwards.almon.almonmonitoringactiveapi.models;

import com.capgemini.bedwards.almon.almoncore.util.MappingUtil;
import com.capgemini.bedwards.almon.almoncore.validators.ValidJsonPath;
import com.capgemini.bedwards.almon.almonmonitoringcore.models.UpdateScheduledMonitorRequestBody;
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
public class UpdateActiveAPIMonitorRequestBody extends UpdateScheduledMonitorRequestBody {


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
    public static UpdateActiveAPIMonitorRequestBody from(ObjectNode objectNode) {
        return MappingUtil.getObjectMapper().treeToValue(objectNode, UpdateActiveAPIMonitorRequestBody.class);
    }

    public ActiveAPIMonitor updateAPIMonitor(ActiveAPIMonitor activeApiMonitor) {
        activeApiMonitor = toScheduledMonitor(activeApiMonitor);
        if (this.getUrl() != null)
            activeApiMonitor.setUrl(this.getUrl());
        if (this.getMethod() != null)
            activeApiMonitor.setMethod(this.getMethod());
        if (this.getBody() != null)
            activeApiMonitor.setBody(this.getBody());
        if (this.getHeaders() != null)
            activeApiMonitor.setHeaders(this.getHeaders());
        if (this.getJsonPathValidations() != null)
            activeApiMonitor.setJsonPathValidations(this.getJsonPathValidations());
        if (this.getExpectedStatusCode() != null)
            activeApiMonitor.setExpectedStatus(this.getExpectedStatusCode());
        return activeApiMonitor;
    }

    public UpdateActiveAPIMonitorRequestBody populate(ActiveAPIMonitor monitor) {
        super.populate(monitor);
        this.setMethod(monitor.getMethod());
        this.setUrl(monitor.getUrl());
        this.setBody(monitor.getBody());
        this.setExpectedStatusCode(monitor.getExpectedStatus());
        this.setHeaders(monitor.getHeaders());
        this.setJsonPathValidations(monitor.getJsonPathValidations());
        return this;
    }
}
