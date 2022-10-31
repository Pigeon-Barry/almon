package com.capgemini.bedwards.almon.almonmonitoringcore.convertor;

import com.capgemini.bedwards.almon.almondatastore.models.monitor.Monitor;
import com.capgemini.bedwards.almon.almonmonitoringcore.service.monitor.MonitorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MonitorConvertor implements Converter<String, Monitor> {


    public final MonitorService<Monitor> MONITOR_SERVICE;

    public MonitorConvertor(@Qualifier("monitorServiceImpl") MonitorService<Monitor> monitorService) {
        this.MONITOR_SERVICE = monitorService;
    }

    @Override
    public Monitor convert(String source) {
        return MONITOR_SERVICE.getMonitorFromCombinedId(source);
    }
}
