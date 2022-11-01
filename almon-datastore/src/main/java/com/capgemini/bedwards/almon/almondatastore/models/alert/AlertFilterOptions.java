package com.capgemini.bedwards.almon.almondatastore.models.alert;

import com.capgemini.bedwards.almon.almondatastore.models.monitor.Monitor;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class AlertFilterOptions {
    private Status[] status;

    private Monitor[] monitors;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime from;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime to;
}
