package com.capgemini.bedwards.almon.almondatastore.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Builder
@Data
@AllArgsConstructor
@ToString
@Table(name = "alerts")
public class Alert {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "com.capgemini.bedwards.almon.almondatastore.util.UUIDGenerator")
    private UUID id;

    @NotNull
    @JoinColumn(name = "alertType", referencedColumnName = "name")
    @OneToOne()
    private AlertType alertType;

    @DateTimeFormat
    @NotNull
    private LocalDateTime alertReceivedTime;

    private String message;

    public Alert() {
    }
}
