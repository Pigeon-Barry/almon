package com.capgemini.bedwards.almon.almonmonitoringcore;

import com.capgemini.bedwards.almon.almondatastore.models.service.Service;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;


public interface MonitorType {
    String getName();

    default ModelAndView getCreatePageWeb(Service service, Model model) {
        ModelAndView modelAndView = new ModelAndView("monitor/" + getName() + "/createMonitor");
        modelAndView.addAllObjects(model.asMap());
        modelAndView.addObject("service", service);
        modelAndView.addObject("monitoringType", this);


        model.asMap().forEach((s, o) -> System.out.println(s + " - " + o));

        return modelAndView;
    }

    ModelAndView createMonitorWeb(Service service, ObjectNode requestBody);

    ResponseEntity<?> createMonitorAPI(Service service, ObjectNode requestBody);
}
