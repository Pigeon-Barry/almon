package com.capgemini.bedwards.almon.almonmonitoringcore;

import com.capgemini.bedwards.almon.almondatastore.models.monitor.Monitor;
import com.capgemini.bedwards.almon.almondatastore.models.service.Service;

public abstract class LinkUtil {

    private LinkUtil() {
    }

    public static String getMonitorWebViewLink(Service service, Monitor monitor) {
        return "/web/service/" + service.getId() + "/monitor/" + monitor.getId();
    }
}
