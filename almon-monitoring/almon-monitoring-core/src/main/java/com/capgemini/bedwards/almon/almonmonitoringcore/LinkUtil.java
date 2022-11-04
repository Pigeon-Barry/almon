package com.capgemini.bedwards.almon.almonmonitoringcore;

import com.capgemini.bedwards.almon.almondatastore.models.monitor.Monitor;

public abstract class LinkUtil {

    private LinkUtil() {
    }

    public static String getMonitorWebViewLink(Monitor monitor) {
        return "/web/service/" + monitor.getId().getService().getId() + "/monitor/" + monitor.getId();
    }
}
