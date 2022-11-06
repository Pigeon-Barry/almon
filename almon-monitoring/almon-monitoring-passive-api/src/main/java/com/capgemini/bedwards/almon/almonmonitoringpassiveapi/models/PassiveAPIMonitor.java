package com.capgemini.bedwards.almon.almonmonitoringpassiveapi.models;

import com.capgemini.bedwards.almon.almondatastore.models.monitor.Monitor;
import com.capgemini.bedwards.almon.almonmonitoringpassiveapi.PassiveAPIMonitorAdapter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;


@Entity
@SuperBuilder
//@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@DiscriminatorValue(PassiveAPIMonitorAdapter.ID)
public class PassiveAPIMonitor extends Monitor {

    @Override
    public String getMonitorType() {
        return PassiveAPIMonitorAdapter.ID;
    }
}
