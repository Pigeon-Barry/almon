package com.capgemini.bedwards.almon.almonmonitoringapi.models;

import com.capgemini.bedwards.almon.almoncore.util.BeanUtil;
import com.capgemini.bedwards.almon.almoncore.validators.ValidJsonPath;
import com.capgemini.bedwards.almon.almondatastore.models.schedule.ScheduledMonitoringType;
import com.capgemini.bedwards.almon.almonmonitoringapi.service.APIMonitorService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpMethod;

import javax.persistence.DiscriminatorValue;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.validation.constraints.NotNull;
import java.util.Map;


@Entity
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@DiscriminatorValue("ACTIVE_API")
public class APIMonitoringType extends ScheduledMonitoringType {
    @NotNull
    private HttpMethod method;
    protected String url;

    protected String body;

    @ElementCollection(fetch = FetchType.EAGER)
    protected Map<String, String> headers;

    protected int expectedStatus;

    @ElementCollection(fetch = FetchType.EAGER)
    protected Map<String, @ValidJsonPath String> jsonPathValidations;


    @Override
    public String getTaskId() {
        return "ACTIVE_API-" + getId().toString();
    }


    @Override
    public APIMonitoringTask getScheduledTask() {
        return BeanUtil.getBeanOfClass(APIMonitorService.class).getScheduledTask(this);
    }
}
