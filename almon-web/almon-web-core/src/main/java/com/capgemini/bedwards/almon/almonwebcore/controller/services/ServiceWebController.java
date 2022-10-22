package com.capgemini.bedwards.almon.almonwebcore.controller.services;


import com.capgemini.bedwards.almon.almoncore.services.service.ServiceService;
import com.capgemini.bedwards.almon.almondatastore.models.service.Service;
import com.capgemini.bedwards.almon.almonwebcore.controller.WebController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/web/service/{serviceId}")
@Slf4j
@PreAuthorize("isAuthenticated()")
public class ServiceWebController extends WebController {

    @Autowired
    ServiceService serviceService;

    @GetMapping()
    @PreAuthorize("hasAuthority('VIEW_ALL_SERVICES') || hasAuthority('SERVICE_' + #serviceId + '_CAN_VIEW')")
    public String getUsersList(@PathVariable(name = "serviceId") String serviceId, Model model) {

        Service service = serviceService.findServiceById(serviceId);
        model.addAttribute("service", service);


        return "services/service";
    }
}
