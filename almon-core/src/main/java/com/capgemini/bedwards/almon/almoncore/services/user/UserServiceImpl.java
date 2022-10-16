package com.capgemini.bedwards.almon.almoncore.services.user;

import com.capgemini.bedwards.almon.almoncore.exceptions.NotFoundException;
import com.capgemini.bedwards.almon.almondatastore.models.auth.User;
import com.capgemini.bedwards.almon.almondatastore.repository.auth.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Override
    public Page<User> findPaginated(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return userRepository.findAll(pageable);
    }

    @Override
    public void enableAccount(@NotNull UUID userId) {
        updateEnabledStatus(userId, true);
    }

    private void updateEnabledStatus(@NotNull UUID userId, boolean enabled) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent())
            throw new NotFoundException("User with id '" + userId + "' could not be located");
        User user = userOptional.get();
        user.setEnabled(enabled);
        userRepository.save(user);
    }

    @Override
    public void disableAccount(UUID userId) {
        updateEnabledStatus(userId, false);
    }

    @Override
    public User getUserById(UUID userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent())
            return userOptional.get();
        throw new NotFoundException("User with ID: " + userId + " not found");
    }
}
