package com.capgemini.bedwards.almon.almonaccessweb.controller.notification;

import com.capgemini.bedwards.almon.almonaccessweb.controller.WebAPIController;
import com.capgemini.bedwards.almon.almoncore.services.notification.WebNotificationService;
import com.capgemini.bedwards.almon.almoncore.util.SecurityUtil;
import com.capgemini.bedwards.almon.almondatastore.models.notification.WebNotification;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/web/notification/{notificationId}")
@Slf4j
@PreAuthorize("isAuthenticated()")
public class WebNotificationWebController extends WebAPIController {

  private final WebNotificationService WEB_NOTIFICATION_SERVICE;

  @Autowired
  public WebNotificationWebController(WebNotificationService webNotificationService) {
    this.WEB_NOTIFICATION_SERVICE = webNotificationService;
  }

  @PutMapping("/read")
  @ResponseBody
  public ResponseEntity<Object> readNotification(
      @Valid @PathVariable(name = "notificationId") WebNotification webNotification) {
    WEB_NOTIFICATION_SERVICE.read(SecurityUtil.getAuthenticatedUser(), webNotification);
    return new ResponseEntity<>(null, HttpStatus.OK);
  }
}
