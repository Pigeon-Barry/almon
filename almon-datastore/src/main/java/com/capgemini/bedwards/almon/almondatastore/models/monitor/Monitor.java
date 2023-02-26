package com.capgemini.bedwards.almon.almondatastore.models.monitor;

import com.capgemini.bedwards.almon.almondatastore.models.alert.Alert;
import com.capgemini.bedwards.almon.almondatastore.models.service.Service;
import com.capgemini.bedwards.almon.almondatastore.models.subscription.MonitorSubscription;
import com.capgemini.bedwards.almon.almondatastore.util.Constants;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@SuperBuilder(toBuilder = true)
@DiscriminatorColumn(discriminatorType = DiscriminatorType.STRING, name = "type")
public abstract class Monitor {


    @EmbeddedId
    protected MonitorId id;

    @NotBlank
    protected String name;


    protected String description;

    protected boolean enabled = false;

    @NotNull
    protected Long notificationThrottle;

    @NotNull
    @Builder.Default
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected LocalDateTime preventNotificationUntil = Constants.DEFAULT_MONITOR_PREVENT_UNTIL;

    @OneToMany(mappedBy = "monitor", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    protected Set<Alert> alerts;

    @OneToMany(mappedBy = "id.monitor", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    protected Set<MonitorSubscription> subscriptions;

    public abstract String getMonitorType();


    @Data
    @Embeddable
    @AllArgsConstructor
    @SuperBuilder
    @NoArgsConstructor
    public static class MonitorId implements Serializable {
        @Length(max = Constants.MONITOR_ID_MAX_LENGTH)
        @Column(length = Constants.MONITOR_ID_MAX_LENGTH)
        protected String id;
        @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
        @JoinColumn(name = "service_id")
        protected Service service;

        @JsonValue
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
