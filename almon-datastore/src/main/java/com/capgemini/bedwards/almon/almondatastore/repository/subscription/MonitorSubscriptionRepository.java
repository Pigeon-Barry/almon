package com.capgemini.bedwards.almon.almondatastore.repository.subscription;

import com.capgemini.bedwards.almon.almondatastore.models.auth.User;
import com.capgemini.bedwards.almon.almondatastore.models.monitor.Monitor;
import com.capgemini.bedwards.almon.almondatastore.models.subscription.MonitorSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface MonitorSubscriptionRepository extends JpaRepository<MonitorSubscription, MonitorSubscription.SubscriptionId> {
    @Transactional
    void deleteById_UserAndId_Monitor(User user, Monitor monitor);

    @Query("SELECT ms FROM MonitorSubscription ms WHERE ms.id.notificationType = ?1 and ms.id.monitor = ?2")
    List<MonitorSubscription> getFromNotificationId(String notificationId, Monitor monitor);

    void deleteAllById_Monitor(Monitor monitor);
}
