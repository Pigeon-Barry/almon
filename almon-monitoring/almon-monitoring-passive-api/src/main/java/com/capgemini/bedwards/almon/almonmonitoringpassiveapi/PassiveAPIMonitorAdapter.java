package com.capgemini.bedwards.almon.almonmonitoringpassiveapi;

import com.capgemini.bedwards.almon.almoncore.exceptions.ValidationException;
import com.capgemini.bedwards.almon.almoncore.util.ValidatorUtil;
import com.capgemini.bedwards.almon.almondatastore.models.monitor.Monitor;
import com.capgemini.bedwards.almon.almondatastore.models.service.Service;
import com.capgemini.bedwards.almon.almonmonitoringcore.contracts.MonitorAdapter;
import com.capgemini.bedwards.almon.almonmonitoringcore.service.alert.AlertService;
import com.capgemini.bedwards.almon.almonmonitoringcore.service.monitor.MonitorService;
import com.capgemini.bedwards.almon.almonmonitoringpassiveapi.models.*;
import com.capgemini.bedwards.almon.almonmonitoringpassiveapi.service.PassiveAPIAlertService;
import com.capgemini.bedwards.almon.almonmonitoringpassiveapi.service.PassiveAPIMonitorService;
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
public class PassiveAPIMonitorAdapter implements MonitorAdapter<PassiveAPIMonitor, PassiveAPIAlert> {

    public static final String ID = "PASSIVE_API";
    private final PassiveAPIMonitorService API_MONITOR_SERVICE;
    private final PassiveAPIAlertService ALERT_SERVICE;

    @Autowired
    public PassiveAPIMonitorAdapter(PassiveAPIMonitorService passiveApiMonitorService, PassiveAPIAlertService alertService) {
        this.API_MONITOR_SERVICE = passiveApiMonitorService;
        this.ALERT_SERVICE = alertService;
    }

    @Override
    public String getId() {
        return ID;
    }


    @Override
    public String getDescription() {
        return "Passively listens for inbound API calls.";
    }

    @Override
    public AlertService<PassiveAPIAlert> getAlertService() {
        return ALERT_SERVICE;
    }

    @Override
    public MonitorService<PassiveAPIMonitor> getMonitorService() {
        return API_MONITOR_SERVICE;
    }

    @Override
    public ModelAndView getCreatePageWeb(Service service, Model model) {
        ModelAndView modelAndView = MonitorAdapter.super.getCreatePageWeb(service, model);
        if (!modelAndView.getModelMap().containsAttribute("formData"))
            modelAndView.getModelMap().addAttribute("formData", new CreatePassiveAPIMonitorRequestBody());
        return modelAndView;
    }

    @Override
    public ModelAndView getUpdatePageWeb(Monitor monitor, Model model) {
        ModelAndView modelAndView = MonitorAdapter.super.getUpdatePageWeb(monitor, model);
        if (!modelAndView.getModelMap().containsAttribute("formData"))
            modelAndView.getModelMap().addAttribute("formData", new UpdatePassiveAPIMonitorRequestBody().populate((PassiveAPIMonitor) monitor));
        return modelAndView;
    }

    @Override
    public PassiveAPIMonitor createMonitor(Service service, Object formData, Model model) {
        CreatePassiveAPIMonitorRequestBody requestBody = (CreatePassiveAPIMonitorRequestBody) formData;
        BeanPropertyBindingResult errors = ValidatorUtil.validate(requestBody, "formData");
        if (errors.hasErrors()) {
            throw new ValidationException(errors);
        }
        return createAPIMonitorType(requestBody, service);
    }

    private PassiveAPIMonitor createAPIMonitorType(CreatePassiveAPIMonitorRequestBody requestBody, Service service) {
        return API_MONITOR_SERVICE.create(requestBody.toAPIMonitor(service));
    }


    @Override
    public PassiveAPIMonitor updateMonitor(Monitor monitor, Object formData, Model model) {
        UpdatePassiveAPIMonitorRequestBody requestBody = (UpdatePassiveAPIMonitorRequestBody) formData;
        BeanPropertyBindingResult errors = ValidatorUtil.validate(requestBody, "formData");
        if (errors.hasErrors()) {
            throw new ValidationException(errors);
        }
        return this.getMonitorService().save(requestBody.updateAPIMonitor((PassiveAPIMonitor) monitor));
    }

    @Override
    public Object getCreateMonitorRequestBody(ObjectNode objectNode) {
        return CreatePassiveAPIMonitorRequestBody.from(objectNode);
    }

    @Override
    public Object getUpdateMonitorRequestBody(ObjectNode objectNode) {
        return UpdatePassiveAPIMonitorRequestBody.from(objectNode);
    }

    @Override
    public Class<PassiveAPIMonitor> getMonitorClass() {
        return PassiveAPIMonitor.class;
    }

    public ResponseEntity<TriggerPassiveAPIMonitorResponseBody> trigger(PassiveAPIMonitor monitor, TriggerPassiveAPIMonitorRequestBody body) {
        PassiveAPIAlert alert = body.toAlert(monitor);
        return ResponseEntity.ok(new TriggerPassiveAPIMonitorResponseBody(ALERT_SERVICE.create(alert)));
    }
}
