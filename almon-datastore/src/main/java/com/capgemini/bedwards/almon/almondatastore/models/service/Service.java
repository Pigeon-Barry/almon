package com.capgemini.bedwards.almon.almondatastore.models.service;

import com.capgemini.bedwards.almon.almondatastore.models.monitor.MonitoringType;
import com.capgemini.bedwards.almon.almondatastore.util.Constants;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Set;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Service {

    @Id
    @Pattern(regexp = Constants.SERVICE_REGEX, message = Constants.SERVICE_REGEX_INVALID_MESSAGE)
    @NotNull
    private String id;

    @NotBlank
    private String name;

    private String description;

    @OneToMany(mappedBy = "id.service", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<MonitoringType> monitoringTypes;

    @NotNull
    private boolean enabled = true;

}
