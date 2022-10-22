package com.capgemini.bedwards.almon.almoncore.service;

import com.capgemini.bedwards.almon.almondatastore.models.auth.Authority;
import com.capgemini.bedwards.almon.almondatastore.models.auth.UpdateType;
import com.capgemini.bedwards.almon.almondatastore.models.auth.User;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface AuthorityService {
    void createAuthority(String authority, String description, User... defaultUsers);

    List<Authority> getAllAuthorities();

    void updateAuthorities(User user, Map<String, UpdateType> authorities);
    void addAuthorities(User user, String... authorities);
    void removeAuthorities(User user, String... authorities);
}
