package com.capgemini.bedwards.almon.almonwebcore;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Validated
@ConfigurationProperties(prefix = "almon.web")
@Data
public class AlmonWebConfig {
    @NotBlank
    @Email
    private String rootAccount;
}
