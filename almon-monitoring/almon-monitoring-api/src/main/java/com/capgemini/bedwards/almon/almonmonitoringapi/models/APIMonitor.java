package com.capgemini.bedwards.almon.almonmonitoringapi.models;

import com.capgemini.bedwards.almon.almoncore.util.BeanUtil;
import com.capgemini.bedwards.almon.almoncore.validators.ValidJsonPath;
import com.capgemini.bedwards.almon.almondatastore.models.schedule.ScheduledMonitor;
import com.capgemini.bedwards.almon.almonmonitoringapi.monitoring.APIMonitorAdapter;
import com.capgemini.bedwards.almon.almonmonitoringapi.service.APIMonitorService;
import lombok.*;
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
@Setter
@DiscriminatorValue("ACTIVE_API")
public class APIMonitor extends ScheduledMonitor {
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
    public APIMonitoringTask getScheduledTask() {
        return BeanUtil.getBeanOfClass(APIMonitorService.class).getScheduledTask(this);
    }

    @Override
    public String getMonitorType() {
        return APIMonitorAdapter.getStaticId();
    }
}
