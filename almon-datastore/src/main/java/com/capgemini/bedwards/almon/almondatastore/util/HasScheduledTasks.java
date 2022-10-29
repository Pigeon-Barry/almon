package com.capgemini.bedwards.almon.almondatastore.util;

import com.capgemini.bedwards.almon.almondatastore.models.schedule.ScheduledTask;

import java.util.Set;

public interface HasScheduledTasks {
    Set<ScheduledTask> getScheduledTasks();
}
