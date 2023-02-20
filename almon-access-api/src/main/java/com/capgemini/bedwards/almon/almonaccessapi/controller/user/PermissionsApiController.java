package com.capgemini.bedwards.almon.almonaccessapi.controller.user;


import com.capgemini.bedwards.almon.almonaccessapi.models.services.ServiceResponseBody;
import com.capgemini.bedwards.almon.almoncore.intergrations.api.APIController;
import com.capgemini.bedwards.almon.almoncore.intergrations.api.error.ErrorResponse;
import com.capgemini.bedwards.almon.almoncore.intergrations.api.error.InternalServerErrorResponse;
import com.capgemini.bedwards.almon.almoncore.services.auth.AuthorityService;
import com.capgemini.bedwards.almon.almoncore.services.user.UserService;
import com.capgemini.bedwards.almon.almondatastore.models.auth.UpdateType;
import com.capgemini.bedwards.almon.almondatastore.models.auth.User;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("/api/user/{userId}/permissions")
@Slf4j
@PreAuthorize("hasAuthority('ASSIGN_PERMISSIONS')")
public class PermissionsApiController extends APIController {

    private final UserService USER_SERVICE;
    private final AuthorityService AUTHORITY_SERVICE;

    @Autowired
    public PermissionsApiController(UserService userService, AuthorityService authorityService) {
        this.USER_SERVICE = userService;
        this.AUTHORITY_SERVICE = authorityService;
    }

    @Operation(
            summary = "PUT - Update user permissions",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Users permissions successfully updated",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ServiceResponseBody.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid payload",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = InternalServerErrorResponse.class)))),
                    @ApiResponse(responseCode = "503", description = "Downstream error",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))),
            })
    @PutMapping
    public ResponseEntity<String> updateAuthorities(@PathVariable(name = "userId") User user,
                                                    @RequestBody Map<String, UpdateType> authorities) {
        this.AUTHORITY_SERVICE.updateAuthorities(user, authorities);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
