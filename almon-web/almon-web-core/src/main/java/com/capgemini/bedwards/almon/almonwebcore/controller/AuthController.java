package com.capgemini.bedwards.almon.almonwebcore.controller;

import com.capgemini.bedwards.almon.almonwebcore.model.auth.Login;
import com.capgemini.bedwards.almon.almonwebcore.model.auth.Register;
import com.capgemini.bedwards.almon.almonwebcore.model.util.ScreenAlert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.jaas.SecurityContextLoginModule;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JdbcUserDetailsManager jdbcUserDetailsManager;
    @Autowired
    private PasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/register")
    public String getRegisterForm(Register register) {
        return "/auth/register";
    }

    @PostMapping("/register")
    public String register(@Valid Register register, Errors errors, Model model) {
        if (errors.hasErrors()) {
            return "/auth/register";
        }

        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        String encodedPassword = bCryptPasswordEncoder.encode(register.getPassword());

        org.springframework.security.core.userdetails.User user = new org.springframework.security.core.userdetails.User(register.getEmail(), encodedPassword, authorities);
        jdbcUserDetailsManager.createUser(user);

        ScreenAlert screenAlert = new ScreenAlert("Successfully Registered", ScreenAlert.Type.SUCCESS);
        model.addAttribute("screenAlerts", screenAlert);
        return "/home";
    }


    @GetMapping("/login")
    public String getLoginForm(Login login) {
        return "/auth/login";
    }

    @PostMapping("/login")
    public String login(@Valid Login register, Errors errors, Model model) {
        if (errors.hasErrors()) {
            return "/auth/login";
        }
        ScreenAlert screenAlert = new ScreenAlert("Welcome %NAME%", ScreenAlert.Type.SUCCESS);
        model.addAttribute("screenAlerts", screenAlert);
        return "/home";
    }

    @GetMapping(value = "/logout")
    public String logoutPage(Model model, HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
            ScreenAlert screenAlert = new ScreenAlert("Successfully Loged out", ScreenAlert.Type.SUCCESS);
            model.addAttribute("screenAlerts", screenAlert);
        }
        return "redirect:/auth/login";
    }
}
