package com.capgemini.bedwards.almon.almonapi.controllers;

import com.capgemini.bedwards.almon.almoncore.intergrations.api.APIController;
import com.capgemini.bedwards.almon.almoncore.services.service.ServiceService;
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
        info = @Info(title = "ALMON - Services",
                version = "1.0.0")
)
@RestController
@RequestMapping("/api/service/{serviceId}")
@Slf4j
public class ServiceAPIController extends APIController {

    @Autowired
    ServiceService serviceService;


    @PutMapping("/enable")
    @PreAuthorize("hasAuthority('ENABLE_DISABLE_SERVICES') || hasAuthority('SERVICE_' + #serviceId + '_CAN_ENABLE_DISABLE')")
    public ResponseEntity<String> enable(@PathVariable(name = "serviceId") String serviceId) {
        serviceService.enableService(serviceId);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PutMapping("/disable")
    @PreAuthorize("hasAuthority('ENABLE_DISABLE_SERVICES') || hasAuthority('SERVICE_' + #serviceId + '_CAN_ENABLE_DISABLE')")
    public ResponseEntity<String> disable(@PathVariable(name = "serviceId") String serviceId, HttpServletRequest request) {
        serviceService.disableService(serviceId);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
