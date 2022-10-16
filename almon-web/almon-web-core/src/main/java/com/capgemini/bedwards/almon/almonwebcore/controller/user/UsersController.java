package com.capgemini.bedwards.almon.almonwebcore.controller.user;

import com.capgemini.bedwards.almon.almoncore.services.user.UserService;
import com.capgemini.bedwards.almon.almondatastore.models.auth.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/web/users")
@Slf4j
@PreAuthorize("isAuthenticated()")
public class UsersController {
    @Autowired
    UserService userService;

    @GetMapping()
    @PreAuthorize("hasAuthority('VIEW_ALL_USERS')")
    public String getAllUsers(@RequestParam(defaultValue = "1") int pageNumber, @RequestParam(defaultValue = "25") int pageSize, Model model) {
        Page<User> page = userService.findPaginated(pageNumber, pageSize);
        List<User> listUsers = page.getContent();
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("listUsers", listUsers);
        model.addAttribute("pageSize", pageSize);
        return "/users/users";
    }
}
