package com.capgemini.bedwards.almon.almonmonitoringapi.models;

import com.capgemini.bedwards.almon.almondatastore.models.alert.MonitoringType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.DiscriminatorValue;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import java.util.Map;


@Entity
@SuperBuilder
@AllArgsConstructor
@ToString
@Getter
@DiscriminatorValue("API")
public class APIMonitoringType extends MonitoringType {

    protected String url;

    @ElementCollection
    Map<String, String> headers;

    protected int expectedStatus;

    @ElementCollection
    Map<String, String> jsonPathValidations;

    @ElementCollection
    Map<String, String> xPathValidations;

    public APIMonitoringType() {

    }
}
