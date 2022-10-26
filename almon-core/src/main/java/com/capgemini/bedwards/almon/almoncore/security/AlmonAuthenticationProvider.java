package com.capgemini.bedwards.almon.almoncore.security;

import com.capgemini.bedwards.almon.almoncore.services.auth.AuthService;
import com.capgemini.bedwards.almon.almondatastore.models.auth.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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

        List<GrantedAuthority> authorities = new ArrayList<>();
        if (user.isEnabled()) {
            if (user.getAuthorities() != null)
                authorities.addAll(user.getAuthorities());

            if (user.getRoles() != null)
                user.getRoles().forEach(role -> authorities.addAll(role.getAuthorities()));
        }
        return new UsernamePasswordAuthenticationToken(user, name, authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
