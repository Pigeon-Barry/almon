package com.capgemini.bedwards.almon.almoncore.services.user;

import com.capgemini.bedwards.almon.almoncore.exceptions.InvalidPermissionException;
import com.capgemini.bedwards.almon.almoncore.exceptions.NotFoundException;
import com.capgemini.bedwards.almon.almondatastore.models.auth.User;
import com.capgemini.bedwards.almon.almondatastore.repository.auth.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository USER_REPOSITORY;

    public UserServiceImpl(UserRepository userRepository) {
        this.USER_REPOSITORY = userRepository;
    }

    @Override
    public Page<User> findPaginated(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return USER_REPOSITORY.findAll(pageable);
    }

    @Override
    public Page<User> findPaginatedWithFilter(int pageNo, int pageSize, Boolean enabled) {
        if (enabled == null)
            return findApprovalsPaginated(pageNo, pageSize);
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return USER_REPOSITORY.findUsersByEnabledEquals(pageable, enabled);
    }

    @Override
    public Page<User> findApprovalsPaginated(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return USER_REPOSITORY.findAll(pageable);
    }

    @Override
    public void enableAccount(@NotNull User authorizer, @NotNull User user) {
        updateEnabledStatus(authorizer, user, true);
    }

    private void updateEnabledStatus(@NotNull User authoriser, @NotNull User user, boolean enabled) {
        if (authoriser != null && authoriser.getId().equals(user.getId()))
            throw new InvalidPermissionException("Can not disable/enable your own account");
        log.info((enabled ? "Enabling" : "Disabling") + " user account: " + user.getId());
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
    public void disableAccount(@NotNull User authorizer, User user) {
        updateEnabledStatus(authorizer, user, false);
    }

    @Override
    public User getUserById(UUID userId) {
        Optional<User> userOptional = findById(userId);
        if (userOptional.isPresent())
            return userOptional.get();
        throw new NotFoundException("User with ID: " + userId + " not found");
    }

    @Override
    public User getUserByEmail(String email) {
        Optional<User> userOptional = USER_REPOSITORY.findUserByEmail(email);
        if (userOptional.isPresent())
            return userOptional.get();
        throw new NotFoundException("User with Email: " + email + " not found");
    }

    @Override
    public Set<User> convertEmailsToUsers(Set<String> emails) {
        Set<User> users = new HashSet<>();
        for (String email : emails)
            users.add(getUserByEmail(email));
        return users;
    }

    @Override
    public Optional<User> findById(UUID userId) {
        return USER_REPOSITORY.findById(userId);
    }


    @Override
    public User save(User user) {
        return USER_REPOSITORY.saveAndFlush(user);
    }


}
