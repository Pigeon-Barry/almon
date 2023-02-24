package com.capgemini.bedwards.almon.notificationcore;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


@Validated
@Data
@ConfigurationProperties(prefix = "almon.notification.core")
@Component
public class NotificationCoreConfig {
    @NotNull
    @Min(0)
    private Long notificationThrottle = 86400L;
}
