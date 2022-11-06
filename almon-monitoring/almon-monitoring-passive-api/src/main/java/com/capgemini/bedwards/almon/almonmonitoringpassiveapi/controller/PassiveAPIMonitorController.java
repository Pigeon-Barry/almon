package com.capgemini.bedwards.almon.almonmonitoringpassiveapi.controller;

import com.capgemini.bedwards.almon.almoncore.intergrations.api.APIController;
import com.capgemini.bedwards.almon.almoncore.intergrations.api.error.BadRequestResponse;
import com.capgemini.bedwards.almon.almoncore.intergrations.api.error.ErrorResponse;
import com.capgemini.bedwards.almon.almoncore.intergrations.api.error.InternalServerErrorResponse;
import com.capgemini.bedwards.almon.almondatastore.models.monitor.Monitor;
import com.capgemini.bedwards.almon.almondatastore.models.service.Service;
import com.capgemini.bedwards.almon.almonmonitoringpassiveapi.PassiveAPIMonitorAdapter;
import com.capgemini.bedwards.almon.almonmonitoringpassiveapi.models.PassiveAPIMonitor;
import com.capgemini.bedwards.almon.almonmonitoringpassiveapi.models.TriggerPassiveAPIMonitorRequestBody;
import com.capgemini.bedwards.almon.almonmonitoringpassiveapi.models.TriggerPassiveAPIMonitorResponseBody;
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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/service/{serviceId}/monitor/{monitorId}")
@Slf4j
public class PassiveAPIMonitorController extends APIController {

    private final PassiveAPIMonitorAdapter PASSIVE_API_ADAPTER;


    @Autowired
    public PassiveAPIMonitorController(PassiveAPIMonitorAdapter passiveAPIMonitorAdapter) {
        this.PASSIVE_API_ADAPTER = passiveAPIMonitorAdapter;
    }

    @Operation(
            summary = "POST - new passive alert",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Alert successfully created on the system",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = TriggerPassiveAPIMonitorResponseBody.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid payload",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = BadRequestResponse.class)))),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = InternalServerErrorResponse.class)))),
                    @ApiResponse(responseCode = "503", description = "Downstream error",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))),
            })
    @PostMapping("/alert")
    @PreAuthorize("hasAuthority('RUN_MONITORS') || hasAuthority('SERVICE_' + #service.id + '_MONITOR_' + #monitor.id + '_CAN_RUN')")
    public ResponseEntity<? extends Object> triggerAlert(
            @Valid @PathVariable(name = "serviceId") Service service,
            @Valid @PathVariable(name = "monitorId") Monitor monitor,
            @RequestBody TriggerPassiveAPIMonitorRequestBody body
    ) {
        if (!(monitor instanceof PassiveAPIMonitor))
            return new ResponseEntity<>(new BadRequestResponse("monitorId", "MonitorId is of type: '" + monitor.getMonitorType() + "' but expected type: '" + PassiveAPIMonitorAdapter.ID + "'"), HttpStatus.BAD_REQUEST);
        return PASSIVE_API_ADAPTER.trigger((PassiveAPIMonitor) monitor, body);
    }
}
