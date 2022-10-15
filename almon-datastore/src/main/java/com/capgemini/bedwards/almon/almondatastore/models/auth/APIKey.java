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
@ToString
@Table(name = "apiKeys")
public class APIKey {

    @Id
    private String apiKey;
    @OneToOne
    @NotNull
    private User owner;

    @ManyToMany(mappedBy = "apiKeys", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Authority> authorities;

}
