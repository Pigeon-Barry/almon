package com.capgemini.bedwards.almon.almondatastore.repository.notification;

import com.capgemini.bedwards.almon.almondatastore.models.auth.User;
import com.capgemini.bedwards.almon.almondatastore.models.notification.WebNotification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface WebNotificationRepository extends JpaRepository<WebNotification, UUID> {

  @Query("select wn from WebNotification wn where ?1 in (KEY(wn.sentTO)) ORDER BY wn.createdAt DESC")
  List<WebNotification> findAllByUser(User user);

  @Query("select wn from WebNotification wn where ?1 in (KEY(wn.sentTO)) ORDER BY wn.createdAt DESC")
  Page<WebNotification> findAllByUser(User user, Pageable pageable);

  @Query("select count(wn) from WebNotification wn join wn.sentTO st where (KEY(st) = :user and st = false)")
  long countWebNotificationBySentTOTrue(@Param("user") User user);

}
