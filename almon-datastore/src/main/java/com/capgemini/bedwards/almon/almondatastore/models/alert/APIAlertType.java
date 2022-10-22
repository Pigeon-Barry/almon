package com.capgemini.bedwards.almon.almondatastore.models.alert;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;


@Entity
@SuperBuilder
@AllArgsConstructor
@ToString
@Getter
@DiscriminatorValue("API")
public class APIAlertType extends AlertType{

    protected String url;
    protected int expectedStatus;

    public APIAlertType() {

    }
}
