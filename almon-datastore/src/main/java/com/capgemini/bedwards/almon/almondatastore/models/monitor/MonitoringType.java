package com.capgemini.bedwards.almon.almondatastore.models.monitor;

import com.capgemini.bedwards.almon.almondatastore.models.service.Service;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

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

    protected boolean enabled = false;


    @Data
    @Embeddable
    @AllArgsConstructor
    @SuperBuilder
    @NoArgsConstructor
    public static class MonitoringTypeId implements Serializable {
        protected String id;
        @ManyToOne
        @JoinColumn(name = "service_id")
        protected Service service;

        public String toString() {
            return getService().getId() + "-" + getId();
        }
    }

}
