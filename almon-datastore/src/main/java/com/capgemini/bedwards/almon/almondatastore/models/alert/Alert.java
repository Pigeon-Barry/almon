package com.capgemini.bedwards.almon.almondatastore.models.alert;

import com.capgemini.bedwards.almon.almondatastore.models.monitor.Monitor;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@ToString
@SuperBuilder
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(discriminatorType = DiscriminatorType.STRING, name = "type")
public abstract class Alert {

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
    protected Monitor monitor;

    @NotNull
    @Builder.Default
    protected LocalDateTime createdAt = LocalDateTime.now();

    @NotNull
    protected Status status;

    protected String message;

    public abstract String getLongMessage();
    public abstract String getShortMessage();
    public abstract String getHTMLMessage();
}
