package com.capgemini.bedwards.almon.almoncore.services.user;

import com.capgemini.bedwards.almon.almondatastore.models.auth.User;
import org.springframework.data.domain.Page;

import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    Page<User> findPaginated(int pageNo, int pageSize);

    Page<User> findPaginatedWithFilter(int pageNo, int pageSize, Boolean enabled);

    Page<User> findApprovalsPaginated(int pageNo, int pageSize);


    void enableAccount(@NotNull User authorizer, @NotNull User user);

    User getUser(@NotNull UUID userId);

    void disableAccount(@NotNull User authorizer, @NotNull User user);

    User getUserById(UUID userId);


    User save(User user);

    Optional<User> findById(UUID userId);
}
