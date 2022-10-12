package com.capgemini.bedwards.almon.almondatastore.models.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@ToString
@Table(name = "authorities")
public class Authority {

    @EmbeddedId
    private AuthorityId authorityId;


    public Authority() {

    }

    @Embeddable
    private static class AuthorityId implements Serializable {
        @JoinColumn(name = "users", referencedColumnName = "username")
        private String username;

        private String authority;
    }
}
