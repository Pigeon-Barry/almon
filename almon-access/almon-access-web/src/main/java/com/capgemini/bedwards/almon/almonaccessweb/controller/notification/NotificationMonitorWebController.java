package com.capgemini.bedwards.almon.almonaccessweb.controller.notification;

import com.capgemini.bedwards.almon.almonaccessweb.controller.WebAPIController;
import com.capgemini.bedwards.almon.almoncore.intergrations.api.error.ErrorCode;
import com.capgemini.bedwards.almon.almoncore.intergrations.api.error.ErrorResponse;
import com.capgemini.bedwards.almon.almoncore.services.subscription.SubscriptionService;
import com.capgemini.bedwards.almon.almoncore.util.SecurityUtil;
import com.capgemini.bedwards.almon.almondatastore.models.contract.Notification;
import com.capgemini.bedwards.almon.almondatastore.models.monitor.Monitor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;


@Controller
@RequestMapping("/web/notification/monitor/{monitorId}")
@Slf4j
@PreAuthorize("isAuthenticated()")
public class NotificationMonitorWebController extends WebAPIController {

    public final SubscriptionService SUBSCRIPTION_SERVICE;

    @Autowired
    public NotificationMonitorWebController(SubscriptionService subscriptionService) {
        this.SUBSCRIPTION_SERVICE = subscriptionService;
    }

    @PutMapping("/{notificationId}/subscribe")
    public ResponseEntity<Object> subscribe(
            @Valid @PathVariable(name = "monitorId")
            Monitor monitor,
            @Valid @PathVariable(name = "notificationId")
            Notification notification,
            Model model) {
        boolean result = this.SUBSCRIPTION_SERVICE.subscribe(SecurityUtil.getAuthenticatedUser(), monitor, notification);

        if (result)
            return new ResponseEntity<>(null, HttpStatus.OK);
        else
            return new ResponseEntity<>(new ErrorResponse(ErrorCode.ALREADY_SUBSCRIBED), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @PutMapping("/{notificationId}/unsubscribe")
    public ResponseEntity<Object> unsubscribe(
            @Valid @PathVariable(name = "monitorId")
            Monitor monitor,
            @Valid @PathVariable(name = "notificationId")
            Notification notification,
            Model model) {

        boolean result = this.SUBSCRIPTION_SERVICE.unsubscribe(SecurityUtil.getAuthenticatedUser(), monitor, notification);
        if (result)
            return new ResponseEntity<>(null, HttpStatus.OK);
        else
            return new ResponseEntity<>(new ErrorResponse(ErrorCode.NOT_SUBSCRIBED), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @DeleteMapping("/subscriptions")
    public ResponseEntity<Object> clearSubscriptions(
            @Valid @PathVariable(name = "monitorId")
            Monitor monitor,
            Model model) {
        this.SUBSCRIPTION_SERVICE.clearSubscriptions(SecurityUtil.getAuthenticatedUser(), monitor);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
