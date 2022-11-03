package com.capgemini.bedwards.almon.almondatastore.models.monitor;

import com.capgemini.bedwards.almon.almondatastore.models.alert.Alert;
import com.capgemini.bedwards.almon.almondatastore.models.service.Service;
import com.capgemini.bedwards.almon.almondatastore.util.Constants;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@SuperBuilder
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(discriminatorType = DiscriminatorType.STRING, name = "type")
@Table(name = "monitoring_type")
public abstract class Monitor {
    @EmbeddedId
    protected MonitorId id;

    @NotBlank
    protected String name;


    protected String description;

    protected boolean enabled = false;

    @OneToMany(mappedBy = "monitor", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    protected Set<Alert> alerts;


    @Data
    @Embeddable
    @AllArgsConstructor
    @SuperBuilder
    @NoArgsConstructor
    public static class MonitorId implements Serializable {
        @Length(max = Constants.MONITOR_ID_MAX_LENGTH)
        @Column(length = Constants.SERVICE_ID_MAX_LENGTH)
        protected String id;
        @ManyToOne
        @JoinColumn(name = "service_id")
        protected Service service;

        public String toString() {
            return getService().getId() + "-" + getId();
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Monitor monitor = (Monitor) o;

        return Objects.equals(id, monitor.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
