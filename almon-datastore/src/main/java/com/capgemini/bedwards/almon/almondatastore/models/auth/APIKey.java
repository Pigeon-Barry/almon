package com.capgemini.bedwards.almon.almondatastore.models.auth;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class APIKey {

    @Id
    private String apiKey;
    @OneToOne
    @NotNull
    private User owner;

    private boolean enabled = true;

    @ManyToMany(mappedBy = "apiKeys", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private Set<Authority> authorities;

    @ManyToMany(mappedBy = "apiKeys", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private Set<Role> roles;

    @Override
    public String toString() {
        return "APIKey{" +
                "apiKey='" + apiKey + '\'' +
                ", owner=" + owner +
                ", enabled=" + enabled +
                ", authorities=" + authorities +
                ", roles=" + roles +
                '}';
    }
}
