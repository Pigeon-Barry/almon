package com.capgemini.bedwards.almon.almonweb.controller.monitor;


import com.capgemini.bedwards.almon.almoncore.intergrations.web.WebController;
import com.capgemini.bedwards.almon.almondatastore.models.alert.Alert;
import com.capgemini.bedwards.almon.almondatastore.models.alert.AlertFilterOptions;
import com.capgemini.bedwards.almon.almondatastore.models.monitor.Monitor;
import com.capgemini.bedwards.almon.almondatastore.models.service.Service;
import com.capgemini.bedwards.almon.almonmonitoringcore.Monitors;
import com.capgemini.bedwards.almon.almonmonitoringcore.resolver.ConvertUpdateMonitorRequest;
import com.capgemini.bedwards.almon.notificationcore.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@RequestMapping("/web/service/{serviceId}/monitor/{monitorId}")
@Slf4j
@PreAuthorize("isAuthenticated()")
public class MonitorWebController extends WebController {

    private final Monitors MONITORS;
    private final NotificationService NOTIFICATION_SERVICE;

    @Autowired
    public MonitorWebController(Monitors monitors, NotificationService notificationService) {
        this.MONITORS = monitors;
        this.NOTIFICATION_SERVICE = notificationService;
    }


    @PutMapping("/enable")
    @PreAuthorize("hasAuthority('ENABLE_DISABLE_MONITORS') || hasAuthority('SERVICE_' + #service.id + '_MONITOR_' + #monitor.id + '_CAN_ENABLE_DISABLE')")
    public ResponseEntity<String> enable(
            @Valid @PathVariable(name = "serviceId") Service service,
            @Valid @PathVariable(name = "monitorId") Monitor monitor) {
        MONITORS.getMonitorServiceFromMonitor(monitor).enable(monitor);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PutMapping("/disable")
    @PreAuthorize("hasAuthority('ENABLE_DISABLE_MONITORS') || hasAuthority('SERVICE_' + #service.id + '_MONITOR_' + #monitor.id + '_CAN_ENABLE_DISABLE')")
    public ResponseEntity<String> disable(
            @Valid @PathVariable(name = "serviceId") Service service,
            @Valid @PathVariable(name = "monitorId") Monitor monitor,
            HttpServletRequest request) {
        MONITORS.getMonitorServiceFromMonitor(monitor).disable(monitor);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @DeleteMapping()
    @PreAuthorize("hasAuthority('DELETE_MONITORS') || hasAuthority('SERVICE_' + #service.id + '_MONITOR_' + #monitor.id + '_CAN_DELETE')")
    public ResponseEntity<Void> delete(
            @Valid @PathVariable(name = "serviceId") Service service,
            @Valid @PathVariable(name = "monitorId")
            Monitor monitor,
            Model model) {
        MONITORS.getMonitorServiceFromMonitor(monitor).delete(monitor);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping("")
    @PreAuthorize("hasAuthority('VIEW_ALL_MONITORS') || hasAuthority('SERVICE_' + #service.id + '_MONITOR_' + #monitor.id + '_CAN_VIEW')")
    public ModelAndView createNewAlertTypePage(
            @Valid @PathVariable(name = "serviceId") Service service,
            @Valid @PathVariable(name = "monitorId") Monitor monitor,
            @ModelAttribute AlertFilterOptions alertFilterOptions,
            @RequestParam(defaultValue = "1") int alertPageNumber,
            @RequestParam(defaultValue = "10") int alertPageSize,
            Model model) {
        model.addAttribute("notificationHelper", NOTIFICATION_SERVICE.getNotificationHelper());
        return MONITORS.getMonitorAdapterFromMonitor(monitor).getViewPageWeb(service, monitor, model, alertFilterOptions, alertPageNumber, alertPageSize);
    }

    @PostMapping("/run")
    @PreAuthorize("hasAuthority('RUN_MONITORS') || hasAuthority('SERVICE_' + #service.id + '_MONITOR_' + #monitor.id + '_CAN_RUN')")
    public ResponseEntity<Alert> run(
            @Valid @PathVariable(name = "serviceId") Service service,
            @Valid @PathVariable(name = "monitorId") Monitor monitor) {
        Alert alert = MONITORS.getMonitorAdapterFromMonitor(monitor).execute(monitor);
        return ResponseEntity.ok(alert);
    }


    @GetMapping("/update")
    @PreAuthorize("hasAuthority('RUN_MONITORS') || hasAuthority('SERVICE_' + #service.id + '_MONITOR_' + #monitor.id + '_CAN_UPDATE')")
    public ModelAndView updateMonitor(
            @Valid @PathVariable(name = "serviceId") Service service,
            @Valid @PathVariable(name = "monitorId") Monitor monitor,
            Model model) {
        return MONITORS.getMonitorAdapterFromMonitor(monitor).getUpdatePageWeb(monitor, model);
    }

    @PostMapping(value = "/update")
    @PreAuthorize("hasAuthority('RUN_MONITORS') || hasAuthority('SERVICE_' + #service.id + '_MONITOR_' + #monitor.id + '_CAN_UPDATE')")
    public ModelAndView updateMonitor(
            @Valid @PathVariable(name = "serviceId") Service service,
            @Valid @PathVariable(name = "monitorId") Monitor monitor,
            @Valid @ConvertUpdateMonitorRequest Object formData,
            Model model) {

        return MONITORS.getMonitorAdapterFromMonitor(monitor).updateMonitorWeb(monitor, formData, model);
    }
}
