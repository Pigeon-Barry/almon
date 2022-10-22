package com.capgemini.bedwards.almon.almondatastore.models.alert;

import com.capgemini.bedwards.almon.almondatastore.models.service.Service;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;


@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@SuperBuilder
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "alert_type", discriminatorType = DiscriminatorType.STRING)
public class AlertType {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "com.capgemini.bedwards.almon.almondatastore.util.UUIDGenerator")
    @Type(type = "org.hibernate.type.UUIDCharType")
    protected UUID id;

    @NotNull
    protected String name;

    protected String description;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "service_id", nullable = false)
    protected Service service;

    public AlertType() {

    }
}
