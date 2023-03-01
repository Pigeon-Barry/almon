package com.capgemini.bedwards.almon.almonaccessweb;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Validated
@ConfigurationProperties(prefix = "almon.web")
@Component
@Data
public class AlmonWebSettings {
    @NotBlank
    @Email
    private String rootAccount;
}
