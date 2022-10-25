package com.capgemini.bedwards.almon.almoncore.services.user;

import com.capgemini.bedwards.almon.almondatastore.models.auth.Authority;
import com.capgemini.bedwards.almon.almondatastore.models.auth.Role;
import com.capgemini.bedwards.almon.almondatastore.models.auth.UpdateType;
import com.capgemini.bedwards.almon.almondatastore.models.auth.User;

import java.util.*;

public interface RoleService {
    void removeRole(UUID userId, String roleName);

    List<Role> getAllRoles();
    void updateRoles(User user, Map<String, UpdateType> roles);


    void assignRole(User owner, String... roles);
    void assignRole(User owner, Role... roles);

    Role createRole(String name, String description, Set<Authority> authorities);

    default Role createRole(String name, String description) {
        return createRole(name, description, null);
    }

    Optional<Role> getRoleFromName(String roleName);

    Role findOrCreate(String name, String description);
}
