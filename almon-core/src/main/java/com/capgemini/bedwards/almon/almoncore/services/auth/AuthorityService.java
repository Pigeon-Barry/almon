package com.capgemini.bedwards.almon.almoncore.services.auth;

import com.capgemini.bedwards.almon.almondatastore.models.auth.Authority;
import com.capgemini.bedwards.almon.almondatastore.models.auth.Role;
import com.capgemini.bedwards.almon.almondatastore.models.auth.UpdateType;
import com.capgemini.bedwards.almon.almondatastore.models.auth.User;
import com.capgemini.bedwards.almon.almondatastore.models.monitor.Monitor;
import com.capgemini.bedwards.almon.almondatastore.models.service.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface AuthorityService {
    Authority createAuthority(String authority, String description, Set<User> defaultUsers, Set<Role> roles);

    default Authority createAuthority(String authority, String description) {
        return createAuthority(authority, description, null, null);
    }



    List<Authority> getAllAuthorities();

    void updateAuthorities(User user, Map<String, UpdateType> authorities);

    void addAuthorities(User user, String... authorities);

    void removeAuthorities(User user, String... authorities);

    void addRole(Authority authority, Set<Role> roles);

    Authority save(Authority authority);

    void deleteServiceAuthorities(Service service);

    void deleteMonitorAuthorities(Monitor monitor);

}
