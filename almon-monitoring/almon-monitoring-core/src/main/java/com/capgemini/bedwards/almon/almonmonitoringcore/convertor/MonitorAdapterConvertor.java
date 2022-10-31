package com.capgemini.bedwards.almon.almonmonitoringcore.convertor;

import com.capgemini.bedwards.almon.almonmonitoringcore.Monitors;
import com.capgemini.bedwards.almon.almonmonitoringcore.contracts.MonitorAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MonitorAdapterConvertor implements Converter<String, MonitorAdapter> {


    public final Monitors MONITORS;

    public MonitorAdapterConvertor(Monitors monitors) {
        this.MONITORS = monitors;
    }

    @Override
    public MonitorAdapter convert(String source) {
        return MONITORS.getMonitorAdapterFromId(source);
    }
}
