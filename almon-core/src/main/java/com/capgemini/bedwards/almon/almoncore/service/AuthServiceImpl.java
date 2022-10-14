package com.capgemini.bedwards.almon.almoncore.service;

import com.capgemini.bedwards.almon.almondatastore.models.auth.User;
import com.capgemini.bedwards.almon.almondatastore.repository.auth.AuthorityRepository;
import com.capgemini.bedwards.almon.almondatastore.repository.auth.RoleRepository;
import com.capgemini.bedwards.almon.almondatastore.repository.auth.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    AuthorityRepository authorityRepository;
    @Autowired
    RoleRepository roleRepository;
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
                user.setApprovedBy(user);
                user.setRoles(new HashSet<>());
                user.getRoles().add(roleRepository.findById("ADMIN").orElse(null));
                user.getRoles().add(roleRepository.findById("USER").orElse(null));
                userRepository.saveAndFlush(user);

            }
            return user;
        }
        throw new BadCredentialsException("Invalid Credentials");
    }


    @Override
    public boolean checkUserExists(String email) {
        return userRepository.existsByEmail(email);
    }
}
