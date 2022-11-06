package com.capgemini.bedwards.almon.almonmonitoringpassiveapi.models;

import com.capgemini.bedwards.almon.almondatastore.models.alert.Alert;
import com.capgemini.bedwards.almon.almonmonitoringpassiveapi.PassiveAPIMonitorAdapter;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@DiscriminatorValue(PassiveAPIMonitorAdapter.ID)
public class PassiveAPIAlert extends Alert<PassiveAPIMonitor> {


    @Override
    public String getLongMessage() {
        return this.getShortMessage();
    }


    @Override
    public String getHTMLMessage() {
        return "<p><h3>Message: </h3>" + this.getMessage() + "</p>";
    }
}
