package com.capgemini.bedwards.almon.almondatastore.models.alerts;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;


@Getter
@Setter
@ToString
@Entity
@DiscriminatorValue("SSH")
public  class SSHAlertType extends AlertType{


    private String ssh;
}
