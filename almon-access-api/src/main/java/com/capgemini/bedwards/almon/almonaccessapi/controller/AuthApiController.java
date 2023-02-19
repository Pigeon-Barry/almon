package com.capgemini.bedwards.almon.almonaccessapi.controller;


import com.capgemini.bedwards.almon.almonaccessapi.models.auth.Register;
import com.capgemini.bedwards.almon.almoncore.intergrations.api.APIController;
import com.capgemini.bedwards.almon.almoncore.services.auth.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/api/auth")
@Slf4j
public class AuthApiController extends APIController {
    private final AuthService AUTH_SERVICE;

    @Autowired
    public AuthApiController(AuthService authService) {
        this.AUTH_SERVICE = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid Register register) {
        this.AUTH_SERVICE.register(register.getEmail(), register.getFirstName(), register.getLastName(), register.getPassword());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
