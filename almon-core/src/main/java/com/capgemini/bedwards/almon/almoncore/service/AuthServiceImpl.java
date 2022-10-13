package com.capgemini.bedwards.almon.almoncore.service;

import com.capgemini.bedwards.almon.almondatastore.models.auth.Authority;
import com.capgemini.bedwards.almon.almondatastore.models.auth.User;
import com.capgemini.bedwards.almon.almondatastore.repository.auth.AuthorityRepository;
import com.capgemini.bedwards.almon.almondatastore.repository.auth.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    @Autowired
    @Lazy
    AuthenticationProvider authenticationProvider;
    @Autowired
    UserRepository userRepository;
    @Autowired
    AuthorityRepository authorityRepository;
    @Autowired
    @Lazy
    PasswordEncoder passwordEncoder;

    @Value("${almon.web.rootAccount}")
    private String rootAccount;

    @Override
    public User getAuthenticatedUser(String email, String password) {
        User user = userRepository.findUserByEmail(email);
        if (user != null && passwordEncoder.matches(password, user.getPassword()))
            return user;
        return null;
    }

    @Override
    public void authenticate(String email, String password) throws AuthenticationException {
        SecurityContextHolder.getContext().setAuthentication(authenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(email, password)));
    }

    @Override
    public User register(String email, String firstname, String lastname, String password) {
        if (!checkUserExists(email)) {
            User user = userRepository.saveAndFlush(
                    User.builder()
                            .email(email)
                            .firstName(firstname)
                            .lastName(lastname)
                            .password(passwordEncoder.encode(password))
                            .build());

            if (email.equalsIgnoreCase(rootAccount)) {
                log.info("ID: " + user.getId());
                user.setApprovedBy(user);
                userRepository.saveAndFlush(user);
//                authorityRepository.saveAndFlush(new Authority(new Authority.AuthorityId(user, "ROLE_ADMIN")));
//                authorityRepository.saveAndFlush(new Authority(new Authority.AuthorityId(user, "ROLE_USER")));
            }
            authenticate(email, password);
            return user;
        }
        throw new BadCredentialsException("Invalid Credentials");
    }


    @Override
    public boolean checkUserExists(String email) {
        return userRepository.existsByEmail(email);
    }
}
