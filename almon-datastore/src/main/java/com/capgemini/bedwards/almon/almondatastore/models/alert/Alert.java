package com.capgemini.bedwards.almon.almondatastore.models.alert;

import com.capgemini.bedwards.almon.almondatastore.models.monitor.Monitor;
import com.capgemini.bedwards.almon.almondatastore.util.Constants;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Length;

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
    @JoinColumns({
            @JoinColumn(name = "id.service"),
            @JoinColumn(name = "id.id")
    })
    @JsonIgnore
    protected M monitor;

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
}
