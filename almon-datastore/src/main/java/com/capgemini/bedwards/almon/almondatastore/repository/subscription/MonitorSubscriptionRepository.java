package com.capgemini.bedwards.almon.almondatastore.repository.subscription;

import com.capgemini.bedwards.almon.almondatastore.models.subscription.MonitorSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonitorSubscriptionRepository extends JpaRepository<MonitorSubscription, MonitorSubscription.SubscriptionId> {
}
