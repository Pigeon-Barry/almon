package com.capgemini.bedwards.almon.almoncore.services.auth;

import com.capgemini.bedwards.almon.almoncore.services.user.UserService;
import com.capgemini.bedwards.almon.almondatastore.models.auth.Authority;
import com.capgemini.bedwards.almon.almondatastore.models.auth.Role;
import com.capgemini.bedwards.almon.almondatastore.models.auth.UpdateType;
import com.capgemini.bedwards.almon.almondatastore.models.auth.User;
import com.capgemini.bedwards.almon.almondatastore.models.monitor.Monitor;
import com.capgemini.bedwards.almon.almondatastore.repository.auth.AuthorityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class AuthorityServiceImpl implements AuthorityService {


    private final AuthorityRepository AUTHORITY_REPOSITORY;

    private final UserService USER_SERVICE;

    @Autowired
    public AuthorityServiceImpl(AuthorityRepository authorityRepository, UserService userService) {
        this.AUTHORITY_REPOSITORY = authorityRepository;
        this.USER_SERVICE = userService;
    }

    @Override
    public Authority createAuthority(String authority, String description, Set<User> defaultUsers, Set<Role> roles) {
        log.info("Creating new authority with name: " + authority + " description: " + description);
        return save(Authority.builder()
                .authority(authority)
                .description(description)
                .users(defaultUsers)
                .roles(roles)
                .build());
    }

    @Override
    public List<Authority> getAllAuthorities() {
        return AUTHORITY_REPOSITORY.findAll();
    }

    @Override
    public void updateAuthorities(User user, Map<String, UpdateType> authoritiesToUpdate) {
        log.debug("Updating Authorities for user: " + user.getId());
        List<Authority> authorities = getAllAuthorities();

        for (Map.Entry<String, UpdateType> entry : authoritiesToUpdate.entrySet()) {
            log.info("Key: " + entry.getKey() + " : Value: " + entry.getValue());
        }

        authorities.forEach(authority -> {
            UpdateType updateType = authoritiesToUpdate.getOrDefault(authority.getAuthority(), null);
            log.info("Authority: " + authority.getAuthority() + " : " + updateType);
            if (updateType == null) {
                //Do nothing
            } else if (updateType == UpdateType.REMOVE) {
                authority.getUsers().remove(user);
                save(authority);
            } else if (updateType == UpdateType.GRANT) {
                authority.getUsers().add(user);
               save(authority);
            } else
                throw new RuntimeException("Update type " + updateType + " does not have any actions assigned to it");
        });
    }

    @Override
    public void addAuthorities(User user, String... authorities) {
        updateAuthorities(user, new HashMap<String, UpdateType>() {{
            for (String authority : authorities) {
                put(authority, UpdateType.GRANT);
            }
        }});
    }

    @Override
    public void removeAuthorities(User user, String... authorities) {
        updateAuthorities(user, new HashMap<String, UpdateType>() {{
            for (String authority : authorities) {
                put(authority, UpdateType.REMOVE);
            }
        }});
    }

    @Override
    public void addRole(Authority authority, Set<Role> roles) {
        if (authority.getRoles() == null)
            authority.setRoles(new HashSet<>());
        authority.getRoles().addAll(roles);
        save(authority);
    }

    @Override
    public Authority save(Authority authority) {
        log.info("Saving authority: " + authority);
        return AUTHORITY_REPOSITORY.saveAndFlush(authority);
    }

    @Override
    public void deleteServiceAuthorities(com.capgemini.bedwards.almon.almondatastore.models.service.Service service) {
        AUTHORITY_REPOSITORY.deleteServiceAuthorities(service);
    }

    @Override
    public void deleteMonitorAuthorities(Monitor monitor) {
        AUTHORITY_REPOSITORY.deleteMonitorAuthorities(monitor);
    }
}
