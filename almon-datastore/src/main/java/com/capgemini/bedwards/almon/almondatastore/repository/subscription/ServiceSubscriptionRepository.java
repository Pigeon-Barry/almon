package com.capgemini.bedwards.almon.almondatastore.repository.subscription;

import com.capgemini.bedwards.almon.almondatastore.models.auth.User;
import com.capgemini.bedwards.almon.almondatastore.models.subscription.ServiceSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ServiceSubscriptionRepository extends JpaRepository<ServiceSubscription, ServiceSubscription.SubscriptionId> {

    @Query("SELECT ms FROM ServiceSubscription ms WHERE ms.id.notificationType = ?1")
    List<ServiceSubscription> getFromNotificationId(String notificationId);
    @Query("SELECT ms FROM ServiceSubscription ms WHERE ms.id.notificationType = ?1 and ms.id.user NOT IN ?2")
    List<ServiceSubscription> getFromNotificationIdWhereNotUser(String notificationId, Set<User> users);
}
