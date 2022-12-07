package com.capgemini.bedwards.almon.almonaccessapi.controllers.service;

import com.capgemini.bedwards.almon.almoncore.intergrations.api.APIController;
import com.capgemini.bedwards.almon.almoncore.services.service.ServiceService;
import com.capgemini.bedwards.almon.almondatastore.models.service.Service;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/service/{serviceId}")
@Slf4j
public class ServiceAPIController extends APIController {


    private ServiceService SERVICE_SERVICE;

    @Autowired
    public ServiceAPIController(ServiceService serviceService) {
        this.SERVICE_SERVICE = serviceService;
    }


    @PutMapping("/enable")
    @PreAuthorize("hasAuthority('ENABLE_DISABLE_SERVICES') || hasAuthority('SERVICE_' + #service.id + '_CAN_ENABLE_DISABLE')")
    public ResponseEntity<String> enable(@PathVariable(name = "serviceId") Service service) {
        SERVICE_SERVICE.enableService(service);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PutMapping("/disable")
    @PreAuthorize("hasAuthority('ENABLE_DISABLE_SERVICES') || hasAuthority('SERVICE_' + #service.id + '_CAN_ENABLE_DISABLE')")
    public ResponseEntity<String> disable(@PathVariable(name = "serviceId") Service service, HttpServletRequest request) {
        SERVICE_SERVICE.disableService(service);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
