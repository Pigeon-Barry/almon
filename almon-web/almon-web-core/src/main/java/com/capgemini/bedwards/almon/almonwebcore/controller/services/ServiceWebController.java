package com.capgemini.bedwards.almon.almonwebcore.controller.services;


import com.capgemini.bedwards.almon.almoncore.services.service.ServiceService;
import com.capgemini.bedwards.almon.almoncore.util.SecurityUtil;
import com.capgemini.bedwards.almon.almondatastore.models.auth.Authority;
import com.capgemini.bedwards.almon.almondatastore.models.auth.Role;
import com.capgemini.bedwards.almon.almondatastore.models.auth.User;
import com.capgemini.bedwards.almon.almondatastore.models.service.Service;
import com.capgemini.bedwards.almon.almonmonitoringapi.models.services.ServiceRequestBody;
import com.capgemini.bedwards.almon.almonwebcore.controller.WebController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

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
