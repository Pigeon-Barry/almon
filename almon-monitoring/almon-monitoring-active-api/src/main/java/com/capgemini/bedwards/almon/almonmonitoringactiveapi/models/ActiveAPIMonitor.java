package com.capgemini.bedwards.almon.almonmonitoringactiveapi.models;

import com.capgemini.bedwards.almon.almoncore.util.BeanUtil;
import com.capgemini.bedwards.almon.almoncore.validators.ValidJsonPath;
import com.capgemini.bedwards.almon.almondatastore.models.schedule.ScheduledMonitor;
import com.capgemini.bedwards.almon.almonmonitoringactiveapi.ActiveAPIMonitorAdapter;
import com.capgemini.bedwards.almon.almonmonitoringactiveapi.service.ActiveAPIMonitorService;
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
@DiscriminatorValue(ActiveAPIMonitorAdapter.ID)
public class ActiveAPIMonitor extends ScheduledMonitor {
    @NotNull
    private HttpMethod method;
    @NotNull
    protected String url;

    @NotNull
    protected Integer expectedStatus;

    @NotNull
    private Integer maxResponseTime;

    protected String body;


    @ElementCollection(fetch = FetchType.EAGER)
    protected Map<String, String> headers;

    @ElementCollection(fetch = FetchType.EAGER)
    protected Map<String, @ValidJsonPath String> jsonPathValidations;

    @Override
    public ActiveAPIMonitoringTask getScheduledTask() {
        return BeanUtil.getBeanOfClass(ActiveAPIMonitorService.class).getScheduledTask(this);
    }

    @Override
    public String getMonitorType() {
        return ActiveAPIMonitorAdapter.ID;
    }
}
