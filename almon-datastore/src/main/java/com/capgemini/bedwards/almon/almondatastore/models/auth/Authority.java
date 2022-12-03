package com.capgemini.bedwards.almon.almondatastore.models.auth;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.lang.Nullable;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Data
@AllArgsConstructor
@Builder
@Table(name = "authority")
public class Authority implements GrantedAuthority {

    @Id
    private String authority;
    @Nullable
    private String description;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<User> users;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<APIKey> apiKeys;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
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
