package com.capgemini.bedwards.almon.almondatastore.repository.notification;

import com.capgemini.bedwards.almon.almondatastore.models.auth.User;
import com.capgemini.bedwards.almon.almondatastore.models.notification.WebNotification;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WebNotificationRepository extends JpaRepository<WebNotification, UUID> {

  @Query("select wn from WebNotification wn where ?1 in (KEY(wn.sentTO))")
  List<WebNotification> findAllByUser(User user);
}
