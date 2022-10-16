package com.capgemini.bedwards.almon.almoncore.services.user;

import com.capgemini.bedwards.almon.almondatastore.models.auth.User;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface UserService {
    Page<User> findPaginated(int pageNo, int pageSize);

    void enableAccount(UUID userId);
    void disableAccount(UUID userId);
    User getUserById(UUID userId);
}
