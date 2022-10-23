package com.capgemini.bedwards.almon.almondatastore.models.alert;

import com.capgemini.bedwards.almon.almondatastore.models.service.Service;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;


@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
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


    public MonitoringType() {

    }

    @Data
    @Embeddable
    public static class MonitoringTypeId implements Serializable {
        protected String id;
        @ManyToOne
        @JoinColumn(name = "service_id")
        protected Service service;
    }
}
