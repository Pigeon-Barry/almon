package com.capgemini.bedwards.almon.almonnotificationteams;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;


@Validated
@Data
@ConfigurationProperties(prefix = "almon.notification.teams")
@Component
public class NotificationTeamsConfig {

    @NotBlank
    private String clientId;
    @NotBlank
    private String clientSecret;
    @NotBlank
    private String authority;
    @NotBlank
    private String clientScope;

}
