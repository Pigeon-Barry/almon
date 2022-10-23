package com.capgemini.bedwards.almon.almonapi.controllers;

import com.capgemini.bedwards.almon.almoncore.intergrations.api.error.BadRequestResponse;
import com.capgemini.bedwards.almon.almonapi.models.services.ServiceRequestBody;
import com.capgemini.bedwards.almon.almonapi.models.services.ServiceResponseBody;
import com.capgemini.bedwards.almon.almoncore.intergrations.api.APIController;
import com.capgemini.bedwards.almon.almoncore.services.service.ServiceService;
import com.capgemini.bedwards.almon.almondatastore.models.service.Service;
import com.capgemini.bedwards.almon.almoncore.intergrations.api.error.ErrorResponse;
import com.capgemini.bedwards.almon.almoncore.intergrations.api.error.InternalServerErrorResponse;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@OpenAPIDefinition(
        info = @Info(title = "ALMON - Services",
                version = "1.0.0")
)
@RestController
@RequestMapping("/api/services")
@Slf4j
public class ServicesAPIController extends APIController {

    @Autowired
    ServiceService serviceService;

    @Operation(
            summary = "POST - new passive alert",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Service successfully created on the system",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ServiceResponseBody.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid payload",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = BadRequestResponse.class)))),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = InternalServerErrorResponse.class)))),
                    @ApiResponse(responseCode = "503", description = "Downstream error",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))),
            })
    @PostMapping("/create")
    @PreAuthorize("hasAuthority('CREATE_SERVICE')")
    public ResponseEntity<ServiceResponseBody> create(@RequestBody @Valid ServiceRequestBody serviceRequestBody) {
        Service service = serviceService.createService(serviceRequestBody.getKey(), serviceRequestBody.getName(), serviceRequestBody.getDescription());
        return new ResponseEntity<>(ServiceResponseBody.from(service), HttpStatus.CREATED);
    }
}
