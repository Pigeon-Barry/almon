package com.capgemini.bedwards.almon.almonwebcore.controller;

import com.capgemini.bedwards.almon.almoncore.service.AuthService;
import com.capgemini.bedwards.almon.almondatastore.models.auth.User;
import com.capgemini.bedwards.almon.almonwebcore.model.auth.Login;
import com.capgemini.bedwards.almon.almonwebcore.model.auth.Register;
import com.capgemini.bedwards.almon.almonwebcore.model.util.ScreenAlert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
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

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @GetMapping("/register")
    public String getRegisterForm(Register register) {
        return "/auth/register";
    }

    @PostMapping("/register")
    public String register(@Valid Register register, Errors errors, Model model) {
        if (errors.hasErrors()) {
            return "/auth/register";
        }
        User user = authService.register(register.getEmail(), register.getFirstName(), register.getLastName(), register.getPassword());
        if (user.getApprovedBy() == null)
            return "redirect:/auth/pendingApproval";
        return "/home";
    }


    @GetMapping("/login")
    public String getLoginForm(Login login) {
        return "/auth/login";
    }
    @GetMapping("/pendingApproval")
    public String getPendingApprovalPage() {
        return "/auth/pendingApproval";
    }

    @PostMapping("/login")
    public String login(@Valid Login login, Errors errors, Model model) {
        if (errors.hasErrors())
            return "/auth/login";
        try {
            authService.authenticate(login.getEmail(), login.getPassword());
            User user = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
            if (user.getApprovedBy() == null)
                return "redirect:/auth/pendingApproval";
            ScreenAlert screenAlert = new ScreenAlert("Welcome " + user.getFirstName(), ScreenAlert.Type.SUCCESS);
            model.addAttribute("screenAlerts", screenAlert);
            return "redirect:/home";
        } catch (AuthenticationException ignored) {
            ScreenAlert screenAlert = new ScreenAlert("Invalid Credentials", ScreenAlert.Type.ERROR);
            model.addAttribute("screenAlerts", screenAlert);
            return "/auth/login";
        }
    }

    @GetMapping(value = "/logout")
    public String logoutPage(Model model, HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
            ScreenAlert screenAlert = new ScreenAlert("Successfully Logged out", ScreenAlert.Type.SUCCESS);
            model.addAttribute("screenAlerts", screenAlert);
        }
        return "redirect:/auth/login";
    }
}
