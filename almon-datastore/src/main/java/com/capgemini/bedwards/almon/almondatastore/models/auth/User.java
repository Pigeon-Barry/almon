package com.capgemini.bedwards.almon.almondatastore.models.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Data
@AllArgsConstructor
@ToString
@Table(name = "users")
public class User {

    @Id
    private String username;
    @NotNull
    private String password;
    @NotNull
    private boolean enabled;

    public User() {

    }
}
