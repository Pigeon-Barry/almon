package com.capgemini.bedwards.almon.almondatastore.repository.auth;

import com.capgemini.bedwards.almon.almondatastore.models.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    User findUserByEmail(String email);
    boolean existsByEmail(String email);
}
