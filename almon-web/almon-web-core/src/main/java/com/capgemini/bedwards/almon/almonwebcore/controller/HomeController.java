package com.capgemini.bedwards.almon.almonwebcore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Controller
public class HomeController {
    @GetMapping({"home", "/"})
    public String home(Map<String, Object> model) {
        model.put("message", "Hello Ben");
        return "home";
    }
}
