package com.capgemini.bedwards.almon.almoncore.services.service;

import com.capgemini.bedwards.almon.almondatastore.models.auth.User;
import com.capgemini.bedwards.almon.almondatastore.models.service.Service;
import org.springframework.data.domain.Page;

public interface ServiceService {
    Page<Service> findPaginated(int pageNo, int pageSize);

    boolean checkKeyExists(String value);

    Service createService(User owner, String id, String name, String description);

    Page<Service> findPaginatedFromUser(int pageNumber, int pageSize, User user);
}
