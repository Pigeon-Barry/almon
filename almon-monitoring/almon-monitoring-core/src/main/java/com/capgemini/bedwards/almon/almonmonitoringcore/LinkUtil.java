package com.capgemini.bedwards.almon.almonmonitoringcore;

import com.capgemini.bedwards.almon.almondatastore.models.monitor.MonitoringType;
import com.capgemini.bedwards.almon.almondatastore.models.service.Service;

public abstract class LinkUtil {

    private LinkUtil() {
    }

    public static String getMonitorWebViewLink(Service service,MonitoringType monitoringType) {
        return "/web/service/" + service.getId() + "/monitoring/" + monitoringType.getId();
    }
}
