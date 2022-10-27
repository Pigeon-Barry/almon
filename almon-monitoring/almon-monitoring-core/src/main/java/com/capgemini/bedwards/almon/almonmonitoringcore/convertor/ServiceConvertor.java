package com.capgemini.bedwards.almon.almonmonitoringcore.convertor;

import com.capgemini.bedwards.almon.almoncore.services.service.ServiceService;
import com.capgemini.bedwards.almon.almondatastore.models.service.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ServiceConvertor implements Converter<String, Service> {


    public final ServiceService SERVICE_SERVICE;

    public ServiceConvertor(ServiceService serviceService) {
        this.SERVICE_SERVICE = serviceService;
    }

    @Override
    public Service convert(String source) {
        return SERVICE_SERVICE.findServiceById(source);
    }
}
