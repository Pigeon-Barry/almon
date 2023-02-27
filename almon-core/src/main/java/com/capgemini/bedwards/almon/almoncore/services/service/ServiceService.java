package com.capgemini.bedwards.almon.almoncore.services.service;

import com.capgemini.bedwards.almon.almondatastore.models.auth.Role;
import com.capgemini.bedwards.almon.almondatastore.models.auth.User;
import com.capgemini.bedwards.almon.almondatastore.models.service.Service;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface ServiceService {
    Page<Service> findPaginated(int pageNo, int pageSize);

    Optional<Service> findById(String serviceId);

    boolean checkKeyExists(String value);

    void assignAdminRole(Service service, Set<User> users);

    void assignUserRole(Service service, Set<User> users);

    Service createService(String id, String name, String description);

    Service createService(User owner, String id, String name, String description);

    Page<Service> findPaginatedFromUser(int pageNumber, int pageSize, User user);

    List<Service> findServicesFromUser(User user);

    Service findServiceById(String serviceId);

    void enableService(Service service);

    void disableService(Service service);

    Service save(Service service);

    Role getOrCreateUserRole(Service service);

    Role getOrCreateAdminRole(Service service);


    void deleteService(Service service);

    Service updateService(Service service, String name, String description);

    Map<String, Set<User>> getUsersByServiceRole(Service service);

    boolean removeUser(Service service, User user);


}
