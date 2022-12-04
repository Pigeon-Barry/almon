package com.capgemini.bedwards.almon.almondatastore.models.auth;

import com.capgemini.bedwards.almon.almondatastore.models.subscription.MonitorSubscription;
import com.capgemini.bedwards.almon.almondatastore.models.subscription.ServiceSubscription;
import java.util.Set;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.lang.Nullable;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Builder
public class User {

  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "com.capgemini.bedwards.almon.almondatastore.util.UUIDGenerator")
  @Type(type = "org.hibernate.type.UUIDCharType")
  @Column(length = 36)
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

  @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private Set<Role> roles;

  @OneToMany(mappedBy = "id.user", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
  private Set<ServiceSubscription> serviceSubscriptions;

  @OneToMany(mappedBy = "id.user", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
  private Set<MonitorSubscription> monitorSubscriptions;

  public String getProfilePage() {
    return "/web/user/" + getId();
  }

  public String getProfilePicture() {
    return "/web/img/ProfilePicturePlaceholder.png";
  }

  public User() {

  }

  public String getFullName() {
    return this.firstName + " " + this.lastName;
  }

  public boolean isAuthorityFromRole(Authority authority) {
    return getRoles().stream().anyMatch(role -> role.getAuthorities().contains(authority));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    User user = (User) o;
    return id.equals(user.id);
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }
}
