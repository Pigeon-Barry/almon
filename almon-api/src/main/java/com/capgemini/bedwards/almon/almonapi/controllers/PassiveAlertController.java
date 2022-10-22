package com.capgemini.bedwards.almon.almonapi.controllers;

import com.capgemini.bedwards.almon.almonalertingcore.AlertService;
import com.capgemini.bedwards.almon.almoncore.exceptions.NotFoundException;
import com.capgemini.bedwards.almon.almondatastore.models.alert.Alert;
import com.capgemini.bedwards.almon.almonapi.models.alerts.AlertRequestBody;
import com.capgemini.bedwards.almon.almonapi.models.alerts.AlertResponseBody;
import com.capgemini.bedwards.almon.almonapi.error.ErrorResponse;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;


@OpenAPIDefinition(
        info = @Info(title = "ALMON - Passive Alerts",
                version = "1.0.0")
)
@RestController
@RequestMapping("/api/alert")
public class PassiveAlertController extends APIController{

    @Autowired
    AlertService alertService;

    @Operation(
            summary = "POST - new passive alert",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Alert successfully registered on the system",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Alert.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid payload",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))),
                    @ApiResponse(responseCode = "503", description = "Downstream error",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))),
            })
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<AlertResponseBody> triggerAlert(
            @Valid @RequestBody @NotNull AlertRequestBody alertRequestBody) throws NotFoundException {
        return new ResponseEntity<>(
                AlertResponseBody.from(this.alertService.saveAlert(alertRequestBody.getAlertTypeId(), alertRequestBody.getMessage(), alertRequestBody.getTime())),
                HttpStatus.CREATED);
    }
}
