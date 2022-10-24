package com.capgemini.bedwards.almon.almonmonitoringcore.schedule;

import com.capgemini.bedwards.almon.almondatastore.models.ScheduledTask;
import com.capgemini.bedwards.almon.almonmonitoringcore.contracts.HasScheduledTasks;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ScheduledFuture;

@Service
@Slf4j
public class Scheduler {

    private final TaskScheduler TASK_SCHEDULER;

    private final Map<String, ScheduledFuture<?>> JOBS = new HashMap<>();

    @Autowired
    public Scheduler(TaskScheduler taskScheduler, List<HasScheduledTasks> tasksList) {
        this.TASK_SCHEDULER = taskScheduler;
        if (tasksList != null) {
            tasksList.forEach(hasScheduledTasks -> hasScheduledTasks.getScheduledTasks().forEach(this::scheduleTask));
        }
    }

    public void scheduleTask(ScheduledTask scheduledTask) {
        log.info("Scheduling task with job id: " + scheduledTask.getTASK_ID() + " and cron expression: '" + scheduledTask.getCronExpression() +"'");
        ScheduledFuture<?> scheduledFuture = TASK_SCHEDULER.schedule(scheduledTask, new CronTrigger(scheduledTask.getCronExpression(), TimeZone.getTimeZone(TimeZone.getDefault().getID())));
        JOBS.put(scheduledTask.getTASK_ID(), scheduledFuture);
    }

    public void removeScheduledTask(String jobId) {
        ScheduledFuture<?> scheduledTask = JOBS.get(jobId);
        if (scheduledTask != null) {
            scheduledTask.cancel(false);
            JOBS.remove(jobId);
        }
    }
}