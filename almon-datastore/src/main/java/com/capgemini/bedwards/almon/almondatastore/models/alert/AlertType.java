package com.capgemini.bedwards.almon.almondatastore.models.alert;

import com.capgemini.bedwards.almon.almondatastore.models.service.Service;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
public class AlertType {

//    @Id
//    @GeneratedValue(generator = "uuid")
//    @GenericGenerator(name = "uuid", strategy = "com.capgemini.bedwards.almon.almondatastore.util.UUIDGenerator")
//    @Type(type = "org.hibernate.type.UUIDCharType")
//    protected UUID id;

    @EmbeddedId
    protected AlertTypeId id;

    @NotBlank
    protected String name;


    protected String description;


    public AlertType() {

    }

    @Data
    @Embeddable
    public static class AlertTypeId implements Serializable {
        protected String id;
        @ManyToOne
        @JoinColumn(name = "service_id")
        protected Service service;
    }
}
