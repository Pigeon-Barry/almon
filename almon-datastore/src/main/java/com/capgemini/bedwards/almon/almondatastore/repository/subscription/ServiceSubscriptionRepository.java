package com.capgemini.bedwards.almon.almondatastore.repository.subscription;

import com.capgemini.bedwards.almon.almondatastore.models.subscription.ServiceSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceSubscriptionRepository extends JpaRepository<ServiceSubscription, ServiceSubscription.SubscriptionId> {
}
