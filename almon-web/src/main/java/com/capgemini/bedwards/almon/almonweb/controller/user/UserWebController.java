package com.capgemini.bedwards.almon.almonweb.controller.user;

import com.capgemini.bedwards.almon.almoncore.intergrations.web.WebController;
import com.capgemini.bedwards.almon.almoncore.services.auth.AuthorityService;
import com.capgemini.bedwards.almon.almoncore.services.notification.WebNotificationService;
import com.capgemini.bedwards.almon.almoncore.services.user.RoleService;
import com.capgemini.bedwards.almon.almoncore.services.user.UserService;
import com.capgemini.bedwards.almon.almoncore.util.SecurityUtil;
import com.capgemini.bedwards.almon.almondatastore.models.auth.Authority;
import com.capgemini.bedwards.almon.almondatastore.models.auth.Role;
import com.capgemini.bedwards.almon.almondatastore.models.auth.User;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
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

@Controller
@RequestMapping("/web/user/{userId}")
@Slf4j
@PreAuthorize("isAuthenticated()")
public class UserWebController extends WebController {

    @Autowired
    UserService userService;
    @Autowired
    AuthorityService authorityService;
    @Autowired
    RoleService roleService;
    @Autowired
    WebNotificationService webNotificationService;

    @GetMapping()

    @PreAuthorize("hasAuthority('VIEW_ALL_USERS') || #user.id == authentication.principal.id")
    public String getUserList(@PathVariable(name = "userId") User user, Model model) {
        List<Role> roles = roleService.getAllRoles();
        List<Authority> authorities = authorityService.getAllAuthorities();
        model.addAttribute("roles", roles);
        model.addAttribute("pageUser", user);
        model.addAttribute("authorities", authorities);
        model.addAttribute("webNotifications", webNotificationService.getNotifications(user));
        return "users/user";
    }

    @PutMapping("/enable")
    @PreAuthorize("hasAuthority('ENABLE_DISABLE_ACCOUNTS')")
    public ResponseEntity<String> enableAccount(@PathVariable(name = "userId") User user) {
        userService.enableAccount(SecurityUtil.getAuthenticatedUser(), user);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PutMapping("/disable")
    @PreAuthorize("hasAuthority('ENABLE_DISABLE_ACCOUNTS')")
    public ResponseEntity<String> disableAccount(@PathVariable(name = "userId") User user, HttpServletRequest request) {
        userService.disableAccount(SecurityUtil.getAuthenticatedUser(),user);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
