package com.capgemini.bedwards.almon.almondatastore.models.auth;

import lombok.*;
import org.springframework.lang.Nullable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Role {

    @Id
    private String name;
    @Nullable
    private String description;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.EAGER)
    private Set<User> users;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.EAGER)
    private Set<Authority> authorities;

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return this.name.equals(obj);
    }
}
