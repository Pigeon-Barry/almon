package com.capgemini.bedwards.almon.almonmonitoringactiveapi.models;

import com.capgemini.bedwards.almon.almondatastore.models.alert.Status;
import com.capgemini.bedwards.almon.almondatastore.models.schedule.ScheduledTask;
import com.capgemini.bedwards.almon.almonmonitoringactiveapi.service.ActiveAPIAlertService;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
public class ActiveAPIMonitoringTask extends ScheduledTask<ActiveAPIAlert> {

    private final ActiveAPIMonitor API_MONITORING_TYPE;
    private final ActiveAPIAlertService API_ALERT_SERVICE;

    private final RestTemplate REST_TEMPLATE;

    public ActiveAPIMonitoringTask(ActiveAPIAlertService apiAlertService, ActiveAPIMonitor activeApiMonitor) {
        super(activeApiMonitor);
        this.API_MONITORING_TYPE = activeApiMonitor;
        this.API_ALERT_SERVICE = apiAlertService;
        this.REST_TEMPLATE = getRestTemplate();
    }

    private RestTemplate getRestTemplate() {
        RestTemplateBuilder builder = new RestTemplateBuilder();
        if (this.API_MONITORING_TYPE.getHeaders() != null)
            this.API_MONITORING_TYPE.getHeaders().forEach(builder::defaultHeader);
        return builder.build();
    }

    @Override
    public ActiveAPIAlert execute() {
        log.info("Running for: " + this.API_MONITORING_TYPE.getTaskId());
        try {
            final long START_TIME = System.currentTimeMillis();
            ResponseEntity<String> responseEntity = this.REST_TEMPLATE.getForEntity(this.API_MONITORING_TYPE.getUrl(), String.class);
            final long END_TIME = System.currentTimeMillis();
            Status status = Status.PASS;
            String message = null;

            if (log.isDebugEnabled())
                log.debug("Response: " + responseEntity);
            if (responseEntity.getStatusCode().equals(HttpStatus.valueOf(this.API_MONITORING_TYPE.expectedStatus))) {
                if (this.API_MONITORING_TYPE.getJsonPathValidations() != null && this.API_MONITORING_TYPE.getJsonPathValidations().size() > 0) {
                    Object jsonBody = Configuration.defaultConfiguration().jsonProvider().parse(responseEntity.getBody());

                    for (Map.Entry<String, String> jsonPathEntry : this.API_MONITORING_TYPE.getJsonPathValidations().entrySet()) {
                        JsonPath jsonPath = JsonPath.compile(jsonPathEntry.getKey());
                        try {
                            String value = jsonPath.read(jsonBody);
                            if (value.equals(jsonPathEntry.getValue()))
                                continue;
                            message = "Expected JsonPath '" + jsonPath.getPath() + "' to return '" + jsonPathEntry.getValue() + "' but instead it returned '" + value + "'";
                        } catch (PathNotFoundException e) {
                            log.error("Error: " + e.getMessage(), e);
                            message = "Path:  '" + jsonPath.getPath() + "' not found in body '" + jsonBody + "'";
                        }
                        status = Status.FAIL;
                        break;
                    }
                }
            } else {
                status = Status.FAIL;
                message = "Expected a status code of: " + this.API_MONITORING_TYPE.expectedStatus + " but got " + responseEntity.getStatusCodeValue();
            }
            return this.API_ALERT_SERVICE.create(ActiveAPIAlert.builder()
                    .monitor(this.API_MONITORING_TYPE)
                    .status(status)
                    .message(message)
                    .responseEntity(responseEntity)
                    .responseStatusCode(responseEntity.getStatusCodeValue())
                    .requestDurationMS(END_TIME - START_TIME)
                    .build());
        } catch (Throwable e) {
            log.error("Failed to run task: " + this.getTASK_ID(), e);
            return this.API_ALERT_SERVICE.create(ActiveAPIAlert.builder()
                    .monitor(this.API_MONITORING_TYPE)
                    .status(Status.ERROR)
                    .message("Monitor failed with error: " + e.getMessage())
                    .build());
        }
    }

    @Override
    public boolean isEnabled() {
        return this.API_MONITORING_TYPE.isEnabled();
    }
}
