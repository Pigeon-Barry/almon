package com.capgemini.bedwards.almon.almonaccessapi.controller.alert;


import com.capgemini.bedwards.almon.almoncore.intergrations.api.APIController;
import com.capgemini.bedwards.almon.almondatastore.models.alert.Alert;
import com.capgemini.bedwards.almon.almondatastore.models.monitor.Monitor;
import com.capgemini.bedwards.almon.almondatastore.models.service.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/api/service/{serviceId}/monitor/{monitorId}/alert/{alertId}")
@Slf4j
@PreAuthorize("isAuthenticated()")
public class AlertApiController extends APIController {


    @GetMapping(value = "/html", produces = MediaType.TEXT_HTML_VALUE)
    @PreAuthorize("hasAuthority('VIEW_ALL_MONITORS') || hasAuthority('SERVICE_' + #service.id + '_MONITOR_' + #monitor.id + '_CAN_VIEW')")
    public ResponseEntity<String> viewAlertDetailsAsHtml(
            @Valid @PathVariable(name = "serviceId") Service service,
            @Valid @PathVariable(name = "monitorId") Monitor monitor,
            @Valid @PathVariable(name = "alertId") Alert<?> alert) {
        return ResponseEntity.ok(alert.getHTMLMessage());
    }

    @GetMapping("/json")
    @PreAuthorize("hasAuthority('VIEW_ALL_MONITORS') || hasAuthority('SERVICE_' + #service.id + '_MONITOR_' + #monitor.id + '_CAN_VIEW')")
    public ResponseEntity<Alert<?>> viewAlertDetailsAsJson(
            @Valid @PathVariable(name = "serviceId") Service service,
            @Valid @PathVariable(name = "monitorId") Monitor monitor,
            @Valid @PathVariable(name = "alertId") Alert<?> alert) {
        return ResponseEntity.ok(alert);
    }
}
