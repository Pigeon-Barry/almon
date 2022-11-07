package com.capgemini.bedwards.almon.almonweb.controller.services;


import com.capgemini.bedwards.almon.almoncore.exceptions.NotFoundException;
import com.capgemini.bedwards.almon.almoncore.services.service.ServiceService;
import com.capgemini.bedwards.almon.almoncore.services.user.UserService;
import com.capgemini.bedwards.almon.almondatastore.models.auth.User;
import com.capgemini.bedwards.almon.almondatastore.models.service.Service;
import com.capgemini.bedwards.almon.almonweb.controller.WebAPIController;
import com.capgemini.bedwards.almon.almonweb.model.services.UpdateServiceUserRolesRequestBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@RequestMapping("/web/service/{serviceId}")
@Slf4j
@PreAuthorize("isAuthenticated()")
public class ServiceWebAPIController extends WebAPIController {


    private final ServiceService SERVICE_SERVICE;
    private final UserService USER_SERVICE;


    @Autowired
    public ServiceWebAPIController(ServiceService serviceService, UserService userService) {
        this.SERVICE_SERVICE = serviceService;
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
}
