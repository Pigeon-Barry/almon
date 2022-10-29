package com.capgemini.bedwards.almon.notificationstmp;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;


@Validated
@Data
@ConfigurationProperties(prefix = "almon.notification.stmp")
@Component
public class NotificationSTMPConfig {

    @NotBlank
    private String host;
    @NotBlank
    private String port;
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    private String subjectPrefix = "";
}
