package com.capgemini.bedwards.almon.almonmonitoringapi.models;

import com.capgemini.bedwards.almon.almondatastore.models.alert.Status;
import com.capgemini.bedwards.almon.almondatastore.models.schedule.ScheduledTask;
import com.capgemini.bedwards.almon.almonmonitoringapi.service.APIAlertService;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
public class APIMonitoringTask extends ScheduledTask<APIAlertType> {

    private final APIMonitoringType API_MONITORING_TYPE;
    private final APIAlertService API_ALERT_SERVICE;

    private final RestTemplate REST_TEMPLATE;

    public APIMonitoringTask(APIAlertService apiAlertService, APIMonitoringType apiMonitoringType) {
        super(apiMonitoringType);
        this.API_MONITORING_TYPE = apiMonitoringType;
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
    public void run() {
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
                    DocumentContext jsonBody = JsonPath.parse(responseEntity.getBody());

                    ObjectMapper mapper = new ObjectMapper();
                    JsonFactory factory = mapper.getFactory();

                    for (Map.Entry<String, String> jsonPathEntry : this.API_MONITORING_TYPE.getJsonPathValidations().entrySet()) {
                        JsonPath jsonPath = JsonPath.compile(jsonPathEntry.getKey());
                        JsonNode value = jsonBody.read(jsonPath);
                        if (!value.equals(mapper.readTree(factory.createParser(jsonPathEntry.getValue())))) {
                            status = Status.FAIL;
                            message = "One or more Json assertions failed: Response body '" + responseEntity.getBody() + "'";
                            break;
                        }
                    }
                }
            } else {
                status = Status.FAIL;
                message = "Expected a status code of: " + this.API_MONITORING_TYPE.expectedStatus + " but got " + responseEntity.getStatusCodeValue();
            }

            this.API_ALERT_SERVICE.create(APIAlertType.builder()
                    .monitoringType(this.API_MONITORING_TYPE)
                    .status(status)
                    .message(message)
                    .responseEntity(responseEntity)
                    .responseStatusCode(responseEntity.getStatusCodeValue())
                    .requestDurationMS(END_TIME - START_TIME)
                    .build());
        } catch (Throwable e) {
            log.error("Failed to run task: " + this.getTASK_ID(), e);
            this.API_ALERT_SERVICE.create(APIAlertType.builder()
                    .monitoringType(this.API_MONITORING_TYPE)
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
