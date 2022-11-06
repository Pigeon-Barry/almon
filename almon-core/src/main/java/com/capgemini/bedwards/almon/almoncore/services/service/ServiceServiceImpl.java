package com.capgemini.bedwards.almon.almoncore.services.service;

import com.capgemini.bedwards.almon.almoncore.exceptions.NotFoundException;
import com.capgemini.bedwards.almon.almoncore.services.auth.AuthorityService;
import com.capgemini.bedwards.almon.almoncore.services.user.RoleService;
import com.capgemini.bedwards.almon.almondatastore.models.auth.Authority;
import com.capgemini.bedwards.almon.almondatastore.models.auth.Role;
import com.capgemini.bedwards.almon.almondatastore.models.auth.User;
import com.capgemini.bedwards.almon.almondatastore.models.monitor.Monitor;
import com.capgemini.bedwards.almon.almondatastore.models.schedule.ScheduledMonitor;
import com.capgemini.bedwards.almon.almondatastore.models.schedule.Scheduler;
import com.capgemini.bedwards.almon.almondatastore.models.service.Service;
import com.capgemini.bedwards.almon.almondatastore.repository.ServiceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@org.springframework.stereotype.Service
@Slf4j
public class ServiceServiceImpl implements ServiceService {
    private final ServiceRepository SERVICE_REPOSITORY;
    private final AuthorityService AUTHORITY_SERVICE;
    private final RoleService ROLE_SERVICE;
    @Autowired
    @Lazy
    protected Scheduler SCHEDULER;

    @Autowired
    public ServiceServiceImpl(ServiceRepository serviceRepository,
                              AuthorityService authorityService,
                              RoleService roleService) {
        this.SERVICE_REPOSITORY = serviceRepository;
        this.AUTHORITY_SERVICE = authorityService;
        this.ROLE_SERVICE = roleService;
    }

    @Override
    public Page<Service> findPaginated(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        return SERVICE_REPOSITORY.findAll(pageable);
    }

    @Override
    public Page<Service> findPaginatedFromUser(int pageNumber, int pageSize, User user) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        return SERVICE_REPOSITORY.findServicesUsersCanView(pageable, user.getId().toString());
    }

    @Override
    public Service findServiceById(String serviceId) {
        Optional<Service> serviceOptional = findById(serviceId);
        if (serviceOptional.isPresent())
            return serviceOptional.get();
        throw new NotFoundException("Service with Key: " + serviceId + " not found");
    }

    @Override
    public void enableService(Service service) {
        updateEnabledStatus(service, true);
    }

    @Override
    public void disableService(Service service) {
        updateEnabledStatus(service, false);
    }

    @Override
    public Service save(Service service) {
        return SERVICE_REPOSITORY.saveAndFlush(service);
    }


    private void updateEnabledStatus(Service service, boolean enabled) {
        log.info((enabled ? "Enabling" : "Disabling") + " Service: " + service.getId());
        service.setEnabled(enabled);
        save(service);
        for (Monitor monitor : service.getMonitors()) {
            if (monitor instanceof ScheduledMonitor) {
                if (enabled)
                    SCHEDULER.scheduleTask(((ScheduledMonitor) monitor).getScheduledTask());
                else
                    SCHEDULER.removeScheduledTask(((ScheduledMonitor) monitor).getTaskId());
            }
        }
    }

    @Override
    public Optional<Service> findById(String serviceId) {
        return SERVICE_REPOSITORY.findById(serviceId);
    }

    @Override
    public boolean checkKeyExists(String key) {
        return SERVICE_REPOSITORY.existsById(key);
    }

    @Override
    public Service updateService(Service service, String name, String description) {
        service.setName(name);
        service.setDescription(description);
        return save(service);
    }

    @Override
    public Map<String, Set<User>> getUsersByServiceRole(Service service) {
        Set<User> adminUsers = ROLE_SERVICE.getUsersByRole(getOrCreateAdminRole(service));
        Set<User> standardUsers = ROLE_SERVICE.getUsersByRole(getOrCreateUserRole(service));

        standardUsers.removeAll(adminUsers);
        return new HashMap<String, Set<User>>() {{
            put("ADMIN", adminUsers);
            put("STANDARD", standardUsers);
        }};
    }

    @Override
    public boolean removeUser(Service service, User user) {
        return ROLE_SERVICE.removeRole(user, getOrCreateAdminRole(service))
                || ROLE_SERVICE.removeRole(user, getOrCreateUserRole(service));
    }

    @Override
    public Service createService(String id, String name, String description) {
        log.info("Creating new service with id: " + id + " name: " + name + " description: " + description);
        Service service = SERVICE_REPOSITORY.saveAndFlush(Service.builder()
                .id(id)
                .name(name)
                .description(description)
                .build());


        final Set<Role> adminRoleSet = new HashSet<Role>() {{
            add(getOrCreateAdminRole(service));
        }};

        final Set<Role> userRoleSet = new HashSet<Role>() {{
            add(getOrCreateUserRole(service));
        }};

        AUTHORITY_SERVICE.createAuthority(
                "SERVICE_" + id + "_CAN_VIEW",
                "Grants the ability to view this service",
                null,
                userRoleSet
        );
        AUTHORITY_SERVICE.createAuthority(
                "SERVICE_" + id + "_CAN_EDIT",
                "Grants the ability to update core details about this service",
                null,
                adminRoleSet
        );
        AUTHORITY_SERVICE.createAuthority(
                "SERVICE_" + id + "_CAN_DELETE",
                "Grants the ability to delete this service",
                null,
                adminRoleSet
        );
        AUTHORITY_SERVICE.createAuthority(
                "SERVICE_" + id + "_CAN_UPDATE",
                "Grants the ability to update this service",
                null,
                adminRoleSet
        );
        AUTHORITY_SERVICE.createAuthority(
                "SERVICE_" + id + "_" +
                        "CAN_CREATE_MONITORS",
                "Grants the ability to create new monitors for this service",
                null,
                adminRoleSet
        );
        AUTHORITY_SERVICE.createAuthority(
                "SERVICE_" + id + "_CAN_ENABLE_DISABLE",
                "Grants the ability to create alerts for this service",
                null,
                adminRoleSet
        );
        AUTHORITY_SERVICE.createAuthority(
                "SERVICE_" + id + "_CAN_ASSIGN_ROLES",
                "Grants the ability to assign roles to other users",
                null,
                adminRoleSet
        );


        return service;
    }

    @Override
    public Service createService(User owner, String id, String name, String description) {
        Service service = createService(id, name, description);

        ROLE_SERVICE.assignRole(owner, getOrCreateAdminRole(service));
        ROLE_SERVICE.assignRole(owner, getOrCreateUserRole(service));

        return service;
    }

    @Override
    public Role getOrCreateAdminRole(Service service) {
        return ROLE_SERVICE.findOrCreate("SERVICE_" + service.getId() + "_ADMIN", "Standard Admin permissions");
    }

    @Override
    public void assignAdminAuthority(Service service, Authority authority) {
        AUTHORITY_SERVICE.addRole(authority, Collections.singleton(getOrCreateAdminRole(service)));
    }

    @Override
    public void deleteService(Service service) {
        SERVICE_REPOSITORY.delete(service);
        AUTHORITY_SERVICE.deleteServiceAuthorities(service);
        ROLE_SERVICE.deleteServiceRoles(service);
    }


    @Override
    @Transactional
    public Role getOrCreateUserRole(Service service) {
        return ROLE_SERVICE.findOrCreate("SERVICE_" + service.getId() + "_USER", "Standard User permissions");
    }
}
