package com.capgemini.bedwards.almon.almonaccessweb;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "almon.web")
@Component
@Data
public class AlmonWebConfig {
    @NotBlank
    @Email
    private String rootAccount;
}
