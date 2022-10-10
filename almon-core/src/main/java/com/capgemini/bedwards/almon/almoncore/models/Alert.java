package com.capgemini.bedwards.almon.almoncore.models;

import java.time.LocalDateTime;
import java.util.UUID;


public class Alert {
    private UUID id;
    private AlertType alertType;
    private LocalDateTime alertReceivedTime;
    private String message;
}
