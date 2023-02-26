package com.capgemini.bedwards.almon.almondatastore.models.notification;

import com.capgemini.bedwards.almon.almondatastore.models.auth.User;
import com.capgemini.bedwards.almon.almondatastore.util.Constants;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@ToString
@SuperBuilder
@Entity
public class WebNotification {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "com.capgemini.bedwards.almon.almondatastore.util.UUIDGenerator")
    @Type(type = "org.hibernate.type.UUIDCharType")
    protected UUID id;

    @NotNull
    @Builder.Default
    @Column(nullable = false, updatable = false, columnDefinition = "TIMESTAMP default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP")
    protected LocalDateTime createdAt = LocalDateTime.now();

    @NotNull
    @Column(length = Constants.ALERT_SHORT_MAX_LENGTH)
    protected String title;
    @NotNull
    @Column(length = Constants.ALERT_LONG_MAX_LENGTH)
    protected String message;

    @ElementCollection(fetch = FetchType.EAGER)
    @Column(name = "hasRead")
    @MapKeyJoinColumn(name = "user_id")
    protected Map<User, Boolean> sentTO;


    public Map<User, Boolean> getSentTO() {
        if (this.sentTO == null)
            this.sentTO = new HashMap<>();
        return this.sentTO;
    }
}
