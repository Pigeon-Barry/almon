package com.capgemini.bedwards.almon.almondatastore.models.alert;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;

@Getter
@Setter
@ToString
@NoArgsConstructor
@SuperBuilder
@Entity
public abstract class ScheduledAlert extends Alert {

}
