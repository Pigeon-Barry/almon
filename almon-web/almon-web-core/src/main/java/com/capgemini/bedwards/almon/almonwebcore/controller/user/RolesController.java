package com.capgemini.bedwards.almon.almonwebcore.controller.user;


import com.capgemini.bedwards.almon.almoncore.services.user.RoleService;
import com.capgemini.bedwards.almon.almoncore.services.user.UserService;
import com.capgemini.bedwards.almon.almondatastore.models.auth.UpdateType;
import com.capgemini.bedwards.almon.almondatastore.models.auth.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/web/user/{userId}/roles")
@Slf4j
@PreAuthorize("hasAuthority('ASSIGN_ROLES')")
public class RolesController {
    @Autowired
    UserService userService;
    @Autowired
    RoleService roleService;

    @PutMapping
    public ResponseEntity<String> updateRoles(@PathVariable(name = "userId") UUID userId,
                                              @RequestBody Map<String, UpdateType> roles,
                                              Model model) {
        User user = userService.getUserById(userId);
        roleService.updateRoles(user, roles);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
