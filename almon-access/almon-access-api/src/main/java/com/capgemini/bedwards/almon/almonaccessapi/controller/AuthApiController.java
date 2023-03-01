package com.capgemini.bedwards.almon.almonaccessapi.controller;


import com.capgemini.bedwards.almon.almonaccessapi.models.auth.Register;
import com.capgemini.bedwards.almon.almoncore.intergrations.api.APIController;
import com.capgemini.bedwards.almon.almoncore.intergrations.api.error.ErrorResponse;
import com.capgemini.bedwards.almon.almoncore.intergrations.api.error.InternalServerErrorResponse;
import com.capgemini.bedwards.almon.almoncore.services.auth.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @Operation(
            summary = "POST - Registers the user on the system",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User has successfully been registered",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
                    @ApiResponse(responseCode = "404", description = "Not Found",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = InternalServerErrorResponse.class)))),
                    @ApiResponse(responseCode = "503", description = "Downstream error",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))),
            })
    @PostMapping(value = "/register")
    public ResponseEntity<Void> register(@RequestBody @Valid Register register) {
        this.AUTH_SERVICE.register(register.getEmail(), register.getFirstName(), register.getLastName(), register.getPassword());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
