package com.capgemini.bedwards.almon.almonmonitoringactiveapi.models;

import com.capgemini.bedwards.almon.almondatastore.models.alert.ScheduledAlert;
import com.capgemini.bedwards.almon.almonmonitoringactiveapi.ActiveAPIMonitorAdapter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.http.ResponseEntity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@DiscriminatorValue(ActiveAPIMonitorAdapter.ID)
public class ActiveAPIAlert extends ScheduledAlert<ActiveAPIMonitor> {

  private int responseStatusCode;
  private long requestDurationMS;

  @Transient
  private ResponseEntity<?> responseEntity;

  @Override
  public String getLongMessage() {
    return "Method: " + this.getMonitor().getMethod() + "\n" +
        "URL: " + this.getMonitor().getUrl() + "\n" +
        "Response: " + this.responseEntity + "\n" +
        "Message: " + this.getMessage();
  }

  @Override
  public String getHTMLMessage() {
    return "<p><h3>Method: </h3>" + this.getMonitor().getMethod() + "</p>" +
        "<p><h3>URL: </h3>" + this.getMonitor().getUrl() + "</p>" +
        "<p><h3>Status Code: </h3>" + this.responseStatusCode + "</p>" +
        "<p><h3>Duration: </h3>" + this.requestDurationMS + " ms</p>" +
        (this.responseEntity != null ?
            "<p><h3>Response: </h3>" + this.responseEntity + "</p>" : "") +
        (this.getMessage() != null ?
            "<p><h3>Message: </h3>" + this.getMessage() + "</p>" : "");
  }
}
