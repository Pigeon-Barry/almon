package com.capgemini.bedwards.almon.almondatastore.models.auth;

import lombok.*;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@ToString
@Table(name = "authorities")
public class Authority {

    @Id
    private String authority;
    @Nullable
    private String description;
    @ManyToMany
    private Set<User> users;

    @ManyToMany
    private Set<APIKey> apiKeys;

    @ManyToMany
    private Set<Role> roles;

    public Authority() {

    }

    @Override
    public int hashCode() {
        return authority.hashCode();
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
}
