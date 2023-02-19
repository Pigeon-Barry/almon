package com.capgemini.bedwards.almon.almonaccessapi.controller.monitor;


import com.capgemini.bedwards.almon.almoncore.intergrations.api.APIController;
import com.capgemini.bedwards.almon.almondatastore.models.service.Service;
import com.capgemini.bedwards.almon.almonmonitoringcore.Monitors;
import com.capgemini.bedwards.almon.almonmonitoringcore.contracts.MonitorAdapter;
import com.capgemini.bedwards.almon.almonmonitoringcore.resolver.ConvertCreateMonitorRequest;
import com.capgemini.bedwards.almon.almonmonitoringcore.validators.MonitorAdapterExists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/api/service/{serviceId}/monitor/{monitorAdapterId}")
@Slf4j
@PreAuthorize("isAuthenticated()")
public class ServiceMonitorApiController extends APIController {


    private final Monitors MONITORS;

    @Autowired
    public ServiceMonitorApiController(Monitors monitors) {
        this.MONITORS = monitors;
    }


    @PostMapping(value = "/create")
    @PreAuthorize("hasAuthority('CREATE_MONITORS') || hasAuthority('SERVICE_' + #service.id + '_CAN_CREATE_MONITORS')")
    public ResponseEntity<Void> createNewAlertType(
            @Valid @PathVariable(name = "serviceId")
            Service service,
            @Valid @PathVariable(name = "monitorAdapterId")
            @MonitorAdapterExists
            MonitorAdapter<?, ?> monitorAdapter,
            @Valid @ConvertCreateMonitorRequest Object formData,
            Model model) {
        monitorAdapter.createMonitor(service, formData, model);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
