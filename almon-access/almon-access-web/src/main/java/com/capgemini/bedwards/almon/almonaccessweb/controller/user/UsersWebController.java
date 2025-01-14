package com.capgemini.bedwards.almon.almonaccessweb.controller.user;

import com.capgemini.bedwards.almon.almoncore.intergrations.web.WebController;
import com.capgemini.bedwards.almon.almoncore.services.user.UserService;
import com.capgemini.bedwards.almon.almondatastore.models.auth.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/web/users")
@Slf4j
@PreAuthorize("isAuthenticated()")
public class UsersWebController extends WebController {
    private final UserService USER_SERVICE;

    @Autowired
    public UsersWebController(UserService userService) {
        this.USER_SERVICE = userService;
    }

    @GetMapping()
    @PreAuthorize("hasAuthority('VIEW_ALL_USERS')")
    public String getAllUsers(@RequestParam(defaultValue = "1") int userPageNumber,
                              @RequestParam(defaultValue = "25") int userPageSize,
                              @RequestParam(required = false) String enabled,
                              Model model) {
        Boolean enabledVal = enabled == null || enabled.equalsIgnoreCase("false") == enabled.equalsIgnoreCase("true") ? null : Boolean.parseBoolean(enabled);
        Page<User> page = this.USER_SERVICE.findPaginatedWithFilter(userPageNumber, userPageSize, enabledVal);
        List<User> listUsers = page.getContent();


        model.addAttribute("currentPage", userPageNumber);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("listUsers", listUsers);
        model.addAttribute("pageSize", userPageSize);
        return "/users/users";
    }
}
