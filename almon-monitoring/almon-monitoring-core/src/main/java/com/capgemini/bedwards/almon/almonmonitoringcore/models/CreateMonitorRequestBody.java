package com.capgemini.bedwards.almon.almonmonitoringcore.models;

import com.capgemini.bedwards.almon.almoncore.util.BeanUtil;
import com.capgemini.bedwards.almon.almondatastore.models.monitor.Monitor;
import com.capgemini.bedwards.almon.almondatastore.models.service.Service;
import com.capgemini.bedwards.almon.almondatastore.util.Constants;
import com.capgemini.bedwards.almon.notificationcore.NotificationCoreConfig;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.*;

@Data
@Slf4j
public class CreateMonitorRequestBody {
    @NotBlank
    @Pattern(regexp = Constants.MONITOR_KEY_REGEX, message = Constants.MONITOR_KEY_REGEX_INVALID_MESSAGE)
    @Size(max = Constants.MONITOR_ID_MAX_LENGTH)
    private String key;
    @NotBlank
    protected String name;
    @Min(0)
    @NotNull
    private Long notificationThrottle = BeanUtil.getBeanOfClass(NotificationCoreConfig.class).getNotificationThrottle();

    protected String description;

    protected <T extends Monitor.MonitorBuilder<?, ?>> T toMonitor(T builder, Service service) {
        builder.id(new Monitor.MonitorId(key, service))
                .name(name)
                .description(description)
                .notificationThrottle(notificationThrottle);
        return builder;
    }
}
