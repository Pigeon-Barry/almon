package com.capgemini.bedwards.almon.almonmonitoringapi.models.services;

import com.capgemini.bedwards.almon.almondatastore.models.service.Service;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ServiceResponseBody {


    private String key;


    private String name;


    private String description;

    public static ServiceResponseBody from(Service service) {
        return ServiceResponseBody.builder()
                .key(service.getId())
                .name(service.getName())
                .description(service.getDescription())
                .build();
    }
}
