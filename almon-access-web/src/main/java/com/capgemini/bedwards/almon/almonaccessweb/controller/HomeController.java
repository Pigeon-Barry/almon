package com.capgemini.bedwards.almon.almonaccessweb.controller;

import com.capgemini.bedwards.almon.almoncore.intergrations.web.WebController;
import com.capgemini.bedwards.almon.almoncore.services.notification.WebNotificationService;
import com.capgemini.bedwards.almon.almoncore.services.service.ServiceService;
import com.capgemini.bedwards.almon.almondatastore.models.notification.WebNotification;
import com.capgemini.bedwards.almon.almondatastore.models.service.Service;
import com.capgemini.bedwards.almon.almonmonitoringcore.service.alert.AlertServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/web")
@PreAuthorize("isAuthenticated()")
@Slf4j
public class HomeController extends WebController {

    private final AlertServiceImpl ALERT_SERVICE;
    private final ServiceService SERVICE_SERVICE;
    private final WebNotificationService WEB_NOTIFICATION_SERVICE;

    @Autowired
    public HomeController(AlertServiceImpl alertService, ServiceService serviceService, WebNotificationService webNotificationService) {
        this.ALERT_SERVICE = alertService;
        this.SERVICE_SERVICE = serviceService;
        this.WEB_NOTIFICATION_SERVICE = webNotificationService;
    }

    @GetMapping("/home")
    public String home(@RequestParam(defaultValue = "1") int notificationPageNumber,
                       @RequestParam(defaultValue = "25") int notificationPageSize,
                       Model model) {


        Page<WebNotification> page = this.WEB_NOTIFICATION_SERVICE.getNotifications(getUser(),
                notificationPageNumber, notificationPageSize);

        model.addAttribute("currentPage", notificationPageNumber);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("webNotifications", page.getContent());
        model.addAttribute("pageSize", notificationPageSize);


        List<Service> serviceList = this.SERVICE_SERVICE.findServicesFromUser(getUser());
        Map<String, List<?>> stats = new HashMap<>();
        LocalDate today = LocalDate.now();
        //Calculate Weekly
        List<Map<?, ?>> weekly = new ArrayList<>();
        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = today.atTime(23, 59, 59);
        for (int index = 0; index < 7; index++) {

            weekly.add(this.ALERT_SERVICE.getAlertStatusByService(serviceList, start, end));
            start = start.minusDays(1);
            end = end.minusDays(1);
        }
        stats.put("weekly", weekly);
        model.addAttribute("serviceStats", stats);
        return "home";
    }

    @GetMapping
    public String root(@RequestParam(defaultValue = "1") int notificationPageNumber,
                       @RequestParam(defaultValue = "25") int notificationPageSize,
                       Model model) {
        return home(notificationPageNumber, notificationPageSize, model);
    }
}
