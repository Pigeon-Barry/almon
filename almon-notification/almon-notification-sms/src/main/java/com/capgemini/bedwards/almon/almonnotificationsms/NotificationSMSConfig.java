package com.capgemini.bedwards.almon.almonnotificationsms;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;


@Validated
@Data
@ConfigurationProperties(prefix = "almon.notification.sms")
@Component
public class NotificationSMSConfig {
    @NotBlank
    private String accessToken;
    @NotBlank
    private String phoneId;

}
