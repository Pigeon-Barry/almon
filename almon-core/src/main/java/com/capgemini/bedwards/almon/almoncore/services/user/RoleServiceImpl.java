package com.capgemini.bedwards.almon.almoncore.services.user;

import com.capgemini.bedwards.almon.almoncore.exceptions.NotFoundException;
import com.capgemini.bedwards.almon.almondatastore.models.auth.Authority;
import com.capgemini.bedwards.almon.almondatastore.models.auth.Role;
import com.capgemini.bedwards.almon.almondatastore.models.auth.UpdateType;
import com.capgemini.bedwards.almon.almondatastore.models.auth.User;
import com.capgemini.bedwards.almon.almondatastore.repository.auth.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class RoleServiceImpl implements RoleService {


    private final RoleRepository ROLE_REPOSITORY;

    private final UserService USER_SERVICE;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository, UserService userService) {
        this.ROLE_REPOSITORY = roleRepository;
        this.USER_SERVICE = userService;
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    @Override
    public void removeRole(UUID userId, String roleName) {
        Optional<User> userOptional = USER_SERVICE.findById(userId);
        if (!userOptional.isPresent())
            throw new NotFoundException("User with ID: " + userId + " could not be located");
        User user = userOptional.get();
        log.info("CLS: " + user.getRoles().getClass());


        Optional<Role> roleToDeleteOptional = user.getRoles().stream().filter(role -> role.getName().equals(roleName)).findFirst();
        if (roleToDeleteOptional.isPresent()) {
            user.getRoles().remove(roleToDeleteOptional.get());
            USER_SERVICE.save(user);
        } else {
            throw new NotFoundException("User does not have role " + roleName);
        }
    }

    @Override
    public List<Role> getAllRoles() {
        return ROLE_REPOSITORY.findAll();
    }

    @Override
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
    public void assignRole(User user, String... roles) {
        updateRoles(user, new HashMap<String, UpdateType>() {{
            for (String role : roles) {
                put(role, UpdateType.GRANT);
            }
        }});
    }

    @Override
    public void assignRole(User user, Role... roles) {
        user.getRoles().addAll(Arrays.asList(roles));
        USER_SERVICE.save(user);
    }



    @Override
    public Role createRole(String name, String description, Set<Authority> authorities) {
        log.info("Creating new Role with name: " + name + " description: " + description);
        return ROLE_REPOSITORY.save(Role.builder()
                .name(name)
                .description(description)
                .authorities(authorities)
                .build());
    }

    @Override
    public Optional<Role> getRoleFromName(String roleName) {
        return ROLE_REPOSITORY.findById(roleName);
    }

    @Override
    public Role findOrCreate(String name, String description) {
        Optional<Role> roleOptional = getRoleFromName(name);
        return roleOptional.orElseGet(() -> createRole(name, description));
    }
}
