package com.capgemini.bedwards.almon.almonmonitoringcore;

import com.capgemini.bedwards.almon.almondatastore.models.service.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

public interface MonitorType {
    String getName();

    default ModelAndView createPage(Service service, Model model) {
        ModelAndView modelAndView = new ModelAndView("monitor/" + getName() + "/createMonitor");
        modelAndView.addAllObjects(model.asMap());
        modelAndView.addObject("service", service);
        return modelAndView;
    }
}
