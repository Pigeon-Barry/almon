package com.capgemini.bedwards.almon.almonmonitoringpassiveapi;

import com.capgemini.bedwards.almon.almoncore.util.ValidatorUtil;
import com.capgemini.bedwards.almon.almondatastore.models.monitor.Monitor;
import com.capgemini.bedwards.almon.almondatastore.models.service.Service;
import com.capgemini.bedwards.almon.almonmonitoringcore.LinkUtil;
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
    public ModelAndView createMonitorWeb(Service service, Object formData, Model model) {
        CreatePassiveAPIMonitorRequestBody requestBody = (CreatePassiveAPIMonitorRequestBody) formData;
        BeanPropertyBindingResult errors = ValidatorUtil.validate(requestBody, "formData");
        if (errors.hasErrors()) {
            model.addAttribute("org.springframework.validation.BindingResult.formData", errors);
            model.addAttribute("previousFormData", formData);
            return getCreatePageWeb(service, model);
        }
        PassiveAPIMonitor activeApiMonitor = createAPIMonitorType(requestBody, service);

        return new ModelAndView("redirect:" + LinkUtil.getMonitorWebViewLink(activeApiMonitor));
    }

    private PassiveAPIMonitor createAPIMonitorType(CreatePassiveAPIMonitorRequestBody requestBody, Service service) {
        return API_MONITOR_SERVICE.create(requestBody.toAPIMonitor(service));
    }

    @Override
    public ModelAndView updateMonitorWeb(Monitor monitor, Object formData, Model model) {
        throw new UnsupportedOperationException();//TODO
    }

    @Override
    public Object getCreateMonitorRequestBody(ObjectNode objectNode) {
        return CreatePassiveAPIMonitorRequestBody.from(objectNode);
    }

    @Override
    public Object getUpdateMonitorRequestBody(ObjectNode jsonRes) {
        throw new UnsupportedOperationException();//TODO
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
