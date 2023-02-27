package com.capgemini.bedwards.almon.almoncore.services.user;

import com.capgemini.bedwards.almon.almondatastore.models.auth.Authority;
import com.capgemini.bedwards.almon.almondatastore.models.auth.Role;
import com.capgemini.bedwards.almon.almondatastore.models.auth.UpdateType;
import com.capgemini.bedwards.almon.almondatastore.models.auth.User;
import com.capgemini.bedwards.almon.almondatastore.models.service.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface RoleService {
    boolean removeRole(User user, Role role);

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

    void deleteServiceRoles(Service service);

    void assignRoleToUsers(Role role, Set<User> users);
}
