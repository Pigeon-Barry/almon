package com.capgemini.bedwards.almon.almonweb.controller.user;


import com.capgemini.bedwards.almon.almoncore.exceptions.NotFoundException;
import com.capgemini.bedwards.almon.almoncore.intergrations.web.WebController;
import com.capgemini.bedwards.almon.almoncore.services.user.RoleService;
import com.capgemini.bedwards.almon.almondatastore.models.auth.Role;
import com.capgemini.bedwards.almon.almondatastore.models.auth.User;
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

@Controller
@RequestMapping("/web/user/{userId}/roles/{roleName}")
@Slf4j
@PreAuthorize("hasAuthority('ASSIGN_ROLES')")
public class RoleWebController extends WebController {

    @Autowired
    RoleService roleService;

    @DeleteMapping()
    public ResponseEntity<String> deleteRole(
            @PathVariable(name = "userId") User user,
            @PathVariable(name = "roleName") Role role,
            Model model) {
        if (roleService.removeRole(user, role))
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        throw new NotFoundException("User does not have role " + role.getName());
    }

}
