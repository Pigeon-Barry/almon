package com.capgemini.bedwards.almon.almonmonitoringapi.models;

import com.capgemini.bedwards.almon.almondatastore.models.alert.ScheduledAlert;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@DiscriminatorValue("ACTIVE_API")
public class APIAlertType extends ScheduledAlert {
    protected int responseStatusCode;
    private long requestDurationMS;
}
