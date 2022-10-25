package com.capgemini.bedwards.almon.almonmonitoringcore.contracts;

import com.capgemini.bedwards.almon.almonmonitoringcore.schedule.ScheduledTask;

import java.util.Set;

public interface HasScheduledTasks {
    Set<ScheduledTask> getScheduledTasks();
}
