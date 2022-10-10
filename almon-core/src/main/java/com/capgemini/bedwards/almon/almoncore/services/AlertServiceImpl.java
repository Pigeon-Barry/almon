package com.capgemini.bedwards.almon.almoncore.services;

import com.capgemini.bedwards.almon.almoncore.models.Alert;
import com.capgemini.bedwards.almon.almoncore.models.AlertType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class AlertServiceImpl implements AlertService {
    @Override
    public Alert saveAlert(AlertType alertType, String message, LocalDateTime timeOfAlert) {
        log.info("Alert received");
        return new Alert();
    }
}
