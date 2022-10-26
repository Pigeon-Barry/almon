package com.capgemini.bedwards.almon.almonapi.controllers.monitor;

import com.capgemini.bedwards.almon.almoncore.intergrations.api.APIController;
import com.capgemini.bedwards.almon.almondatastore.models.monitor.MonitoringType;
import com.capgemini.bedwards.almon.almonmonitoringcore.service.monitor.MonitorService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@OpenAPIDefinition(
        info = @Info(title = "ALMON - Monitor",
                version = "1.0.0")
)
@RestController
@RequestMapping("/api/service/{serviceId}/monitoring/{monitorId}")
@Slf4j
public class MonitorAPIController extends APIController {


    private MonitorService<MonitoringType> MONITOR_SERVICE;

    @Autowired
    public MonitorAPIController(MonitorService<MonitoringType> monitorService) {
        this.MONITOR_SERVICE = monitorService;
    }

    @PutMapping("/enable")
    @PreAuthorize("hasAuthority('ENABLE_DISABLE_MONITORS') || hasAuthority('MONITOR_' + #monitor.id + '_CAN_ENABLE_DISABLE')")
    public ResponseEntity<String> enable(@PathVariable(name = "monitorId") MonitoringType monitor) {
        MONITOR_SERVICE.enable(monitor);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PutMapping("/disable")
    @PreAuthorize("hasAuthority('ENABLE_DISABLE_MONITORS') || hasAuthority('MONITOR_' + #monitor.id + '_CAN_ENABLE_DISABLE')")
    public ResponseEntity<String> disable(@PathVariable(name = "monitorId") MonitoringType monitor, HttpServletRequest request) {
        MONITOR_SERVICE.disable(monitor);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
