package com.capgemini.bedwards.almon.almoncore.services.auth;

import com.capgemini.bedwards.almon.almondatastore.models.auth.User;

public interface AuthService {
    User getAuthenticatedUser(String name, String encodedPassword);

    User register(String email, String firstname, String lastname, String password);

    boolean checkUserExists(String email);

}
