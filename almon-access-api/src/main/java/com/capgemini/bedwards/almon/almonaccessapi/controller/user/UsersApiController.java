package com.capgemini.bedwards.almon.almonaccessapi.controller.user;

import com.capgemini.bedwards.almon.almonaccessapi.models.UserList;
import com.capgemini.bedwards.almon.almoncore.intergrations.api.APIController;
import com.capgemini.bedwards.almon.almoncore.intergrations.api.error.ErrorResponse;
import com.capgemini.bedwards.almon.almoncore.intergrations.api.error.InternalServerErrorResponse;
import com.capgemini.bedwards.almon.almoncore.services.user.UserService;
import com.capgemini.bedwards.almon.almondatastore.models.auth.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api/users")
@Slf4j
@PreAuthorize("isAuthenticated()")
public class UsersApiController extends APIController {

    private final UserService USER_SERVICE;

    @Autowired
    public UsersApiController(UserService userService) {
        this.USER_SERVICE = userService;
    }

    @Operation(
            summary = "GET - Returns all the users on the system",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Users found matching the provided details",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserList.class))),
                    @ApiResponse(responseCode = "404", description = "Not Found",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = InternalServerErrorResponse.class)))),
                    @ApiResponse(responseCode = "503", description = "Downstream error",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))),
            })
    @GetMapping()
    @PreAuthorize("hasAuthority('VIEW_ALL_USERS')")
    public ResponseEntity<UserList> getAllUsers(@RequestParam(defaultValue = "1") int userPageNumber,
                                                @RequestParam(defaultValue = "25") int userPageSize,
                                                @RequestParam(required = false) String enabled) {
        Boolean enabledVal = enabled == null || enabled.equalsIgnoreCase("false") == enabled.equalsIgnoreCase("true") ? null : Boolean.parseBoolean(enabled);
        Page<User> page = this.USER_SERVICE.findPaginatedWithFilter(userPageNumber, userPageSize, enabledVal);
        UserList userList = new UserList();
        userList.addAll(page.getContent());
        return ResponseEntity.ok(userList);
    }
}
