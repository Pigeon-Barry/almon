package com.capgemini.bedwards.almon.almonmonitoringcore.contracts;

import com.capgemini.bedwards.almon.almondatastore.models.monitor.Monitor;
import com.capgemini.bedwards.almon.almondatastore.models.service.Service;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;


public interface MonitorAdapter {
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

    default ModelAndView getViewPageWeb(Service service, Monitor monitor, Model model) {
        ModelAndView modelAndView = new ModelAndView(getViewMonitorPageWebView());
        modelAndView.addAllObjects(model.asMap());
        modelAndView.addObject("monitor", monitor);
        model.addAttribute("service", service);
        return modelAndView;
    }

    default ModelAndView getCreatePageWeb(Service service, Model model) {
        model.addAttribute("service", service);
        ModelAndView modelAndView = new ModelAndView(getCreatePageWebView());
        modelAndView.addAllObjects(model.asMap());
        modelAndView.addObject("monitor", this);
        return modelAndView;
    }

    ModelAndView createMonitorWeb(Service service, Object formData, Model model);

    Object getCreateMonitorRequestBody(ObjectNode jsonRes);


    Class<? extends Monitor> getMonitorClass();
}
