package com.capgemini.bedwards.almon.almonmonitoringapi.models;

import com.capgemini.bedwards.almon.almondatastore.models.alert.ScheduledAlert;
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
@DiscriminatorValue("ACTIVE_API")
public class APIAlertType extends ScheduledAlert {
    protected int responseStatusCode;
    private long requestDurationMS;

    @Transient
    private ResponseEntity responseEntity;

    @Override
    public String getLongMessage() {
        APIMonitoringType apiMonitoringType = (APIMonitoringType) getMonitoringType();
        return new StringBuilder()
                .append("Method: ").append(apiMonitoringType.getMethod()).append("\n")
                .append("URL: ").append(apiMonitoringType.getUrl()).append("\n")
                .append("Response: ").append(responseEntity).append("\n")
                .append("Message: ").append(getMessage())

                .toString();
    }

    @Override
    public String getShortMessage() {
        return getMessage();
    }

    @Override
    public String getHTMLMessage() {
        APIMonitoringType apiMonitoringType = (APIMonitoringType) getMonitoringType();
        return new StringBuilder()
                .append("<p><h3>Method: </h3>").append(apiMonitoringType.getMethod()).append("</p>")
                .append("<p><h3>URL: </h3>").append(apiMonitoringType.getUrl()).append("</p>")
                .append("<p><h3>Response: </h3>").append(responseEntity).append("</p>")
                .append("<p><h3>Message: </h3>").append(getMessage()).append("</p>")

                .toString();
    }
}
