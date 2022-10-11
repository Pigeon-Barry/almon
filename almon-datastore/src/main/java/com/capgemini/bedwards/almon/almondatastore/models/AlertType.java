package com.capgemini.bedwards.almon.almondatastore.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;


@Getter
@Setter
@EqualsAndHashCode
@Entity
public class AlertType {
    @Id
    private String name;
}
