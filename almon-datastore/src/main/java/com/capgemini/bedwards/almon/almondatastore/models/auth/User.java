package com.capgemini.bedwards.almon.almondatastore.models.auth;

import com.capgemini.bedwards.almon.almondatastore.models.service.Service;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "com.capgemini.bedwards.almon.almondatastore.util.UUIDGenerator")
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID id;

    @NotNull
    private String email;
    @Nullable
    private String phoneNumber;

    @NotNull
    private String firstName;
    @NotNull
    private String lastName;

    @NotNull
    private String password;
    @NotNull
    private boolean enabled = false;

    @JoinColumn(name = "approvedBy_id")
    @OneToOne(fetch = FetchType.EAGER)
    private User approvedBy;

    @ManyToMany(mappedBy = "users", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Authority> authorities;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;

    @OneToMany
    private Set<Service> ownerOfServices;

    @ManyToMany(mappedBy = "users", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Service> services;

    public User() {

    }

    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }


    public boolean isAuthorityFromRole(Authority authority) {
        return getRoles().stream().anyMatch(role -> role.getAuthorities().contains(authority)) ;
    }

}
