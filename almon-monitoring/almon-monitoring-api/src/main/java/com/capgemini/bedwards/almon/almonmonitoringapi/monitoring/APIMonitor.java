package com.capgemini.bedwards.almon.almonmonitoringapi.monitoring;

import com.capgemini.bedwards.almon.almoncore.util.ValidatorUtil;
import com.capgemini.bedwards.almon.almondatastore.models.service.Service;
import com.capgemini.bedwards.almon.almonmonitoringapi.models.APIMonitoringType;
import com.capgemini.bedwards.almon.almonmonitoringapi.models.CreateAPIMonitorRequestBody;
import com.capgemini.bedwards.almon.almonmonitoringapi.service.APIMonitorService;
import com.capgemini.bedwards.almon.almonmonitoringcore.MonitorType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.servlet.ModelAndView;

@Component
@Slf4j
public class APIMonitor implements MonitorType {

    private final APIMonitorService API_MONITOR_SERVICE;

    @Autowired
    public APIMonitor(APIMonitorService apiMonitorService) {
        this.API_MONITOR_SERVICE = apiMonitorService;
    }

    @Override
    public String getName() {
        return "ACTIVE_API";
    }


    @Override
    public String getDescription() {
        return "Actively runs APIs calls at a set interval validating the response returned is correct";
    }

    public ModelAndView getCreatePageWeb(Service service, Model model) {
        ModelAndView modelAndView = MonitorType.super.getCreatePageWeb(service, model);
        if (!modelAndView.getModelMap().containsAttribute("formData"))
            modelAndView.getModelMap().addAttribute("formData", new CreateAPIMonitorRequestBody());
        return modelAndView;
    }


    @Override
    public ModelAndView createMonitorWeb(Service service, Object formData, Model model) {
        CreateAPIMonitorRequestBody requestBody = (CreateAPIMonitorRequestBody) formData;
        BeanPropertyBindingResult errors = ValidatorUtil.validate(requestBody, "formData");
        if (errors.hasErrors()) {
            model.addAttribute("org.springframework.validation.BindingResult.formData", errors);
            model.addAttribute("previousFormData", formData);
            return getCreatePageWeb(service, model);
        }
        APIMonitoringType apiMonitoringType = createAPIMonitorType(requestBody, service);
        return new ModelAndView("redirect:/web/service/" + service.getId() + "/monitoring/" + getId() + "/" + apiMonitoringType.getId().getId());
    }


    private APIMonitoringType createAPIMonitorType(CreateAPIMonitorRequestBody requestBody, Service service) {
        return API_MONITOR_SERVICE.create(requestBody.toAPIMonitoringType(service));
    }

    @Override
    public ResponseEntity<?> createMonitorAPI(Service service, ObjectNode objectNode) {
        throw new UnsupportedOperationException();//TODO
    }

    @Override
    public Object getCreateMonitorRequestBody(ObjectNode objectNode) {
        return CreateAPIMonitorRequestBody.from(objectNode);
    }
}
