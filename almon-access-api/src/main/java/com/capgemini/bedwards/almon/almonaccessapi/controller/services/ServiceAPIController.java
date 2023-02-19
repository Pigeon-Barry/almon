package com.capgemini.bedwards.almon.almonaccessapi.controller.services;

import com.capgemini.bedwards.almon.almonaccessapi.models.services.ServiceUpdateRequestBody;
import com.capgemini.bedwards.almon.almonaccessapi.models.services.UpdateServiceUserRolesRequestBody;
import com.capgemini.bedwards.almon.almoncore.exceptions.NotFoundException;
import com.capgemini.bedwards.almon.almoncore.intergrations.api.APIController;
import com.capgemini.bedwards.almon.almoncore.services.service.ServiceService;
import com.capgemini.bedwards.almon.almoncore.services.user.UserService;
import com.capgemini.bedwards.almon.almondatastore.models.auth.User;
import com.capgemini.bedwards.almon.almondatastore.models.service.Service;
import com.capgemini.bedwards.almon.almonmonitoringcore.Monitors;
import com.capgemini.bedwards.almon.notificationcore.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


@RestController
@RequestMapping("/api/service/{serviceId}")
@Slf4j
public class ServiceAPIController extends APIController {

    private final ServiceService SERVICE_SERVICE;
    private final NotificationService NOTIFICATION_SERVICE;
    private final Monitors MONITORS;
    private final UserService USER_SERVICE;

    @Autowired
    public ServiceAPIController(ServiceService serviceService, Monitors monitors, NotificationService notificationService, UserService userService) {
        this.SERVICE_SERVICE = serviceService;
        this.MONITORS = monitors;
        this.NOTIFICATION_SERVICE = notificationService;
        this.USER_SERVICE = userService;
    }

    @DeleteMapping("/users/{userId}")
    @PreAuthorize("hasAuthority('ASSIGN_ROLES') || hasAuthority('SERVICE_' + #service.id + '_CAN_ASSIGN_ROLES')")
    public ResponseEntity<Void> removeUserFromService(
            @PathVariable(name = "serviceId") Service service,
            @PathVariable(name = "userId") User user,
            Model model) {
        if (SERVICE_SERVICE.removeUser(service, user))
            return new ResponseEntity<>(HttpStatus.OK);
        throw new NotFoundException("User does not have service roles for " + service.getId());
    }

    @PutMapping(value = "/users", consumes = "application/json")
    @PreAuthorize("hasAuthority('ASSIGN_ROLES') || hasAuthority('SERVICE_' + #service.id + '_CAN_ASSIGN_ROLES')")
    public ResponseEntity<Void> updateUserFromService(
            @PathVariable(name = "serviceId") Service service,
            @Valid @RequestBody UpdateServiceUserRolesRequestBody requestBody,
            Model model) {
        if (requestBody.getAdmin() != null && requestBody.getAdmin().size() > 0)
            SERVICE_SERVICE.assignAdminRole(service, USER_SERVICE.convertEmailsToUsers(requestBody.getAdmin()));
        if (requestBody.getStandard() != null && requestBody.getStandard().size() > 0)
            SERVICE_SERVICE.assignUserRole(service, USER_SERVICE.convertEmailsToUsers(requestBody.getStandard()));
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @DeleteMapping()
    @PreAuthorize("hasAuthority('DELETE_SERVICES') || hasAuthority('SERVICE_' + #service.id + '_CAN_DELETE')")
    public ResponseEntity<Void> delete(@PathVariable(name = "serviceId") Service service, Model model) {
        SERVICE_SERVICE.deleteService(service);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/enable")
    @PreAuthorize("hasAuthority('ENABLE_DISABLE_SERVICES') || hasAuthority('SERVICE_' + #service.id + '_CAN_ENABLE_DISABLE')")
    public ResponseEntity<String> enable(@PathVariable(name = "serviceId") Service service) {
        SERVICE_SERVICE.enableService(service);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PutMapping("/disable")
    @PreAuthorize("hasAuthority('ENABLE_DISABLE_SERVICES') || hasAuthority('SERVICE_' + #service.id + '_CAN_ENABLE_DISABLE')")
    public ResponseEntity<String> disable(@PathVariable(name = "serviceId") Service service, HttpServletRequest request) {
        SERVICE_SERVICE.disableService(service);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @GetMapping()
    @PreAuthorize("hasAuthority('VIEW_ALL_SERVICES') || hasAuthority('SERVICE_' + #service.id + '_CAN_VIEW')")
    public ResponseEntity<Model> getUsersList(@PathVariable(name = "serviceId") Service service, Model model) {
        model.addAttribute("service", service);
        model.addAttribute("monitors", MONITORS);
        return ResponseEntity.ok(model);
    }

    @PostMapping("/update")
    @PreAuthorize("hasAuthority('UPDATE_SERVICE')  || hasAuthority('SERVICE_' + #service.id + '_CAN_UPDATE')")
    public ResponseEntity<Void> updateService(
            @PathVariable(name = "serviceId") Service service,
            @Valid ServiceUpdateRequestBody serviceUpdateRequestBody) {
        SERVICE_SERVICE.updateService(service, serviceUpdateRequestBody.getName(), serviceUpdateRequestBody.getDescription());
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}