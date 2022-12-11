package com.capgemini.bedwards.almon.almoncore.util;

import com.capgemini.bedwards.almon.almoncore.services.user.UserService;
import com.capgemini.bedwards.almon.almondatastore.models.auth.APIKey;
import com.capgemini.bedwards.almon.almondatastore.models.auth.Authority;
import com.capgemini.bedwards.almon.almondatastore.models.auth.User;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
public final class SecurityUtil {

    private SecurityUtil() {
    }

    public static User getAuthenticatedUser() {
        if (getAuthentication() == null || !getAuthentication().isAuthenticated() || !(getAuthentication().getPrincipal() instanceof User))
            return null;
        return (User) getAuthentication().getPrincipal();
    }

    public static Collection<Authority> getAuthoritiesFromUser(User user){
        log.info("AUTH: " + user);
        List<Authority> authorities = new ArrayList<>();
        if (user.isEnabled()) {
            if (user.getAuthorities() != null)
                authorities.addAll(user.getAuthorities());
            log.info("AUTH");
            if (user.getRoles() != null) {
                user.getRoles().forEach(role -> {
                    log.info("ROLE: " + role.getName());
                    log.info("ROLE AUTH: " + Arrays.toString(role.getAuthorities().toArray()));
                    authorities.addAll(role.getAuthorities());
                });
            }
        }
        return authorities;
    }

    public static Collection<Authority> getAuthoritiesFromAPiKey(APIKey apiKey){
        List<Authority> authorities = new ArrayList<>();
        if (apiKey.getAuthorities() != null)
            authorities.addAll(apiKey.getAuthorities());
        if (apiKey.getRoles() != null)
            apiKey.getRoles().forEach(role -> authorities.addAll(role.getAuthorities()));
        return authorities;
    }

    public static void refreshPermissionOfAuthenticatedUser() {
        SecurityContextHolder.getContext().setAuthentication(
            getNewUserAuthenticationToken(
                BeanUtil.getBeanOfClass(UserService.class).getUser(getAuthenticatedUser().getId()),
                getAuthentication().getName()));
    }

    public static Authentication getNewUserAuthenticationToken(User user, String name){
        return new UsernamePasswordAuthenticationToken(user, name, SecurityUtil.getAuthoritiesFromUser(user));
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
