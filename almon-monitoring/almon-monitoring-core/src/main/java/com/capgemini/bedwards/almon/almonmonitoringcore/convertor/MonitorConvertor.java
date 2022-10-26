package com.capgemini.bedwards.almon.almonmonitoringcore.convertor;

import com.capgemini.bedwards.almon.almondatastore.models.monitor.MonitoringType;
import com.capgemini.bedwards.almon.almonmonitoringcore.service.monitor.MonitorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MonitorConvertor implements Converter<String, MonitoringType> {


    public final MonitorService<MonitoringType> MONITOR_SERVICE;

    public MonitorConvertor(@Qualifier("monitorServiceImpl") MonitorService<MonitoringType> monitorService) {
        this.MONITOR_SERVICE = monitorService;
    }

    @Override
    public MonitoringType convert(String source) {
        return MONITOR_SERVICE.getMonitorFromCombinedId(source);
    }
}
