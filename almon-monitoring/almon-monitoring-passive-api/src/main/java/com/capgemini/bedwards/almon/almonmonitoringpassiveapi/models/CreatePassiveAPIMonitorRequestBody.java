package com.capgemini.bedwards.almon.almonmonitoringpassiveapi.models;

import com.capgemini.bedwards.almon.almoncore.util.MappingUtil;
import com.capgemini.bedwards.almon.almondatastore.models.service.Service;
import com.capgemini.bedwards.almon.almonmonitoringcore.models.CreateMonitorRequestBody;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class CreatePassiveAPIMonitorRequestBody extends CreateMonitorRequestBody {


    @SneakyThrows
    public static CreatePassiveAPIMonitorRequestBody from(ObjectNode objectNode) {
        return MappingUtil.getObjectMapper().treeToValue(objectNode, CreatePassiveAPIMonitorRequestBody.class);
    }

    public PassiveAPIMonitor toAPIMonitor(Service service) {
        return toMonitor(PassiveAPIMonitor.builder(), service)
                .build();
    }
}
