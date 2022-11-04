package com.capgemini.bedwards.almon.almonmonitoringapi.models;

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
public class UpdateAPIMonitorRequestBody extends UpdateScheduledMonitorRequestBody {


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
    public static UpdateAPIMonitorRequestBody from(ObjectNode objectNode) {
        return MappingUtil.getObjectMapper().treeToValue(objectNode, UpdateAPIMonitorRequestBody.class);
    }

    public APIMonitor updateAPIMonitor(APIMonitor apiMonitor) {
        apiMonitor = toScheduledMonitor(apiMonitor);
        if (this.getUrl() != null)
            apiMonitor.setUrl(this.getUrl());
        if (this.getMethod() != null)
            apiMonitor.setMethod(this.getMethod());
        if (this.getBody() != null)
            apiMonitor.setBody(this.getBody());
        if (this.getHeaders() != null)
            apiMonitor.setHeaders(this.getHeaders());
        if (this.getJsonPathValidations() != null)
            apiMonitor.setJsonPathValidations(this.getJsonPathValidations());
        if (this.getExpectedStatusCode() != null)
            apiMonitor.setExpectedStatus(this.getExpectedStatusCode());
        return apiMonitor;
    }

    public UpdateAPIMonitorRequestBody populate(APIMonitor monitor) {
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
