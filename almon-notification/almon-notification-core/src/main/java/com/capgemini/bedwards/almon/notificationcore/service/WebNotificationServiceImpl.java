package com.capgemini.bedwards.almon.notificationcore.service;

import com.capgemini.bedwards.almon.almoncore.exceptions.NotFoundException;
import com.capgemini.bedwards.almon.almoncore.services.notification.WebNotificationService;
import com.capgemini.bedwards.almon.almondatastore.models.auth.User;
import com.capgemini.bedwards.almon.almondatastore.models.notification.WebNotification;
import com.capgemini.bedwards.almon.almondatastore.repository.notification.WebNotificationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@Slf4j
public class WebNotificationServiceImpl implements WebNotificationService {


  private final WebNotificationRepository WEB_NOTIFICATION_REPOSITORY;

  @Autowired
  public WebNotificationServiceImpl(WebNotificationRepository userNotificationRepository) {
    this.WEB_NOTIFICATION_REPOSITORY = userNotificationRepository;
  }

  @Override
  public void save(WebNotification notification) {
    WEB_NOTIFICATION_REPOSITORY.save(notification);
  }

  @Override
  public List<WebNotification> getNotifications(User user) {
    return WEB_NOTIFICATION_REPOSITORY.findAllByUser(user);
  }

  @Override
  public Page<WebNotification> getNotifications(User user, int pageNo, int pageSize) {
    Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
    return WEB_NOTIFICATION_REPOSITORY.findAllByUser(user, pageable);
  }

  @Override
  public void read(User user, WebNotification webNotification) {
    if (!webNotification.getSentTO().containsKey(user)) {
      throw new NotFoundException("Web Notification was not sent to user: " + user.getId());
    }
    webNotification.getSentTO().put(user, true);
    save(webNotification);
  }

  @Override
  public Optional<WebNotification> findById(UUID id) {
    return WEB_NOTIFICATION_REPOSITORY.findById(id);

  }

  @Override
  public WebNotification findByWebNotificationId(UUID id) {
    Optional<WebNotification> webNotificationOptional = findById(id);
    if (webNotificationOptional.isPresent()) {
      return webNotificationOptional.get();
    }
    throw new NotFoundException("Web Notification with id: " + id + " not found");
  }
}
