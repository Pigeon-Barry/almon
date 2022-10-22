package com.capgemini.bedwards.almon.almonmonitoringapi.controllers.admin;

import com.capgemini.bedwards.almon.almonalertingcore.APIAlertTypeService;
import com.capgemini.bedwards.almon.almoncore.exceptions.NotFoundException;
import com.capgemini.bedwards.almon.almondatastore.models.alert.APIAlertType;
import com.capgemini.bedwards.almon.almondatastore.models.alert.Alert;
import com.capgemini.bedwards.almon.almonmonitoringapi.models.alerts.APIAlertTypeRequestBody;
import com.capgemini.bedwards.almon.almonmonitoringapi.error.ErrorResponse;
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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@OpenAPIDefinition(
        info = @Info(title = "ALMON - Alert Types",
                version = "1.0.0")
)
@RestController
@RequestMapping("${almon.api.prefix}/alertType")
public class AlertTypeController {

    @Autowired
    APIAlertTypeService apiAlertTypeService;

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
    @PostMapping(value = "/api")
    public ResponseEntity<APIAlertType> createNewAPIAlertType(
            @Valid @RequestBody @NotNull APIAlertTypeRequestBody apiAlertTypeRequestBody) throws NotFoundException {
        return new ResponseEntity<>(
                this.apiAlertTypeService.saveAlertType(
                        apiAlertTypeRequestBody.getName(),
                        apiAlertTypeRequestBody.getDescription(),
                        apiAlertTypeRequestBody.getUrl(),
                        apiAlertTypeRequestBody.getExpectedStatus()),
        HttpStatus.CREATED);
    }

}
