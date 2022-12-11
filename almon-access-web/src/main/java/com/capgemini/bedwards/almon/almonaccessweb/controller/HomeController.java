package com.capgemini.bedwards.almon.almonaccessweb.controller;

import com.capgemini.bedwards.almon.almoncore.intergrations.web.WebController;
import java.util.Map;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/web")
@PreAuthorize("isAuthenticated()")
public class HomeController  extends WebController {
    @GetMapping("/home")
    public String home(Map<String, Object> model) {
        model.put("message", "Hello Ben");
        return "home";
    }

    @GetMapping
    public String root(Map<String, Object> model) {
        return home(model);
    }
}