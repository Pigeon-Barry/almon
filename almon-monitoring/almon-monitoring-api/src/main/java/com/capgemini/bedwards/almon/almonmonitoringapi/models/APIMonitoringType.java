package com.capgemini.bedwards.almon.almonmonitoringapi.models;

import com.capgemini.bedwards.almon.almondatastore.models.monitor.ScheduledMonitoringType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.DiscriminatorValue;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import java.util.Map;


@Entity
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@DiscriminatorValue("ACTIVE_API")
public class APIMonitoringType extends ScheduledMonitoringType {

    protected String url;

    @ElementCollection
    protected Map<String, String> headers;

    protected int expectedStatus;

    @ElementCollection
    protected Map<String, String> jsonPathValidations;


    @Override
    public String getTaskId() {
        return "ACTIVE_API-" + getId().getService().getId() + "-" + getId().getId();
    }
}
