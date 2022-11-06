package com.capgemini.bedwards.almon.almonmonitoringcore.convertor;

import com.capgemini.bedwards.almon.almondatastore.models.schedule.ScheduledMonitor;
import com.capgemini.bedwards.almon.almonmonitoringcore.service.monitor.ScheduledMonitorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ScheduledMonitorConvertor implements Converter<String, ScheduledMonitor> {


    public final ScheduledMonitorService<ScheduledMonitor> MONITOR_SERVICE;

    public ScheduledMonitorConvertor(@Qualifier("scheduledMonitorServiceImpl") ScheduledMonitorService<ScheduledMonitor> monitorService) {
        this.MONITOR_SERVICE = monitorService;
    }

    @Override
    public ScheduledMonitor convert(String source) {
        return MONITOR_SERVICE.getMonitorFromCombinedId(source);
    }
}
