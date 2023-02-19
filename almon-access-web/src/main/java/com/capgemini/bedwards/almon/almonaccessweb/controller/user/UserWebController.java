package com.capgemini.bedwards.almon.almonaccessweb.controller.user;

import com.capgemini.bedwards.almon.almoncore.intergrations.web.WebController;
import com.capgemini.bedwards.almon.almoncore.services.auth.AuthorityService;
import com.capgemini.bedwards.almon.almoncore.services.notification.WebNotificationService;
import com.capgemini.bedwards.almon.almoncore.services.user.RoleService;
import com.capgemini.bedwards.almon.almoncore.services.user.UserService;
import com.capgemini.bedwards.almon.almoncore.util.SecurityUtil;
import com.capgemini.bedwards.almon.almondatastore.models.auth.Authority;
import com.capgemini.bedwards.almon.almondatastore.models.auth.Role;
import com.capgemini.bedwards.almon.almondatastore.models.auth.User;
import com.capgemini.bedwards.almon.almondatastore.models.notification.WebNotification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/web/user/{userId}")
@Slf4j
@PreAuthorize("isAuthenticated()")
public class UserWebController extends WebController {

    private final UserService USER_SERVICE;

    private final AuthorityService AUTHORITY_SERVICE;

    private final RoleService ROLE_SERVICE;

    private final WebNotificationService WEB_NOTIFICATION_SERVICE;

    @Autowired
    public UserWebController(UserService userService, AuthorityService authorityService, RoleService roleService, WebNotificationService webNotificationService) {
        this.USER_SERVICE = userService;
        this.AUTHORITY_SERVICE = authorityService;
        this.ROLE_SERVICE = roleService;
        this.WEB_NOTIFICATION_SERVICE = webNotificationService;
    }

    @GetMapping()

    @PreAuthorize("hasAuthority('VIEW_ALL_USERS') || #user.id == authentication.principal.id")
    public String getUserList(@PathVariable(name = "userId") User user,
        @RequestParam(defaultValue = "1") int notificationPageNumber,
        @RequestParam(defaultValue = "25") int notificationPageSize,
        Model model) {
        List<Role> roles = this.ROLE_SERVICE.getAllRoles();
        List<Authority> authorities = this.AUTHORITY_SERVICE.getAllAuthorities();
        model.addAttribute("roles", roles);
        model.addAttribute("pageUser", user);
        model.addAttribute("authorities", authorities);

        Page<WebNotification> page = this.WEB_NOTIFICATION_SERVICE.getNotifications(user,
                notificationPageNumber, notificationPageSize);

        model.addAttribute("currentPage", notificationPageNumber);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("webNotifications", page.getContent());
        model.addAttribute("pageSize", notificationPageSize);
        return "users/user";
    }

    @PutMapping("/enable")
    @PreAuthorize("hasAuthority('ENABLE_DISABLE_ACCOUNTS')")
    public ResponseEntity<String> enableAccount(@PathVariable(name = "userId") User user) {
        this.USER_SERVICE.enableAccount(SecurityUtil.getAuthenticatedUser(), user);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PutMapping("/disable")
    @PreAuthorize("hasAuthority('ENABLE_DISABLE_ACCOUNTS')")
    public ResponseEntity<String> disableAccount(@PathVariable(name = "userId") User user) {
        this.USER_SERVICE.disableAccount(SecurityUtil.getAuthenticatedUser(), user);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
