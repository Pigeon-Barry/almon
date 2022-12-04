package com.capgemini.bedwards.almon.almondatastore.repository.notification;

import com.capgemini.bedwards.almon.almondatastore.models.notification.WebNotification;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WebNotificationRepository extends JpaRepository<WebNotification, UUID> {

}
