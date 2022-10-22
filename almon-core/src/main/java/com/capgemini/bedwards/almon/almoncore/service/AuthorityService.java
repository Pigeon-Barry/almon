package com.capgemini.bedwards.almon.almoncore.service;

import com.capgemini.bedwards.almon.almondatastore.models.auth.Authority;
import com.capgemini.bedwards.almon.almondatastore.models.auth.UpdateType;
import com.capgemini.bedwards.almon.almondatastore.models.auth.User;

import java.util.List;
import java.util.Map;

public interface AuthorityService {
    void createAuthority(String authority, String description, User... defaultUsers);

    List<Authority> getAllAuthorities();

    void updateAuthorities(User user, Map<String, UpdateType> authorities);
}
