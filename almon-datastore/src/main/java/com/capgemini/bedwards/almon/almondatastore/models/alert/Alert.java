package com.capgemini.bedwards.almon.almondatastore.models.alert;

import com.capgemini.bedwards.almon.almondatastore.models.monitor.Monitor;
import com.capgemini.bedwards.almon.almondatastore.util.Constants;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@ToString
@SuperBuilder
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(discriminatorType = DiscriminatorType.STRING, name = "type")
public abstract class Alert<M extends Monitor> {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "com.capgemini.bedwards.almon.almondatastore.util.UUIDGenerator")
    @Type(type = "org.hibernate.type.UUIDCharType")
    protected UUID id;

    @NotNull
    @ManyToOne
    @JsonIgnore
    protected Monitor monitor;

    @NotNull
    @Builder.Default
    @Column(nullable = false, updatable = false, columnDefinition="TIMESTAMP default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP")
    protected LocalDateTime createdAt = LocalDateTime.now();

    @NotNull
    protected Status status;

    protected String message;

    @Length(max = Constants.ALERT_LONG_MAX_LENGTH)
    public abstract String getLongMessage();

    @Length(max = Constants.ALERT_SHORT_MAX_LENGTH)
    public String getShortMessage() {
        return this.getMessage();
    }

    public abstract String getHTMLMessage();

    public long getCreatedAtInMilliseconds() {
        return this.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public M getMonitor() {
        return (M) this.monitor;
    }
}
