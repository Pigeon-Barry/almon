package com.capgemini.bedwards.almon.almondatastore.models.auth;

import lombok.*;
import org.springframework.lang.Nullable;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

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

  public void removeUser(User user) {
    this.getUsers().remove(user);
  }

  public void addUser(User user) {
    this.getUsers().add(user);
  }

  public Set<User> getUsers() {
    if (this.users == null) {
      this.users = new HashSet<>();
    }
    return this.users;
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
