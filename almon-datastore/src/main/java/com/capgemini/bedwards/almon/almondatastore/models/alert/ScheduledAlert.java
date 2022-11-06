package com.capgemini.bedwards.almon.almondatastore.models.alert;

import com.capgemini.bedwards.almon.almondatastore.models.schedule.ScheduledMonitor;
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
public abstract class ScheduledAlert<M extends ScheduledMonitor> extends Alert<M> {

}
