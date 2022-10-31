package com.capgemini.bedwards.almon.almonweb.controller.monitor;


import com.capgemini.bedwards.almon.almoncore.intergrations.web.WebController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/web/service/{serviceId}/monitoring/{monitoringType}")
@Slf4j
@PreAuthorize("isAuthenticated()")
public class MonitorWebController extends WebController {
}
