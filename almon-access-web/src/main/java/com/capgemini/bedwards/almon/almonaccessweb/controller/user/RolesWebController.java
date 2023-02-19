package com.capgemini.bedwards.almon.almonaccessweb.controller.user;


import com.capgemini.bedwards.almon.almoncore.intergrations.web.WebController;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/web/user/{userId}/roles")
@Slf4j
@PreAuthorize("hasAuthority('ASSIGN_ROLES')")
public class RolesWebController extends WebController {
    private final UserService USER_SERVICE;
    private final RoleService ROLE_SERVICE;

    @Autowired
    public RolesWebController(UserService userService, RoleService roleService) {
        this.ROLE_SERVICE = roleService;
        this.USER_SERVICE = userService;
    }

    @PutMapping
    public ResponseEntity<String> updateRoles(@PathVariable(name = "userId") UUID userId,
                                              @RequestBody Map<String, UpdateType> roles) {
        User user = this.USER_SERVICE.getUserById(userId);
        this.ROLE_SERVICE.updateRoles(user, roles);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
