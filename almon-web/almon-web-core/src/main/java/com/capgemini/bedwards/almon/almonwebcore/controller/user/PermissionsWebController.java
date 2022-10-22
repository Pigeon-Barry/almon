package com.capgemini.bedwards.almon.almonwebcore.controller.user;


import com.capgemini.bedwards.almon.almoncore.service.AuthorityService;
import com.capgemini.bedwards.almon.almoncore.services.user.RoleService;
import com.capgemini.bedwards.almon.almoncore.services.user.UserService;
import com.capgemini.bedwards.almon.almondatastore.models.auth.UpdateType;
import com.capgemini.bedwards.almon.almondatastore.models.auth.User;
import com.capgemini.bedwards.almon.almonwebcore.controller.WebController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/web/user/{userId}/permissions")
@Slf4j
@PreAuthorize("hasAuthority('ASSIGN_PERMISSIONS')")
public class PermissionsWebController extends WebController {
    @Autowired
    UserService userService;
    @Autowired
    AuthorityService authorityService;

    @PutMapping
    public ResponseEntity<String> updateAuthorities(@PathVariable(name = "userId") UUID userId,
                                              @RequestBody Map<String, UpdateType> authorities,
                                              Model model) {
        User user = userService.getUserById(userId);
        authorityService.updateAuthorities(user, authorities);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
