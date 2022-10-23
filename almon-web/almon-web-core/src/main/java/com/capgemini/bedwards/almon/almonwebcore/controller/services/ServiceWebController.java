package com.capgemini.bedwards.almon.almonwebcore.controller.services;


import com.capgemini.bedwards.almon.almoncore.intergrations.web.WebController;
import com.capgemini.bedwards.almon.almoncore.services.service.ServiceService;
import com.capgemini.bedwards.almon.almondatastore.models.service.Service;
import com.capgemini.bedwards.almon.almonmonitoringcore.Monitors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/web/service/{serviceId}")
@Slf4j
@PreAuthorize("isAuthenticated()")
public class ServiceWebController extends WebController {


    ServiceService SERVICE_SERVICE;
    private final Monitors MONITORS;

    @Autowired
    public ServiceWebController(ServiceService serviceService, Monitors monitors) {
        this.SERVICE_SERVICE = serviceService;
        this.MONITORS = monitors;
    }


    @GetMapping()
    @PreAuthorize("hasAuthority('VIEW_ALL_SERVICES') || hasAuthority('SERVICE_' + #serviceId + '_CAN_VIEW')")
    public String getUsersList(@PathVariable(name = "serviceId") String serviceId, Model model) {

        Service service = SERVICE_SERVICE.findServiceById(serviceId);
        model.addAttribute("service", service);
        model.addAttribute("monitors", MONITORS);


        return "services/service";
    }
}
