package com.capgemini.bedwards.almon.almoncore.service;

import com.capgemini.bedwards.almon.almoncore.services.user.UserService;
import com.capgemini.bedwards.almon.almondatastore.models.auth.Authority;
import com.capgemini.bedwards.almon.almondatastore.models.auth.Role;
import com.capgemini.bedwards.almon.almondatastore.models.auth.UpdateType;
import com.capgemini.bedwards.almon.almondatastore.models.auth.User;
import com.capgemini.bedwards.almon.almondatastore.repository.auth.AuthorityRepository;
import com.capgemini.bedwards.almon.almondatastore.repository.auth.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class AuthorityServiceImpl implements AuthorityService {

    @Autowired
    AuthorityRepository authorityRepository;
    @Autowired
    UserService userService;

    @Override
    public void createAuthority(String authority, String description, User... defaultUsers) {
        log.info("Creating new authority with name: " + authority + " description: " + description);

        authorityRepository.save(Authority.builder()
                .authority(authority)
                .description(description)
                .users(new HashSet<User>() {{
                    if (defaultUsers != null)
                        this.addAll(Arrays.asList(defaultUsers));
                }})
                .build());
    }

    @Override
    public List<Authority> getAllAuthorities() {
        return authorityRepository.findAll();
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
                authorityRepository.save(authority);
            } else if (updateType == UpdateType.GRANT) {
                authority.getUsers().add(user);
                authorityRepository.save(authority);
            } else
                throw new RuntimeException("Update type " + updateType + " does not have any actions assigned to it");
        });
    }

    @Override
    public void addAuthorities(User user, String... authorities) {
        updateAuthorities(user,new HashMap<String, UpdateType>(){{
            for(String authority : authorities){
                put(authority,UpdateType.GRANT);
            }
        }});
    }

    @Override
    public void removeAuthorities(User user, String... authorities) {
        updateAuthorities(user,new HashMap<String, UpdateType>(){{
            for(String authority : authorities){
                put(authority,UpdateType.REMOVE);
            }
        }});
    }
}
