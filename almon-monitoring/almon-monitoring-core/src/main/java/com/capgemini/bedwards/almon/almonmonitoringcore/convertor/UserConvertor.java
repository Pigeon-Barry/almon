package com.capgemini.bedwards.almon.almonmonitoringcore.convertor;

import com.capgemini.bedwards.almon.almoncore.services.user.UserService;
import com.capgemini.bedwards.almon.almondatastore.models.auth.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class UserConvertor implements Converter<UUID, User> {


    public final UserService USER_SERVICE;

    public UserConvertor(UserService userService) {
        this.USER_SERVICE = userService;
    }

    @Override
    public User convert(UUID source) {
        return USER_SERVICE.getUserById(source);
    }
}
