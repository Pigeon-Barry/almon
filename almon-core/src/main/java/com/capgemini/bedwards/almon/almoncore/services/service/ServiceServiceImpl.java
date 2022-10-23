package com.capgemini.bedwards.almon.almoncore.services.service;

import com.capgemini.bedwards.almon.almoncore.exceptions.NotFoundException;
import com.capgemini.bedwards.almon.almoncore.service.AuthorityService;
import com.capgemini.bedwards.almon.almoncore.services.user.UserService;
import com.capgemini.bedwards.almon.almondatastore.models.auth.User;
import com.capgemini.bedwards.almon.almondatastore.models.service.Service;
import com.capgemini.bedwards.almon.almondatastore.repository.ServiceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Optional;

@org.springframework.stereotype.Service
@Slf4j
public class ServiceServiceImpl implements ServiceService {

    @Autowired
    ServiceRepository serviceRepository;

    @Autowired
    AuthorityService authorityService;
    @Autowired
    UserService userService;

    @Override
    public Page<Service> findPaginated(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        return serviceRepository.findAll(pageable);
    }

    @Override
    public Page<Service> findPaginatedFromUser(int pageNumber, int pageSize, User user) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        return serviceRepository.findServicesByUsersIs(pageable, user);
    }

    @Override
    public Service findServiceById(String serviceId) {
        Optional<Service> serviceOptional = findById(serviceId);
        if (serviceOptional.isPresent())
            return serviceOptional.get();
        throw new NotFoundException("Service with Key: " + serviceId + " not found");
    }

    @Override
    public void enableService(String serviceId) {
        updateEnabledStatus(serviceId, true);
    }

    @Override
    public void disableService(String serviceId) {
        updateEnabledStatus(serviceId, false);

    }

    @Override
    public Service save(Service service) {
        return serviceRepository.save(service);
    }

    private void updateEnabledStatus(@NotNull String serviceId, boolean enabled) {
        log.info((enabled ? "Enabling" : "Disabling") + " Service: " + serviceId);
        Service service = findServiceById(serviceId);
        service.setEnabled(enabled);
        save(service);
    }

    @Override
    public Optional<Service> findById(String serviceId) {
        return serviceRepository.findById(serviceId);
    }

    @Override
    public boolean checkKeyExists(String key) {
        return serviceRepository.existsById(key);
    }

    @Override
    public Service createService(String id, String name, String description) {
        log.info("Creating new service with id: " + id + " name: " + name + " description: " + description);
        Service service = serviceRepository.save(Service.builder()
                .id(id)
                .name(name)
                .description(description)
                .build());

        authorityService.createAuthority(
                "SERVICE_" + id + "_CAN_VIEW",
                "Grants the ability to view this service"
        );
        authorityService.createAuthority(
                "SERVICE_" + id + "_ADMIN_CAN_EDIT",
                "Grants the ability to update core details about this service as well as delete permission"
        );
        authorityService.createAuthority(
                "SERVICE_" + id + "_CAN_CREATE_MONITORING",
                "Grants the ability to create new monitors for this service"
        );
        authorityService.createAuthority(
                "SERVICE_" + id + "_CAN_ENABLE_DISABLE",
                "Grants the ability to create alerts for this service"
        );

        return service;
    }

    @Override
    public Service createService(User owner, String id, String name, String description) {
        Service service = createService(id, name, description);

        authorityService.addAuthorities(owner,
                "SERVICE_" + id + "_CAN_VIEW",
                "SERVICE_" + id + "_ADMIN_CAN_EDIT",
                "SERVICE_" + id + "_CAN_CREATE_MONITORING",
                "SERVICE_" + id + "_CAN_ENABLE_DISABLE");
        if (service.getUsers() == null)
            service.setUsers(new HashSet<>());
        service.getUsers().add(owner);
        serviceRepository.save(service);

        return service;
    }


//    @Override
//    public Page<Service> findPaginatedFromUser(int pageNumber, int pageSize, User user) {
//        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
//        return serviceRepository.findAllById(pageable);
//    }
}
