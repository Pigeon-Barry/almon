package com.capgemini.bedwards.almon.almoncore.util;

import com.capgemini.bedwards.almon.almondatastore.models.auth.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtil {
    private SecurityUtil() {
    }

    public static User getAuthenticatedUser() {
        if (!getAuthentication().isAuthenticated() || !(getAuthentication().getPrincipal() instanceof User))
            return null;
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

    public static boolean hasAuthority(String authority) {
        return getAuthentication().getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(authority));
    }
}
