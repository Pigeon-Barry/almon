package com.capgemini.bedwards.almon.almonwebcore.controller.admin;

import com.capgemini.bedwards.almon.almoncore.services.UserService;
import com.capgemini.bedwards.almon.almoncore.validators.UserDoesNotExist;
import com.capgemini.bedwards.almon.almondatastore.models.auth.User;
import com.capgemini.bedwards.almon.almondatastore.repository.auth.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.PostRemove;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

@Controller
@Slf4j
@RequestMapping("/web/users/{userId}")
public class AdminUserController {

    @Autowired
    UserService userService;

    @PutMapping("/enable")
    public ResponseEntity<String> enableAccount(@PathVariable(name = "userId") UUID userId) {
        log.info("Enabling user: " + userId);
        userService.enableAccount(userId);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PutMapping("/disable")
    public ResponseEntity<String> disableAccount(@PathVariable(name = "userId") UUID userId, HttpServletRequest request) {
        log.info("Disabling user: " + userId);
        userService.disableAccount(userId);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    long val = 0L;

    @Autowired
    UserRepository userRepository;

    public User getUser() {
        User user = User.builder()
                .firstName("Firstname " + ++val)
                .lastName("lastname" + val)
                .email("email" + val)
                .password("password" + val)
                .build();
        userRepository.save(user);
        return user;
    }

}
