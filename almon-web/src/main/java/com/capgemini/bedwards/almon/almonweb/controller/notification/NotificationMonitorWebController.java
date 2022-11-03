package com.capgemini.bedwards.almon.almonweb.controller.notification;

import com.capgemini.bedwards.almon.almoncore.exceptions.NotFoundException;
import com.capgemini.bedwards.almon.almoncore.intergrations.api.error.ErrorCode;
import com.capgemini.bedwards.almon.almoncore.intergrations.api.error.ErrorResponse;
import com.capgemini.bedwards.almon.almoncore.util.SecurityUtil;
import com.capgemini.bedwards.almon.almondatastore.models.contract.Notification;
import com.capgemini.bedwards.almon.almondatastore.models.monitor.Monitor;
import com.capgemini.bedwards.almon.notificationcore.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.validation.Valid;


@Controller
@RequestMapping("/web/notification/monitor/{monitorId}/{notificationId}")
@Slf4j
@PreAuthorize("isAuthenticated()")
public class NotificationMonitorWebController {

    public final NotificationService NOTIFICATION_SERVICE;

    @Autowired
    public NotificationMonitorWebController(NotificationService notificationService) {
        this.NOTIFICATION_SERVICE = notificationService;
    }

    @PutMapping("/subscribe")
    public ResponseEntity<Object> subscribe(
            @Valid @PathVariable(name = "monitorId")
            Monitor monitor,
            @Valid @PathVariable(name = "notificationId")
            Notification notification,
            Model model) {
        boolean result = this.NOTIFICATION_SERVICE.subscribe(SecurityUtil.getAuthenticatedUser(), monitor, notification);

        if (result)
            return new ResponseEntity<>(null, HttpStatus.OK);
        else
            return new ResponseEntity<>(new ErrorResponse(ErrorCode.ALREADY_SUBSCRIBED), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @PutMapping("/unsubscribe")
    public ResponseEntity<Object> unsubscribe(
            @Valid @PathVariable(name = "monitorId")
            Monitor monitor,
            @Valid @PathVariable(name = "notificationId")
            Notification notification,
            Model model) {

        boolean result = this.NOTIFICATION_SERVICE.unsubscribe(SecurityUtil.getAuthenticatedUser(), monitor, notification);
        if (result)
            return new ResponseEntity<>(null, HttpStatus.OK);
        else
            return new ResponseEntity<>(new ErrorResponse(ErrorCode.NOT_SUBSCRIBED), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ResponseEntity<?> notFoundException(NotFoundException exception, WebRequest request) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }
}
