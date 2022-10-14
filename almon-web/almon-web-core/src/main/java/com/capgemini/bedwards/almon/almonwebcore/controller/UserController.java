package com.capgemini.bedwards.almon.almonwebcore.controller;

import com.capgemini.bedwards.almon.almonwebcore.model.auth.Register;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/web/users")
public class UserController {

    @GetMapping("/")
    public String getUsersList() {
        return "/auth/register";
    }
}
