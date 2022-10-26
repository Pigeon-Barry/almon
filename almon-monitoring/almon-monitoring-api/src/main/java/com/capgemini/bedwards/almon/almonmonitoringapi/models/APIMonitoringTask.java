package com.capgemini.bedwards.almon.almonmonitoringapi.models;

import com.capgemini.bedwards.almon.almondatastore.models.alert.Status;
import com.capgemini.bedwards.almon.almonmonitoringapi.service.APIAlertService;
import com.capgemini.bedwards.almon.almonmonitoringcore.schedule.ScheduledTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

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
//        Class<Object> responseType = Object.class;
//
//        RequestCallback requestCallback = this.REST_TEMPLATE.acceptHeaderRequestCallback(responseType);
//        ResponseExtractor<ResponseEntity<Object>> responseExtractor = this.REST_TEMPLATE.responseEntityExtractor(responseType);
//
//
//        this.REST_TEMPLATE.execute(this.API_MONITORING_TYPE.getUrl(),
//                this.API_MONITORING_TYPE.getMethod(),
//                requestCallback, responseExtractor, null, null);
//
//
//        ResponseEntity<Object> response = this.REST_TEMPLATE.exchange(
//
//                requestCallback, responseExtractor);
        //TODO
        this.API_ALERT_SERVICE.create(APIAlertType.builder()
                .monitoringType(this.API_MONITORING_TYPE)
                .status(Status.PASS)
                .responseStatusCode(200)
                .requestDurationMS(100)
                .build());
    }

    @Override
    public boolean isEnabled() {
        return this.API_MONITORING_TYPE.isEnabled();
    }
}
