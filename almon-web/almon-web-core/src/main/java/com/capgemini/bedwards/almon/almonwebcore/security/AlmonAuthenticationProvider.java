package com.capgemini.bedwards.almon.almonwebcore.security;

import com.capgemini.bedwards.almon.almoncore.service.AuthService;
import com.capgemini.bedwards.almon.almondatastore.models.auth.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Slf4j
public class AlmonAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    AuthService authService;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String name = authentication.getName();
        String password = authentication.getCredentials().toString();
        User user = authService.getAuthenticatedUser(authentication.getName(), password);
        if (user == null)
            throw new BadCredentialsException("Invalid Credentials");

        Set<String> authoritiesString = new HashSet<>();
        if (user.getAuthorities() != null)
            user.getAuthorities().forEach(authority -> authoritiesString.add(authority.getAuthority()));

        if (user.getRoles() != null)
            user.getRoles().forEach(role -> role.getAuthorities().forEach(authority -> authoritiesString.add(authority.getAuthority())));
        List<GrantedAuthority> authorities = new ArrayList<>();
        authoritiesString.forEach(authority -> authorities.add(new SimpleGrantedAuthority(authority)));

        return new UsernamePasswordAuthenticationToken(user, name, authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
