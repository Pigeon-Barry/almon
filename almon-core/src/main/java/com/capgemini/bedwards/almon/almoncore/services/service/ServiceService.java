package com.capgemini.bedwards.almon.almoncore.services.service;

import com.capgemini.bedwards.almon.almondatastore.models.auth.User;
import com.capgemini.bedwards.almon.almondatastore.models.service.Service;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.UUID;

public interface ServiceService {
    Page<Service> findPaginated(int pageNo, int pageSize);

    Optional<Service> findById(String serviceId);

    boolean checkKeyExists(String value);

    Service createService(String id, String name, String description);

    Service createService(User owner,String id, String name, String description);

    Page<Service> findPaginatedFromUser(int pageNumber, int pageSize, User user);

    Service findServiceById(String serviceId);

    void enableService(String serviceId);
    void disableService(String serviceId);

    Service save(Service service);

}
