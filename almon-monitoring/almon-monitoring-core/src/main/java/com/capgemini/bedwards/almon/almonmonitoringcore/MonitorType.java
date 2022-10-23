package com.capgemini.bedwards.almon.almonmonitoringcore;

import com.capgemini.bedwards.almon.almondatastore.models.service.Service;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.servlet.ModelAndView;


public interface MonitorType {
    String getName();

    default String getId() {
        return getName();
    }

    String getDescription();

    default String getCreatePageWebView(){
        return "monitor/" + getId() + "/createMonitor";
    }
    default String getViewMonitorPageWebView(){
        return "monitor/" + getId() + "/viewMonitor";
    }

    default ModelAndView getCreatePageWeb(Service service, Model model) {
        model.addAttribute("service", service);
        ModelAndView modelAndView = new ModelAndView(getCreatePageWebView());
        modelAndView.addAllObjects(model.asMap());
        modelAndView.addObject("monitoringType", this);
        model.asMap().forEach((s, o) -> System.out.println(s + " - " + o));
        return modelAndView;
    }

    ModelAndView createMonitorWeb(Service service, Object formData, Model model);

    ResponseEntity<?> createMonitorAPI(Service service, ObjectNode requestBody) throws MethodArgumentNotValidException;

    Object getCreateMonitorRequestBody(ObjectNode jsonRes);
}
