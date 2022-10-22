package com.capgemini.bedwards.almon.almoncore.services.user;

import com.capgemini.bedwards.almon.almoncore.exceptions.InvalidPermissionException;
import com.capgemini.bedwards.almon.almoncore.exceptions.NotFoundException;
import com.capgemini.bedwards.almon.almondatastore.models.auth.User;
import com.capgemini.bedwards.almon.almondatastore.repository.auth.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Override
    public Page<User> findPaginated(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return userRepository.findAll(pageable);
    }

    @Override
    public Page<User> findPaginatedWithFilter(int pageNo, int pageSize, Boolean enabled) {
        if (enabled == null)
            return findApprovalsPaginated(pageNo, pageSize);
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return userRepository.findUsersByEnabledEquals(pageable, enabled);
    }

    @Override
    public Page<User> findApprovalsPaginated(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return userRepository.findAll(pageable);
    }

    @Override
    public void enableAccount(@NotNull User authorizer, @NotNull UUID userId) {
        updateEnabledStatus(authorizer, userId, true);
    }

    private void updateEnabledStatus(@NotNull User authoriser, @NotNull UUID userId, boolean enabled) {
        if (authoriser != null && authoriser.getId().equals(userId))
            throw new InvalidPermissionException("Can not disable/enable your own account");
        log.info((enabled ? "Enabling" : "Disabling") + " user account: " + userId);
        User user = getUser(userId);
        user.setEnabled(enabled);
        user.setApprovedBy(authoriser);
        save(user);
    }

    @Override
    public User getUser(@NotNull UUID userId) {
        Optional<User> userOptional = findById(userId);
        if (!userOptional.isPresent())
            throw new NotFoundException("User with id '" + userId + "' could not be located");
        return userOptional.get();
    }

    @Override
    public void disableAccount(@NotNull User authorizer, UUID userId) {
        updateEnabledStatus(authorizer, userId, false);
    }

    @Override
    public User getUserById(UUID userId) {
        Optional<User> userOptional = findById(userId);
        if (userOptional.isPresent())
            return userOptional.get();
        throw new NotFoundException("User with ID: " + userId + " not found");
    }

    @Override
    public Optional<User> findById(UUID userId) {
        return userRepository.findById(userId);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }
}
