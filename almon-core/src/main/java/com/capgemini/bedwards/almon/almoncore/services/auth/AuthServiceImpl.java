package com.capgemini.bedwards.almon.almoncore.services.auth;

import com.capgemini.bedwards.almon.almondatastore.models.auth.Role;
import com.capgemini.bedwards.almon.almondatastore.models.auth.User;
import com.capgemini.bedwards.almon.almondatastore.repository.auth.RoleRepository;
import com.capgemini.bedwards.almon.almondatastore.repository.auth.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository USER_REPOSITORY;

    private final RoleRepository ROLE_REPOSITORY;

    @Autowired
    @Lazy
    PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.USER_REPOSITORY = userRepository;
        this.ROLE_REPOSITORY = roleRepository;
    }

    @Value("${almon.web.root-account}")
    private String rootAccount;

    @Override
    public User getAuthenticatedUser(String email, String password) {
        Optional<User> userOptional = USER_REPOSITORY.findUserByEmail(email);
        if (userOptional.isPresent() && passwordEncoder.matches(password, userOptional.get().getPassword()))
            return userOptional.get();
        return null;
    }

    @Override
    @Transactional
    public User register(String email, String firstname, String lastname, String password) {
        if (!checkUserExists(email)) {
            Set<Role> roles = new HashSet<>();
            roles.add(ROLE_REPOSITORY.findById("USER").orElse(null));
            boolean enabled = false;
            if (email.equalsIgnoreCase(rootAccount)) {
                roles.add(ROLE_REPOSITORY.findById("ADMIN").orElse(null));
                enabled = true;
            }
            User user = USER_REPOSITORY.saveAndFlush(
                    User.builder()
                            .email(email)
                            .enabled(enabled)
                            .firstName(firstname)
                            .lastName(lastname)
                            .password(passwordEncoder.encode(password))
                            .roles(roles)
                            .build());
            if (email.equalsIgnoreCase(rootAccount)) {
                user.setApprovedBy(user);
                user = USER_REPOSITORY.saveAndFlush(user);
            }
            return user;
        }
        throw new BadCredentialsException("User already exists with this email");
    }


    @Override
    public boolean checkUserExists(String email) {
        return USER_REPOSITORY.existsByEmail(email);
    }
}
