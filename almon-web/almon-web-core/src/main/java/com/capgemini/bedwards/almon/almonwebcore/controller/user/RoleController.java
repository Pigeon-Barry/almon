package com.capgemini.bedwards.almon.almonwebcore.controller.user;


import com.capgemini.bedwards.almon.almoncore.services.user.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Controller
@RequestMapping("/web/user/{userId}/roles/{roleName}")
@Slf4j
@PreAuthorize("hasAuthority('ASSIGN_ROLES')")
public class RoleController {

    @Autowired
    RoleService roleService;

    @DeleteMapping()
    public ResponseEntity<String> deleteRole(@PathVariable(name = "userId") UUID userId, @PathVariable(name = "roleName") String roleName, Model model) {
        roleService.removeRole(userId,roleName);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

}
