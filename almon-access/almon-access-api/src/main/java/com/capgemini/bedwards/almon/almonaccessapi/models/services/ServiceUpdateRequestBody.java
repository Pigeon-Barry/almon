package com.capgemini.bedwards.almon.almonaccessapi.models.services;

import com.capgemini.bedwards.almon.almondatastore.models.service.Service;
import lombok.Data;

@Data
public class ServiceUpdateRequestBody {

    private String name;
    private String description;

    public void populate(Service service) {
        if (this.getDescription() == null)
            this.setDescription(service.getDescription());
        if (this.getName() == null)
            this.setName(service.getName());
    }
}
