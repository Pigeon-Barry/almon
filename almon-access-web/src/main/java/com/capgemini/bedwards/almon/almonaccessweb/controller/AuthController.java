package com.capgemini.bedwards.almon.almonaccessweb.controller;

import com.capgemini.bedwards.almon.almonaccessweb.model.auth.Login;
import com.capgemini.bedwards.almon.almonaccessweb.model.auth.Register;
import com.capgemini.bedwards.almon.almonaccessweb.model.util.ScreenAlert;
import com.capgemini.bedwards.almon.almoncore.intergrations.web.WebController;
import com.capgemini.bedwards.almon.almoncore.security.AlmonAuthenticationProvider;
import com.capgemini.bedwards.almon.almoncore.services.auth.AuthService;
import com.capgemini.bedwards.almon.almoncore.util.SecurityUtil;
import com.capgemini.bedwards.almon.almondatastore.models.auth.User;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/web/auth")
@Slf4j
public class AuthController  extends WebController {

    @Autowired
    AuthService authService;

    @Autowired
    AlmonAuthenticationProvider authenticationProvider;

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
            return "redirect:/web/auth/pendingApproval";
        if (!user.isEnabled())
            return "redirect:/web/auth/accountDisabled";
        return "redirect:/web/home";
    }


    @GetMapping("/login")
    public String getLoginForm(Login login) {
        return "/auth/login";
    }

    @GetMapping("/pendingApproval")
    public String getPendingApprovalPage() {
        return "/auth/pendingApproval";
    }

    @GetMapping("/accountDisabled")
    public String getAccountDisabledPage() {
        return "/auth/accountDisabled";
    }

    @PostMapping("/login")
    public String login(@Valid Login login, Errors errors, Model model) {
        if (errors.hasErrors())
            return "/auth/login";
        try {
            doAutoLogin(login.getEmail(), login.getPassword());
            User user = SecurityUtil.getAuthenticatedUser();
            if (user.getApprovedBy() == null) {
                return "redirect:/web/auth/pendingApproval";
            }
            if (!user.isEnabled())
                return "redirect:/web/auth/accountDisabled";

            ScreenAlert screenAlert = new ScreenAlert("Welcome " + user.getFirstName(), ScreenAlert.Type.SUCCESS);
            model.addAttribute("screenAlerts", screenAlert);
            return "redirect:/web/home";
        } catch (AuthenticationException ignored) {
            ScreenAlert screenAlert = new ScreenAlert("Invalid Credentials", ScreenAlert.Type.ERROR);
            model.addAttribute("screenAlerts", screenAlert);
            return "/auth/login";
        }
    }

    @GetMapping(value = "/logout")
    public String logoutPage(Model model, HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityUtil.getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
            ScreenAlert screenAlert = new ScreenAlert("Successfully Logged out", ScreenAlert.Type.SUCCESS);
            model.addAttribute("screenAlerts", screenAlert);
        }
        return "redirect:/web/auth/login";
    }


    private void doAutoLogin(String username, String password) {
        try {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
            Authentication authentication = this.authenticationProvider.authenticate(token);
            log.debug("Logging in with [{}]", authentication.getPrincipal());
            SecurityUtil.setAuthentication(authentication);
        } catch (Exception e) {
            SecurityUtil.clearAuthentication();
            log.error("Failure in autoLogin", e);
        }
    }
}
