package com.capgemini.bedwards.almon.almonmonitoringapi.monitoring;

import com.capgemini.bedwards.almon.almoncore.util.ValidatorUtil;
import com.capgemini.bedwards.almon.almondatastore.models.schedule.ScheduledTask;
import com.capgemini.bedwards.almon.almondatastore.models.service.Service;
import com.capgemini.bedwards.almon.almondatastore.util.HasScheduledTasks;
import com.capgemini.bedwards.almon.almonmonitoringapi.models.APIAlertType;
import com.capgemini.bedwards.almon.almonmonitoringapi.models.APIMonitor;
import com.capgemini.bedwards.almon.almonmonitoringapi.models.CreateAPIMonitorRequestBody;
import com.capgemini.bedwards.almon.almonmonitoringapi.service.APIAlertService;
import com.capgemini.bedwards.almon.almonmonitoringapi.service.APIMonitorService;
import com.capgemini.bedwards.almon.almonmonitoringcore.LinkUtil;
import com.capgemini.bedwards.almon.almonmonitoringcore.contracts.MonitorAdapter;
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
public class APIMonitorAdapter implements MonitorAdapter<APIMonitor, APIAlertType>, HasScheduledTasks<APIAlertType> {

    private final APIMonitorService API_MONITOR_SERVICE;
    private final AlertService<APIAlertType> ALERT_SERVICE;

    @Autowired
    public APIMonitorAdapter(APIMonitorService apiMonitorService, APIAlertService alertService) {
        this.API_MONITOR_SERVICE = apiMonitorService;
        this.ALERT_SERVICE = alertService;
    }

    @Override
    public String getName() {
        return "ACTIVE_API";
    }

    @Override
    public String getDescription() {
        return "Actively runs APIs calls at a set interval validating the response returned is correct";
    }

    @Override
    public AlertService<APIAlertType> getAlertService() {
        return ALERT_SERVICE;
    }

    @Override
    public APIMonitorService getMonitorService() {
        return API_MONITOR_SERVICE;
    }

    public ModelAndView getCreatePageWeb(Service service, Model model) {
        ModelAndView modelAndView = MonitorAdapter.super.getCreatePageWeb(service, model);
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
        APIMonitor apiMonitor = createAPIMonitorType(requestBody, service);

        return new ModelAndView("redirect:" + LinkUtil.getMonitorWebViewLink(service, apiMonitor));
    }


    private APIMonitor createAPIMonitorType(CreateAPIMonitorRequestBody requestBody, Service service) {
        return API_MONITOR_SERVICE.create(requestBody.toAPIMonitor(service));
    }


    @Override
    public Object getCreateMonitorRequestBody(ObjectNode objectNode) {
        return CreateAPIMonitorRequestBody.from(objectNode);
    }

    @Override
    public Class<APIMonitor> getMonitorClass() {
        return APIMonitor.class;
    }

    @Override
    public APIAlertType execute(APIMonitor monitor) {
        return API_MONITOR_SERVICE.getScheduledTask(monitor).execute();
    }

    @Override
    public Set<ScheduledTask<APIAlertType>> getScheduledTasks() {
        return API_MONITOR_SERVICE.findAll().stream()
                .map(API_MONITOR_SERVICE::getScheduledTask)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }
}
