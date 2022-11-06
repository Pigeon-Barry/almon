package com.capgemini.bedwards.almon.almonmonitoringcore.convertor;

import com.capgemini.bedwards.almon.almoncore.exceptions.NotFoundException;
import com.capgemini.bedwards.almon.almoncore.services.user.RoleService;
import com.capgemini.bedwards.almon.almondatastore.models.auth.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class RoleConvertor implements Converter<String, Role> {


    public final RoleService ROLE_SERVICE;

    public RoleConvertor(RoleService roleService) {
        this.ROLE_SERVICE = roleService;
    }

    @Override
    public Role convert(String source) {
        Optional<Role> role = ROLE_SERVICE.getRoleFromName(source);
        if(role.isPresent())
            return role.get();
        throw new NotFoundException("Role with name '" + source + "' not found");
    }
}
