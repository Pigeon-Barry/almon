package com.capgemini.bedwards.almon.almondatastore.models.schedule;

import com.capgemini.bedwards.almon.almondatastore.util.HasScheduledTasks;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ScheduledFuture;

@Service
@Transactional
@Slf4j
public class Scheduler {

  private final TaskScheduler TASK_SCHEDULER;

  private final Map<String, ScheduledFuture<?>> JOBS = new HashMap<>();

  @Autowired
  public Scheduler(TaskScheduler taskScheduler, List<HasScheduledTasks<?>> tasksList) {
    this.TASK_SCHEDULER = taskScheduler;
    if (tasksList != null) {
      tasksList.forEach(
          hasScheduledTasks -> hasScheduledTasks.getScheduledTasks().forEach(this::scheduleTask));
    }
  }

  public void scheduleTask(ScheduledTask<?> scheduledTask) {
    if (!scheduledTask.isEnabled()) {
      log.info("Scheduled task with ID: " + scheduledTask.getTASK_ID() + " is disabled ignoring");
      return;
    }
    log.info(
        "Scheduling task with job id: " + scheduledTask.getTASK_ID() + " and cron expression: '"
            + scheduledTask.getCronExpression() + "'");
    ScheduledFuture<?> scheduledFuture = TASK_SCHEDULER.schedule(scheduledTask,
        new CronTrigger(scheduledTask.getCronExpression(),
            TimeZone.getTimeZone(TimeZone.getDefault().getID())));
    JOBS.put(scheduledTask.getTASK_ID(), scheduledFuture);
  }

  public void removeScheduledTask(String jobId) {
    log.info("Scheduled task with ID: " + jobId + " has been removed");
    ScheduledFuture<?> scheduledTask = JOBS.get(jobId);
    if (scheduledTask != null) {
      scheduledTask.cancel(false);
      JOBS.remove(jobId);
    }
  }

  public void refreshTask(ScheduledTask<?> scheduledTask) {
    if (hasJob(scheduledTask.getTASK_ID())) {
      removeScheduledTask(scheduledTask.getTASK_ID());
      scheduleTask(scheduledTask);
    }
  }

  private boolean hasJob(String task_id) {
    return this.JOBS.containsKey(task_id);
  }
}
