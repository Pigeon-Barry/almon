package com.capgemini.bedwards.almon.almonwebcore.controller.admin;

import com.capgemini.bedwards.almon.almoncore.services.UserService;
import com.capgemini.bedwards.almon.almondatastore.models.auth.User;
import com.capgemini.bedwards.almon.almondatastore.repository.auth.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/web/users")
public class AdminUsersController {

    @Autowired
    UserService userService;

    @GetMapping()
    public String getAllUsers(@RequestParam(defaultValue = "1") int pageNumber, @RequestParam(defaultValue = "25") int pageSize, Model model) {

        Page<User> page = userService.findPaginated(pageNumber, pageSize);
        List<User> listUsers = page.getContent();
//        for (int val = 0; val < 100; val++)
//            getUser();
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("listUsers", listUsers);
        model.addAttribute("pageSize", pageSize);
        return "/admin/users/users";
    }

    long val = 0L;

    @Autowired
    UserRepository userRepository;

    public User getUser() {
        User user = User.builder()
                .firstName("Firstname " + ++val)
                .lastName("lastname" + val)
                .email("email" + val)
                .password("password" + val)
                .build();
        userRepository.save(user);
        return user;
    }

}
