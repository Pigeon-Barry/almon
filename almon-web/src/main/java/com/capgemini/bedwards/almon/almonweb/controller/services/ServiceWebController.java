package com.capgemini.bedwards.almon.almonweb.controller.services;


import com.capgemini.bedwards.almon.almoncore.intergrations.web.WebController;
import com.capgemini.bedwards.almon.almoncore.services.service.ServiceService;
import com.capgemini.bedwards.almon.almoncore.services.user.UserService;
import com.capgemini.bedwards.almon.almondatastore.models.service.Service;
import com.capgemini.bedwards.almon.almonmonitoringcore.Monitors;
import com.capgemini.bedwards.almon.almonweb.model.services.ServiceUpdateRequestBody;
import com.capgemini.bedwards.almon.notificationcore.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public ServiceWebController(ServiceService serviceService, UserService userService, Monitors monitors, NotificationService notificationService) {
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
        model.addAttribute("serviceUsers", SERVICE_SERVICE.getUsersByServiceRole(service));
        return "services/service";
    }


    @GetMapping("/update")
    @PreAuthorize("hasAuthority('CREATE_SERVICE')  || hasAuthority('SERVICE_' + #service.id + '_CAN_UPDATE')")
    public String getUpdateServicePage(
            @PathVariable(name = "serviceId") Service service,
            ServiceUpdateRequestBody serviceUpdateRequestBody,
            Model model) {
        model.addAttribute("service", service);
        serviceUpdateRequestBody.populate(service);

        return "/services/updateService";
    }

    @PostMapping("/update")
    @PreAuthorize("hasAuthority('UPDATE_SERVICE')  || hasAuthority('SERVICE_' + #service.id + '_CAN_UPDATE')")
    public String updateService(
            @PathVariable(name = "serviceId") Service service,
            @Valid ServiceUpdateRequestBody serviceUpdateRequestBody,
            Errors errors, Model model) {
        model.addAttribute("service", service);
        if (errors.hasErrors()) {
            return "/services/updateService";
        }
        SERVICE_SERVICE.updateService(service, serviceUpdateRequestBody.getName(), serviceUpdateRequestBody.getDescription());

        return "redirect:/web/service/" + service.getId();
    }
}
