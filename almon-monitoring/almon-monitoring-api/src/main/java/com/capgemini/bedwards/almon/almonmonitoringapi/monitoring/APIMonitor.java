package com.capgemini.bedwards.almon.almonmonitoringapi.monitoring;

import com.capgemini.bedwards.almon.almondatastore.models.service.Service;
import com.capgemini.bedwards.almon.almonmonitoringapi.models.CreateAPIMonitorRequestBody;
import com.capgemini.bedwards.almon.almonmonitoringcore.MonitorType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

@Component
public class APIMonitor implements MonitorType {
    @Override
    public String getName() {
        return "ACTIVE_API";
    }


    @Override
    public ModelAndView createMonitorWeb(Service service, ObjectNode objectNode) {
        CreateAPIMonitorRequestBody requestBody = CreateAPIMonitorRequestBody.from(objectNode);
        return null;
    }

    @Override
    public ResponseEntity<?> createMonitorAPI(Service service, ObjectNode objectNode) {
        CreateAPIMonitorRequestBody requestBody = CreateAPIMonitorRequestBody.from(objectNode);
        return new ResponseEntity<>(requestBody, HttpStatus.CREATED);
    }
}
