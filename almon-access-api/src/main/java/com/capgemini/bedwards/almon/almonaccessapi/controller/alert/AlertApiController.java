package com.capgemini.bedwards.almon.almonaccessapi.controller.alert;


import com.capgemini.bedwards.almon.almoncore.intergrations.api.APIController;
import com.capgemini.bedwards.almon.almoncore.intergrations.api.error.ErrorResponse;
import com.capgemini.bedwards.almon.almoncore.intergrations.api.error.InternalServerErrorResponse;
import com.capgemini.bedwards.almon.almondatastore.models.alert.Alert;
import com.capgemini.bedwards.almon.almondatastore.models.monitor.Monitor;
import com.capgemini.bedwards.almon.almondatastore.models.service.Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/api/service/{serviceId}/monitor/{monitorId}/alert/{alertId}")
@Slf4j
@PreAuthorize("isAuthenticated()")
public class AlertApiController extends APIController {

    @Operation(
            summary = "GET - alert details in HTML format",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully returned the alert details",
                            content = @Content(mediaType = MediaType.APPLICATION_XML_VALUE, schema = @Schema(implementation = Alert.class))),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = InternalServerErrorResponse.class)))),
                    @ApiResponse(responseCode = "404", description = "Not Found",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))),
                    @ApiResponse(responseCode = "503", description = "Downstream error",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))),
            })
    @GetMapping(value = "/html", produces = MediaType.TEXT_HTML_VALUE)
    @PreAuthorize("hasAuthority('VIEW_ALL_MONITORS') || hasAuthority('SERVICE_' + #service.id + '_MONITOR_' + #monitor.id + '_CAN_VIEW')")
    public ResponseEntity<String> viewAlertDetailsAsHtml(
            @Parameter(in = ParameterIn.PATH, name = "serviceId", schema = @Schema(implementation = String.class), required = true) @Valid @PathVariable(name = "serviceId") Service service,
            @Parameter(in = ParameterIn.PATH, name = "monitorId", schema = @Schema(implementation = String.class), required = true) @Valid @PathVariable(name = "monitorId") Monitor monitor,
            @Parameter(in = ParameterIn.PATH, name = "alertId", schema = @Schema(implementation = String.class), required = true) @Valid @PathVariable(name = "alertId") Alert<?> alert) {
        return ResponseEntity.ok(alert.getHTMLMessage());
    }

    @Operation(
            summary = "GET - alert details in JSON format",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully returned the alert details",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Alert.class))),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = InternalServerErrorResponse.class)))),
                    @ApiResponse(responseCode = "404", description = "Not Found",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))),
                    @ApiResponse(responseCode = "503", description = "Downstream error",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))),
            })
    @GetMapping("/json")
    @PreAuthorize("hasAuthority('VIEW_ALL_MONITORS') || hasAuthority('SERVICE_' + #service.id + '_MONITOR_' + #monitor.id + '_CAN_VIEW')")
    public ResponseEntity<Alert<?>> viewAlertDetailsAsJson(
            @Parameter(in = ParameterIn.PATH, name = "serviceId", schema = @Schema(implementation = String.class), required = true) @Valid @PathVariable(name = "serviceId") Service service,
            @Parameter(in = ParameterIn.PATH, name = "monitorId", schema = @Schema(implementation = String.class), required = true) @Valid @PathVariable(name = "monitorId") Monitor monitor,
            @Parameter(in = ParameterIn.PATH, name = "alertId", schema = @Schema(implementation = String.class), required = true) @Valid @PathVariable(name = "alertId") Alert<?> alert) {
        return ResponseEntity.ok(alert);
    }
}
