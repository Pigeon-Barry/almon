package com.capgemini.bedwards.almon.almonwebcore.controller.services;


import com.capgemini.bedwards.almon.almoncore.intergrations.web.WebController;
import com.capgemini.bedwards.almon.almoncore.services.service.ServiceService;
import com.capgemini.bedwards.almon.almoncore.validators.ServiceDoesNotExist;
import com.capgemini.bedwards.almon.almondatastore.models.service.Service;
import com.capgemini.bedwards.almon.almonmonitoringcore.MonitorType;
import com.capgemini.bedwards.almon.almonmonitoringcore.Monitors;
import com.capgemini.bedwards.almon.almonmonitoringcore.validators.MonitorTypeExists;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping("/web/service/{serviceId}/monitoring/{monitoringType}")
@Slf4j
@PreAuthorize("isAuthenticated()")
public class ServiceMonitoringTypeWebController extends WebController {

    private final ServiceService SERVICE_SERVICE;

    private final Monitors MONITORS;

    @Autowired
    public ServiceMonitoringTypeWebController(ServiceService serviceService, Monitors monitors) {
        this.SERVICE_SERVICE = serviceService;
        this.MONITORS = monitors;
    }

    @GetMapping("/create")
    @PreAuthorize("hasAuthority('CREATE_MONITORING') || hasAuthority('SERVICE_' + #serviceId + '_CAN_CREATE_MONITORING')")
    public ModelAndView createNewAlertTypePage(
            @Valid @PathVariable(name = "serviceId")
            @ServiceDoesNotExist
            String serviceId,
            @Valid @PathVariable(name = "monitoringType")
            @MonitorTypeExists
            String monitoringType,
            Model model) {
        Service service = SERVICE_SERVICE.findServiceById(serviceId);
        MonitorType monitorType = MONITORS.getMonitorTypeFromName(monitoringType);
        return monitorType.getCreatePageWeb(service, model);
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('CREATE_MONITORING') || hasAuthority('SERVICE_' + #serviceId + '_CAN_CREATE_MONITORING')")
    public ModelAndView createNewAlertType(
            @Valid @PathVariable(name = "serviceId")
            @ServiceDoesNotExist
            String serviceId,
            @Valid @PathVariable(name = "monitoringType")
            @MonitorTypeExists
            String monitoringType,
            @RequestBody ObjectNode objectNode) {
        Service service = SERVICE_SERVICE.findServiceById(serviceId);
        MonitorType monitorType = MONITORS.getMonitorTypeFromName(monitoringType);
        return monitorType.createMonitorWeb(service, objectNode);
    }
}
