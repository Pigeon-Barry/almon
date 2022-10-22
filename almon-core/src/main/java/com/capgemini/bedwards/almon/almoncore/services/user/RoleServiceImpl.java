package com.capgemini.bedwards.almon.almoncore.services.user;

import com.capgemini.bedwards.almon.almoncore.exceptions.NotFoundException;
import com.capgemini.bedwards.almon.almondatastore.models.auth.Role;
import com.capgemini.bedwards.almon.almondatastore.models.auth.UpdateType;
import com.capgemini.bedwards.almon.almondatastore.models.auth.User;
import com.capgemini.bedwards.almon.almondatastore.repository.auth.RoleRepository;
import com.capgemini.bedwards.almon.almondatastore.repository.auth.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class RoleServiceImpl implements RoleService {

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserService userService;

    @SuppressWarnings("SuspiciousMethodCalls")
    @Override
    public void removeRole(UUID userId, String roleName) {
        Optional<User> userOptional = userService.findById(userId);
        if (!userOptional.isPresent())
            throw new NotFoundException("User with ID: " + userId + " could not be located");
        User user = userOptional.get();
        log.info("CLS: " + user.getRoles().getClass());


        Optional<Role> roleToDeleteOptional = user.getRoles().stream().filter(role -> role.getName().equals(roleName)).findFirst();
        if (roleToDeleteOptional.isPresent()) {
            user.getRoles().remove(roleToDeleteOptional.get());
            userService.save(user);
        } else {
            throw new NotFoundException("User does not have role " + roleName);
        }
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public void updateRoles(User user, Map<String, UpdateType> rolesToUpdate) {
        List<Role> roles = getAllRoles();
        roles.forEach(role -> {
            UpdateType updateType = rolesToUpdate.getOrDefault(role.getName(), null);
            if (updateType == null) {
                //Do nothing
            } else if (updateType == UpdateType.REMOVE)
                user.getRoles().remove(role);
            else if (updateType == UpdateType.GRANT)
                user.getRoles().add(role);
            else
                throw new RuntimeException("Update type " + updateType + " does not have any actions assigned to it");
        });
        userService.save(user);
    }
}
