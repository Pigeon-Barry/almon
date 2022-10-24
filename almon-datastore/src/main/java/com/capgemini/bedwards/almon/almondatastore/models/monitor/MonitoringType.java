package com.capgemini.bedwards.almon.almondatastore.models.monitor;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@SuperBuilder
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(discriminatorType = DiscriminatorType.STRING, name = "type")
public abstract class MonitoringType {
    @EmbeddedId
    protected MonitoringTypeId id;

    @NotBlank
    protected String name;


    protected String description;

    protected boolean enabled = true;
}
