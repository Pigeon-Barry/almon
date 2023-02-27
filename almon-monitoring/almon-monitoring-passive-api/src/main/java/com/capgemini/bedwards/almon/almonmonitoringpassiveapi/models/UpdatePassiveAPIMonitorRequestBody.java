package com.capgemini.bedwards.almon.almonmonitoringpassiveapi.models;

import com.capgemini.bedwards.almon.almoncore.util.MappingUtil;
import com.capgemini.bedwards.almon.almonmonitoringcore.models.UpdateMonitorRequestBody;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Data
@NoArgsConstructor
@Slf4j
public class UpdatePassiveAPIMonitorRequestBody extends UpdateMonitorRequestBody {


    public UpdatePassiveAPIMonitorRequestBody(String name, String description, Long notificationThrottle) {
        super(name, description, notificationThrottle);
    }

    @SneakyThrows
    public static UpdatePassiveAPIMonitorRequestBody from(ObjectNode objectNode) {
        return MappingUtil.getObjectMapper().treeToValue(objectNode, UpdatePassiveAPIMonitorRequestBody.class);
    }

    public UpdatePassiveAPIMonitorRequestBody populate(PassiveAPIMonitor monitor) {
        super.populate(monitor);
        return this;
    }

    public PassiveAPIMonitor updateAPIMonitor(PassiveAPIMonitor passiveAPIMonitor) {
        return toMonitor(passiveAPIMonitor);
    }
}
