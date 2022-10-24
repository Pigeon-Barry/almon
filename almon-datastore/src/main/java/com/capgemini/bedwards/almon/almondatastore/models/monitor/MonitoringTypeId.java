package com.capgemini.bedwards.almon.almondatastore.models.monitor;

import com.capgemini.bedwards.almon.almondatastore.models.service.Service;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Data
@Embeddable
@AllArgsConstructor
@SuperBuilder
@NoArgsConstructor
public class MonitoringTypeId implements Serializable {
    protected String id;
    @ManyToOne
    @JoinColumn(name = "service_id")
    protected Service service;
}
