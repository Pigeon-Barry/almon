package com.capgemini.bedwards.almon.almonmonitoringcore.contracts;

import com.capgemini.bedwards.almon.almondatastore.models.alert.Alert;
import com.capgemini.bedwards.almon.almondatastore.models.alert.AlertFilterOptions;
import com.capgemini.bedwards.almon.almondatastore.models.alert.AlertSpecification;
import com.capgemini.bedwards.almon.almondatastore.models.monitor.Monitor;
import com.capgemini.bedwards.almon.almondatastore.models.service.Service;
import com.capgemini.bedwards.almon.almonmonitoringcore.service.alert.AlertService;
import com.capgemini.bedwards.almon.almonmonitoringcore.service.monitor.MonitorService;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;


public interface MonitorAdapter<T extends Monitor, A extends Alert> {
    String getName();

    default String getId() {
        return getName();
    }

    String getDescription();

    default String getCreatePageWebView() {
        return "monitor/" + getId() + "/createMonitor";
    }

    default String getViewMonitorPageWebView() {
        return "monitor/" + getId() + "/viewMonitor";
    }

    default ModelAndView getViewPageWeb(Service service, Monitor monitor, Model model, AlertFilterOptions alertFilterOptions, int alertPageNumber, int alertPageSize) {
        ModelAndView modelAndView = new ModelAndView(getViewMonitorPageWebView());
        modelAndView.addAllObjects(model.asMap());
        modelAndView.addObject("monitor", monitor);
        model.addAttribute("service", service);
        alertFilterOptions.setMonitors(new Monitor[]{monitor});

        System.out.println("1FILTER: " + ToStringBuilder.reflectionToString(alertFilterOptions));
        AlertSpecification<A> alertSpecification = new AlertSpecification<A>(alertFilterOptions);

        Page<? extends Alert> page = getAlertService().getAlertsPaginated(alertSpecification, alertPageNumber, alertPageSize);
        List<? extends Alert> listAlerts = page.getContent();
        model.addAttribute("currentPage", alertPageNumber);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("listAlerts", listAlerts);
        model.addAttribute("pageSize", alertPageSize);

        return modelAndView;
    }

    AlertService<A> getAlertService();

    MonitorService<T> getMonitorService();

    default ModelAndView getCreatePageWeb(Service service, Model model) {
        model.addAttribute("service", service);
        ModelAndView modelAndView = new ModelAndView(getCreatePageWebView());
        modelAndView.addAllObjects(model.asMap());
        modelAndView.addObject("monitor", this);
        return modelAndView;
    }

    ModelAndView createMonitorWeb(Service service, Object formData, Model model);

    Object getCreateMonitorRequestBody(ObjectNode jsonRes);


    Class<T> getMonitorClass();

    A execute(T monitor);
}
