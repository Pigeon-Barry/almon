package com.capgemini.bedwards.almon.almonweb.controller.monitor;


import com.capgemini.bedwards.almon.almoncore.intergrations.web.WebController;
import com.capgemini.bedwards.almon.almondatastore.models.service.Service;
import com.capgemini.bedwards.almon.almonmonitoringcore.Monitors;
import com.capgemini.bedwards.almon.almonmonitoringcore.contracts.MonitorAdapter;
import com.capgemini.bedwards.almon.almonmonitoringcore.resolver.ConvertCreateMonitorRequest;
import com.capgemini.bedwards.almon.almonmonitoringcore.validators.MonitorAdapterExists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping("/web/service/{serviceId}/monitor/{monitorAdapterId}")
@Slf4j
@PreAuthorize("isAuthenticated()")
public class ServiceMonitorWebController extends WebController {


    private final Monitors MONITORS;

    @Autowired
    public ServiceMonitorWebController(Monitors monitors) {
        this.MONITORS = monitors;
    }

    @GetMapping("/create")
    @PreAuthorize("hasAuthority('CREATE_MONITORS') || hasAuthority('SERVICE_' + #service.id + '_CAN_CREATE_MONITORS')")
    public ModelAndView createNewAlertTypePage(
            @Valid @PathVariable(name = "serviceId")
            Service service,
            @Valid @PathVariable(name = "monitorAdapterId")
            @MonitorAdapterExists
            MonitorAdapter<?,?> monitorAdapter,
            Model model) {
        return monitorAdapter.getCreatePageWeb(service, model);
    }

    @PostMapping(value = "/create")
    @PreAuthorize("hasAuthority('CREATE_MONITORS') || hasAuthority('SERVICE_' + #service.id + '_CAN_CREATE_MONITORS')")
    public ModelAndView createNewAlertType(
            @Valid @PathVariable(name = "serviceId")
            Service service,
            @Valid @PathVariable(name = "monitorAdapterId")
            @MonitorAdapterExists
            MonitorAdapter<?,?> monitorAdapter,
            @Valid @ConvertCreateMonitorRequest Object formData,
            Model model) {
        return monitorAdapter.createMonitorWeb(service, formData, model);
    }
}
