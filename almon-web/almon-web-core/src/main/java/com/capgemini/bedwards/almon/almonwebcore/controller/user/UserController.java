package com.capgemini.bedwards.almon.almonwebcore.controller.user;

import com.capgemini.bedwards.almon.almoncore.services.user.RoleService;
import com.capgemini.bedwards.almon.almoncore.services.user.UserService;
import com.capgemini.bedwards.almon.almondatastore.models.auth.Role;
import com.capgemini.bedwards.almon.almondatastore.models.auth.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/web/users/{userId}")
@Slf4j
@PreAuthorize("isAuthenticated()")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    RoleService roleService;

    @GetMapping()
    public String getUsersList(@PathVariable(name = "userId") UUID userId, Model model) {
        User user = userService.getUserById(userId);
        List<Role> roles = roleService.getAllRoles();
        model.addAttribute("user", user);
        model.addAttribute("roles", roles);
        return "users/user";
    }


    @PutMapping("/enable")
    @PreAuthorize("hasAuthority('DISABLE_ACCOUNTS')")
    public ResponseEntity<String> enableAccount(@PathVariable(name = "userId") UUID userId) {
        log.info("Enabling user: " + userId);
        userService.enableAccount(userId);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PutMapping("/disable")
    @PreAuthorize("hasAuthority('DISABLE_ACCOUNTS')")
    public ResponseEntity<String> disableAccount(@PathVariable(name = "userId") UUID userId, HttpServletRequest request) {
        log.info("Disabling user: " + userId);
        userService.disableAccount(userId);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
