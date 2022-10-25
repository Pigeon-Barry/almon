package com.capgemini.bedwards.almon.almondatastore.models.auth;

import lombok.*;
import org.springframework.lang.Nullable;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@Builder
public class Authority implements GrantedAuthority {

    @Id
    private String authority;
    @Nullable
    private String description;
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<User> users;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<APIKey> apiKeys;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;

    public Authority() {

    }

    @Override
    public int hashCode() {
        return authority.hashCode();
    }
    @Override
    public boolean equals(Object obj) {
        return authority.equals(obj);
    }

    @Embeddable
    @Data
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AuthorityId implements Serializable {
        @ManyToOne
        @JoinColumn(name = "user_id", nullable = false)
        private User user;
    }


    @Override
    public String toString() {
        return "Authority{" +
                "authority='" + authority + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
