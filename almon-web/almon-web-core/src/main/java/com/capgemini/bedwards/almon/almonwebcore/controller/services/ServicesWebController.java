package com.capgemini.bedwards.almon.almonwebcore.controller.services;


import com.capgemini.bedwards.almon.almoncore.services.service.ServiceService;
import com.capgemini.bedwards.almon.almoncore.util.SecurityUtil;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/web/services")
@Slf4j
@PreAuthorize("isAuthenticated()")
public class ServicesWebController extends WebController {

    @Autowired
    ServiceService serviceService;

    @GetMapping()
    public String getAllUsers(@RequestParam(defaultValue = "1") int pageNumber,
                              @RequestParam(defaultValue = "25") int pageSize,
                              Model model) {
        User user = SecurityUtil.getAuthenticatedUser();
        log.debug("UserID: " + user.getId());
        for (Service service : user.getServices())
            log.debug("Service: " + service.getId());

        Page<Service> page;
        if (SecurityUtil.hasAuthority("VIEW_ALL_SERVICES"))
            page = serviceService.findPaginated(pageNumber, pageSize);
        else
            page = serviceService.findPaginatedFromUser(pageNumber, pageSize, SecurityUtil.getAuthenticatedUser());
        List<Service> listServices = page.getContent();


        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("listServices", listServices);
        model.addAttribute("pageSize", pageSize);
        return "/services/services";
    }

    @GetMapping("/create")
    @PreAuthorize("hasAuthority('CREATE_SERVICE')")
    public String getCreateServicePage(ServiceRequestBody serviceRequestBody) {
        return "/services/createService";
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('CREATE_SERVICE')")
    public String register(@Valid ServiceRequestBody serviceRequestBody, Errors errors, Model model) {
        if (errors.hasErrors()) {
            return "/services/createService";
        }
        Service service = serviceService.createService(SecurityUtil.getAuthenticatedUser(), serviceRequestBody.getKey(), serviceRequestBody.getName(), serviceRequestBody.getDescription());

        return "redirect:/web/service/" + service.getId();
    }
}
