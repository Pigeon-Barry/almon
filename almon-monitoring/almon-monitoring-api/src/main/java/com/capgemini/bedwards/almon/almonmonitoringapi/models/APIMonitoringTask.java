package com.capgemini.bedwards.almon.almonmonitoringapi.models;

import com.capgemini.bedwards.almon.almondatastore.models.ScheduledTask;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class APIMonitoringTask extends ScheduledTask {

    private final APIMonitoringType API_MONITORING_TYPE;

    public APIMonitoringTask(APIMonitoringType apiMonitoringType) {
        super(apiMonitoringType);
        this.API_MONITORING_TYPE = apiMonitoringType;
    }

    @Override
    public void run() {
        log.info("Running for: " + this.API_MONITORING_TYPE.getTaskId());
    }
}
