package com.capgemini.bedwards.almon.almondatastore.models.service;

import com.capgemini.bedwards.almon.almondatastore.models.alert.AlertType;
import com.capgemini.bedwards.almon.almondatastore.models.auth.User;
import com.capgemini.bedwards.almon.almondatastore.util.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Set;

@Entity
@Builder
@Data
@AllArgsConstructor
@ToString
@Table(name = "services")
public class Service {

    @Id
    @Pattern(regexp = Constants.SERVICE_REGEX, message = Constants.SERVICE_REGEX_INVALID_MESSAGE)
    @NotNull
    private String id;

    @NotBlank
    private String name;

    private String description;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<User> users;

    @OneToMany
    private Set<AlertType> alertTypes;


    public Service() {

    }
}
