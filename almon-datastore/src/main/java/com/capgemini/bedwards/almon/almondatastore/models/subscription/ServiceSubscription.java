package com.capgemini.bedwards.almon.almondatastore.models.subscription;


import com.capgemini.bedwards.almon.almondatastore.models.auth.User;
import com.capgemini.bedwards.almon.almondatastore.models.service.Service;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceSubscription {

    @EmbeddedId
    private SubscriptionId id;

    private boolean subscribed;

    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    public static class SubscriptionId implements Serializable {
        private String notificationType;
        @ManyToOne
        private Service service;
        @ManyToOne
        private User user;
    }
}
