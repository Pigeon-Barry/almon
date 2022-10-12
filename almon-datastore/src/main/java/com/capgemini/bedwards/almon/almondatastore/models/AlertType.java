package com.capgemini.bedwards.almon.almondatastore.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;


@Getter
@Setter
@EqualsAndHashCode
@Entity
@ToString
public class AlertType {

    @Id
    @NotNull
    private String name;
}
