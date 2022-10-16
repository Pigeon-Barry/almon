package com.capgemini.bedwards.almon.almoncore.services.user;

import com.capgemini.bedwards.almon.almondatastore.models.auth.Role;
import com.capgemini.bedwards.almon.almondatastore.models.auth.UpdateType;
import com.capgemini.bedwards.almon.almondatastore.models.auth.User;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface RoleService {
    void removeRole(UUID userId, String roleName);

    List<Role> getAllRoles();
    void updateRoles(User user, Map<String, UpdateType> roles);
}
