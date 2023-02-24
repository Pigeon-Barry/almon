package com.capgemini.bedwards.almon.almoncore.services.user;

import com.capgemini.bedwards.almon.almondatastore.models.auth.Authority;
import com.capgemini.bedwards.almon.almondatastore.models.auth.Role;
import com.capgemini.bedwards.almon.almondatastore.models.auth.UpdateType;
import com.capgemini.bedwards.almon.almondatastore.models.auth.User;
import com.capgemini.bedwards.almon.almondatastore.repository.auth.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.*;

@Service
@Slf4j
@Transactional
public class RoleServiceImpl implements RoleService {


    private final RoleRepository ROLE_REPOSITORY;

    private final UserService USER_SERVICE;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository, UserService userService) {
        this.ROLE_REPOSITORY = roleRepository;
        this.USER_SERVICE = userService;
    }

    @Override
    @Transactional
    public boolean removeRole(User user, Role role) {
        if (user.getRoles().remove(role)) {
            USER_SERVICE.save(user);
            return true;
        }
        return false;
    }

    @Override
    public List<Role> getAllRoles() {
        return ROLE_REPOSITORY.findAll();
    }

    @Override
    @Transactional
    public void updateRoles(User user, Map<String, UpdateType> rolesToUpdate) {
        List<Role> roles = getAllRoles();
        roles.forEach(role -> {
            UpdateType updateType = rolesToUpdate.getOrDefault(role.getName(), null);
            if (updateType == null) {
                //Do nothing
            } else if (updateType == UpdateType.REMOVE)
                user.getRoles().remove(role);
            else if (updateType == UpdateType.GRANT)
                user.getRoles().add(role);
            else
                throw new RuntimeException("Update type " + updateType + " does not have any actions assigned to it");
        });
        USER_SERVICE.save(user);
    }

    @Override
    @Transactional
    public void assignRole(User user, String... roles) {
        updateRoles(user, new HashMap<String, UpdateType>() {{
            for (String role : roles) {
                put(role, UpdateType.GRANT);
            }
        }});
    }

    @Override
    @Transactional
    public void assignRole(User user, Role... roles) {
        user.getRoles().addAll(Arrays.asList(roles));
        USER_SERVICE.save(user);
    }

    @Override
    @Transactional
    public void assignRoleToUsers(Role role, Set<User> users) {
        for (User user : users) {
            assignRole(user, role);
        }
    }

    @Override
    @Transactional
    public Role createRole(String name, String description, Set<Authority> authorities) {
        log.info("Creating new Role with name: " + name + " description: " + description);
        return ROLE_REPOSITORY.save(Role.builder()
                .name(name)
                .description(description)
                .authorities(authorities)
                .build());
    }

    @Override
    @Transactional
    public Optional<Role> getRoleFromName(String roleName) {
        return ROLE_REPOSITORY.findById(roleName);
    }

    @Override
    @Transactional
    public Role findOrCreate(String name, String description) {
        Optional<Role> roleOptional = getRoleFromName(name);
        return roleOptional.orElseGet(() -> createRole(name, description));
    }

    @Override
    @Transactional
    public void deleteServiceRoles(com.capgemini.bedwards.almon.almondatastore.models.service.Service service) {
        ROLE_REPOSITORY.deleteServiceRoles(service);
    }

    @Override
    public Set<User> getUsersByRole(Role role) {
        return role.getUsers();
    }


}
