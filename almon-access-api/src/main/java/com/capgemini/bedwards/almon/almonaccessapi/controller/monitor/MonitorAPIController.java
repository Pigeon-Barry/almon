package com.capgemini.bedwards.almon.almonaccessapi.controller.monitor;

import com.capgemini.bedwards.almon.almoncore.intergrations.api.APIController;
import com.capgemini.bedwards.almon.almondatastore.models.alert.Alert;
import com.capgemini.bedwards.almon.almondatastore.models.monitor.Monitor;
import com.capgemini.bedwards.almon.almondatastore.models.schedule.ScheduledMonitor;
import com.capgemini.bedwards.almon.almondatastore.models.service.Service;
import com.capgemini.bedwards.almon.almonmonitoringcore.Monitors;
import com.capgemini.bedwards.almon.almonmonitoringcore.contracts.ScheduledMonitorAdapter;
import com.capgemini.bedwards.almon.almonmonitoringcore.resolver.ConvertUpdateMonitorRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


@RestController
@RequestMapping("/api/service/{serviceId}/monitor/{monitorId}")
@Slf4j
public class MonitorAPIController extends APIController {


    private final Monitors MONITORS;

    @Autowired
    public MonitorAPIController(Monitors monitors) {
        this.MONITORS = monitors;
    }

    @GetMapping("")
    @PreAuthorize("hasAuthority('VIEW_ALL_MONITORS') || hasAuthority('SERVICE_' + #service.id + '_MONITOR_' + #monitor.id + '_CAN_VIEW')")
    public ResponseEntity<Monitor> get(
            @Valid @PathVariable(name = "serviceId") Service service,
            @Valid @PathVariable(name = "monitorId") Monitor monitor) {
        MONITORS.getMonitorServiceFromMonitor(monitor).disable(monitor);
        return ResponseEntity.ok(monitor);
    }

    @PutMapping("/enable")
    @PreAuthorize("hasAuthority('ENABLE_DISABLE_MONITORS') || hasAuthority('MONITOR_' + #monitor.id + '_CAN_ENABLE_DISABLE')")
    public ResponseEntity<Void> enable(@PathVariable(name = "monitorId") Monitor monitor) {
        MONITORS.getMonitorServiceFromMonitor(monitor).enable(monitor);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PutMapping("/disable")
    @PreAuthorize("hasAuthority('ENABLE_DISABLE_MONITORS') || hasAuthority('MONITOR_' + #monitor.id + '_CAN_ENABLE_DISABLE')")
    public ResponseEntity<Void> disable(@PathVariable(name = "monitorId") Monitor monitor, HttpServletRequest request) {
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


    @PostMapping("/run")
    @PreAuthorize("hasAuthority('RUN_MONITORS') || hasAuthority('SERVICE_' + #service.id + '_MONITOR_' + #monitor.id + '_CAN_RUN')")
    public ResponseEntity<Alert<?>> run(
            @Valid @PathVariable(name = "serviceId") Service service,
            @Valid @PathVariable(name = "monitorId") ScheduledMonitor monitor) {
        Alert<?> alert = ((ScheduledMonitorAdapter<ScheduledMonitor, ?>) MONITORS.getMonitorAdapterFromMonitor(monitor)).execute(monitor);
        return ResponseEntity.ok(alert);
    }

    @PostMapping(value = "/update")
    @PreAuthorize("hasAuthority('UPDATE_MONITORS') || hasAuthority('SERVICE_' + #service.id + '_MONITOR_' + #monitor.id + '_CAN_UPDATE')")
    public ResponseEntity<Void> updateMonitor(
            @Valid @PathVariable(name = "serviceId") Service service,
            @Valid @PathVariable(name = "monitorId") Monitor monitor,
            @Valid @ConvertUpdateMonitorRequest Object formData,
            Model model) {
        this.MONITORS.getMonitorAdapterFromMonitor(monitor).updateMonitor(monitor, formData, model);
        return ResponseEntity.accepted().build();
    }
}
