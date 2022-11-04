package com.capgemini.bedwards.almon.almonweb.controller.services;


import com.capgemini.bedwards.almon.almoncore.intergrations.web.WebController;
import com.capgemini.bedwards.almon.almoncore.services.service.ServiceService;
import com.capgemini.bedwards.almon.almondatastore.models.service.Service;
import com.capgemini.bedwards.almon.almonmonitoringcore.Monitors;
import com.capgemini.bedwards.almon.almonweb.model.services.ServiceUpdateRequestBody;
import com.capgemini.bedwards.almon.notificationcore.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@RequestMapping("/web/service/{serviceId}")
@Slf4j
@PreAuthorize("isAuthenticated()")
public class ServiceWebController extends WebController {


    private final ServiceService SERVICE_SERVICE;
    private final NotificationService NOTIFICATION_SERVICE;
    private final Monitors MONITORS;


    @Autowired
    public ServiceWebController(ServiceService serviceService, Monitors monitors, NotificationService notificationService) {
        this.SERVICE_SERVICE = serviceService;
        this.MONITORS = monitors;
        this.NOTIFICATION_SERVICE = notificationService;
    }


    @GetMapping()
    @PreAuthorize("hasAuthority('VIEW_ALL_SERVICES') || hasAuthority('SERVICE_' + #service.id + '_CAN_VIEW')")
    public String getUsersList(@PathVariable(name = "serviceId") Service service, Model model) {
        model.addAttribute("service", service);
        model.addAttribute("monitors", MONITORS);
        model.addAttribute("notificationHelper", NOTIFICATION_SERVICE.getNotificationHelper());
        return "services/service";
    }

    @DeleteMapping()
    @PreAuthorize("hasAuthority('DELETE_SERVICES') || hasAuthority('SERVICE_' + #service.id + '_CAN_DELETE')")
    public ResponseEntity<Void> delete(@PathVariable(name = "serviceId") Service service, Model model) {
        SERVICE_SERVICE.deleteService(service);
        return new ResponseEntity<>(HttpStatus.OK);
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


    @GetMapping("/update")
    @PreAuthorize("hasAuthority('CREATE_SERVICE')  || hasAuthority('SERVICE_' + #service.id + '_CAN_UPDATE')")
    public String getUpdateServicePage(
            @PathVariable(name = "serviceId") Service service,
            ServiceUpdateRequestBody serviceUpdateRequestBody,
            Model model) {
        model.addAttribute("service",service);
        if(serviceUpdateRequestBody.getDescription() == null)
            serviceUpdateRequestBody.setDescription(service.getDescription());
        if(serviceUpdateRequestBody.getName() == null)
            serviceUpdateRequestBody.setName(service.getName());

        return "/services/updateService";
    }

    @PostMapping("/update")
    @PreAuthorize("hasAuthority('UPDATE_SERVICE')  || hasAuthority('SERVICE_' + #service.id + '_CAN_UPDATE')")
    public String updateService(
            @PathVariable(name = "serviceId") Service service,
            @Valid ServiceUpdateRequestBody serviceUpdateRequestBody,
            Errors errors, Model model) {
        model.addAttribute("service",service);
        if (errors.hasErrors()) {
            return "/services/updateService";
        }
        SERVICE_SERVICE.updateService(service, serviceUpdateRequestBody.getName(), serviceUpdateRequestBody.getDescription());

        return "redirect:/web/service/" + service.getId();
    }
}
