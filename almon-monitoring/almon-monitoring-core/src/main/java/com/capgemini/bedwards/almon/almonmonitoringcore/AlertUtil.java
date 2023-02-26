package com.capgemini.bedwards.almon.almonmonitoringcore;

import com.capgemini.bedwards.almon.almoncore.util.BeanUtil;
import com.capgemini.bedwards.almon.almondatastore.models.alert.Status;
import com.capgemini.bedwards.almon.almondatastore.models.monitor.Monitor;
import com.capgemini.bedwards.almon.almondatastore.models.service.Service;
import com.capgemini.bedwards.almon.almonmonitoringcore.service.alert.AlertServiceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

public class AlertUtil {

    private static AlertServiceImpl ALERT_SERVICE;


    private static AlertServiceImpl getAlertService() {
        if (ALERT_SERVICE == null)
            ALERT_SERVICE = BeanUtil.getBeanOfClass(AlertServiceImpl.class);
        return ALERT_SERVICE;
    }

    public static double getFailureRateLast7Days(Service service) {
        LocalDate today = LocalDate.now();
        LocalDateTime start = today.atStartOfDay().minusDays(6);
        LocalDateTime end = today.atTime(23, 59, 59);
        Map<String, Long> map = getAlertService().getAlertCountByStatus(service, start, end);
        return calculateFailureRate(map);
    }

    private static double calculateFailureRate(Map<String, Long> map) {
        int total = map.values().stream().reduce(0L, Long::sum).intValue();
        return total == 0L ? 0.0 : ((double) (total - map.get(Status.PASS.toString())) / (double) total);
    }

    public static double getFailureRateLast7Days(Monitor monitor) {
        LocalDate today = LocalDate.now();
        LocalDateTime start = today.atStartOfDay().minusDays(6);
        LocalDateTime end = today.atTime(23, 59, 59);
        Map<String, Long> map = getAlertService().getAlertCountByStatus(monitor, start, end);
        return calculateFailureRate(map);
    }

}
