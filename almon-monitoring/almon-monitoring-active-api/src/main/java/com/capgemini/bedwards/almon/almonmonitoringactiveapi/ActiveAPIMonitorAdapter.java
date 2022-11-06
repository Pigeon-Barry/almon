package com.capgemini.bedwards.almon.almonmonitoringactiveapi;

import com.capgemini.bedwards.almon.almoncore.util.ValidatorUtil;
import com.capgemini.bedwards.almon.almondatastore.models.monitor.Monitor;
import com.capgemini.bedwards.almon.almondatastore.models.schedule.ScheduledTask;
import com.capgemini.bedwards.almon.almondatastore.models.service.Service;
import com.capgemini.bedwards.almon.almondatastore.util.HasScheduledTasks;
import com.capgemini.bedwards.almon.almonmonitoringactiveapi.models.ActiveAPIAlert;
import com.capgemini.bedwards.almon.almonmonitoringactiveapi.models.ActiveAPIMonitor;
import com.capgemini.bedwards.almon.almonmonitoringactiveapi.models.CreateActiveAPIMonitorRequestBody;
import com.capgemini.bedwards.almon.almonmonitoringactiveapi.models.UpdateActiveAPIMonitorRequestBody;
import com.capgemini.bedwards.almon.almonmonitoringactiveapi.service.ActiveAPIAlertService;
import com.capgemini.bedwards.almon.almonmonitoringactiveapi.service.ActiveAPIMonitorService;
import com.capgemini.bedwards.almon.almonmonitoringcore.LinkUtil;
import com.capgemini.bedwards.almon.almonmonitoringcore.contracts.ScheduledMonitorAdapter;
import com.capgemini.bedwards.almon.almonmonitoringcore.service.alert.AlertService;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.servlet.ModelAndView;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ActiveAPIMonitorAdapter implements ScheduledMonitorAdapter<ActiveAPIMonitor, ActiveAPIAlert>, HasScheduledTasks<ActiveAPIAlert> {
    public static final String ID = "ACTIVE_API";
    private final ActiveAPIMonitorService API_MONITOR_SERVICE;
    private final ActiveAPIAlertService ALERT_SERVICE;



    @Autowired
    public ActiveAPIMonitorAdapter(ActiveAPIMonitorService activeApiMonitorService, ActiveAPIAlertService alertService) {
        this.API_MONITOR_SERVICE = activeApiMonitorService;
        this.ALERT_SERVICE = alertService;
    }

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public String getDescription() {
        return "Actively runs APIs calls at a set interval validating the response returned is correct";
    }

    @Override
    public AlertService<ActiveAPIAlert> getAlertService() {
        return ALERT_SERVICE;
    }

    @Override
    public ActiveAPIMonitorService getMonitorService() {
        return API_MONITOR_SERVICE;
    }

    @Override
    public ModelAndView getCreatePageWeb(Service service, Model model) {
        ModelAndView modelAndView = ScheduledMonitorAdapter.super.getCreatePageWeb(service, model);
        if (!modelAndView.getModelMap().containsAttribute("formData"))
            modelAndView.getModelMap().addAttribute("formData", new CreateActiveAPIMonitorRequestBody());
        return modelAndView;
    }

    @Override
    public ModelAndView getUpdatePageWeb(Monitor monitor, Model model) {
        ModelAndView modelAndView = ScheduledMonitorAdapter.super.getUpdatePageWeb(monitor, model);
        if (!modelAndView.getModelMap().containsAttribute("formData"))
            modelAndView.getModelMap().addAttribute("formData", new UpdateActiveAPIMonitorRequestBody().populate((ActiveAPIMonitor) monitor));
        return modelAndView;
    }

    @Override
    public ModelAndView createMonitorWeb(Service service, Object formData, Model model) {
        CreateActiveAPIMonitorRequestBody requestBody = (CreateActiveAPIMonitorRequestBody) formData;
        BeanPropertyBindingResult errors = ValidatorUtil.validate(requestBody, "formData");
        if (errors.hasErrors()) {
            model.addAttribute("org.springframework.validation.BindingResult.formData", errors);
            model.addAttribute("previousFormData", formData);
            return getCreatePageWeb(service, model);
        }
        ActiveAPIMonitor activeApiMonitor = createAPIMonitorType(requestBody, service);

        return new ModelAndView("redirect:" + LinkUtil.getMonitorWebViewLink(activeApiMonitor));
    }

    @Override
    public ModelAndView updateMonitorWeb(Monitor monitor, Object formData, Model model) {
        UpdateActiveAPIMonitorRequestBody requestBody = (UpdateActiveAPIMonitorRequestBody) formData;
        BeanPropertyBindingResult errors = ValidatorUtil.validate(requestBody, "formData");
        model.addAttribute("formData", formData);

        if (errors.hasErrors()) {
            model.addAttribute("org.springframework.validation.BindingResult.formData", errors);
            return getUpdatePageWeb(monitor, model);
        }

        monitor = this.API_MONITOR_SERVICE.save(requestBody.updateAPIMonitor((ActiveAPIMonitor) monitor));

        return new ModelAndView("redirect:" + LinkUtil.getMonitorWebViewLink(monitor));
    }


    private ActiveAPIMonitor createAPIMonitorType(CreateActiveAPIMonitorRequestBody requestBody, Service service) {
        return API_MONITOR_SERVICE.create(requestBody.toAPIMonitor(service));
    }


    @Override
    public Object getCreateMonitorRequestBody(ObjectNode objectNode) {
        return CreateActiveAPIMonitorRequestBody.from(objectNode);
    }

    @Override
    public Object getUpdateMonitorRequestBody(ObjectNode objectNode) {
        return UpdateActiveAPIMonitorRequestBody.from(objectNode);
    }

    @Override
    public Class<ActiveAPIMonitor> getMonitorClass() {
        return ActiveAPIMonitor.class;
    }

    @Override
    public ActiveAPIAlert execute(ActiveAPIMonitor monitor) {
        return API_MONITOR_SERVICE.getScheduledTask(monitor).execute();
    }

    @Override
    public Set<ScheduledTask<ActiveAPIAlert>> getScheduledTasks() {
        return API_MONITOR_SERVICE.findAll().stream()
                .map(API_MONITOR_SERVICE::getScheduledTask)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }
}
