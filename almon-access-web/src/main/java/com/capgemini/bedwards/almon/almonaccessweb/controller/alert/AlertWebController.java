package com.capgemini.bedwards.almon.almonaccessweb.controller.alert;


import com.capgemini.bedwards.almon.almondatastore.models.alert.Alert;
import com.capgemini.bedwards.almon.almondatastore.models.monitor.Monitor;
import com.capgemini.bedwards.almon.almondatastore.models.service.Service;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/web/service/{serviceId}/monitor/{monitorId}/alert/{alertId}")
@Slf4j
@PreAuthorize("isAuthenticated()")
public class AlertWebController {


  @GetMapping("/html")
  @PreAuthorize("hasAuthority('VIEW_ALL_MONITORS') || hasAuthority('SERVICE_' + #service.id + '_MONITOR_' + #monitor.id + '_CAN_VIEW')")
  @ResponseBody
  public String viewAlertDetailsAsHtml(
      @Valid @PathVariable(name = "serviceId") Service service,
      @Valid @PathVariable(name = "monitorId") Monitor monitor,
      @Valid @PathVariable(name = "alertId") Alert<?> alert) {
    return alert.getHTMLMessage();
  }
}
