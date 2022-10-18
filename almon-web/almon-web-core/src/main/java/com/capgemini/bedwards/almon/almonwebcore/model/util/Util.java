package com.capgemini.bedwards.almon.almonwebcore.model.util;

import com.capgemini.bedwards.almon.almondatastore.models.auth.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class Util {


    public static User getAuthenticatedUser() {
        return (User) getAuthentication().getPrincipal();
    }

    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
    public static void setAuthentication(Authentication authentication) {
         SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public static void clearAuthentication() {
        SecurityContextHolder.clearContext();
    }
}
