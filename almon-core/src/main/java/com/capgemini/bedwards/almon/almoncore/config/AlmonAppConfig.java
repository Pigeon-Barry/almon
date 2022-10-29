package com.capgemini.bedwards.almon.almoncore.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Validated
@Data
@ConfigurationProperties(prefix = "almon.core")
public class AlmonAppConfig {
    @NotNull
    private AppConfig app = new AppConfig();

    @NotNull
    private AlmonCoreCompanyConfig company;

    @Data
    public static class AlmonCoreCompanyConfig {
        @NotBlank
        private String name;
        @NotBlank
        private String url;
    }

    @Data
    public static class AppConfig {

        @NotBlank
        private String name = "Almon";
    }

}
