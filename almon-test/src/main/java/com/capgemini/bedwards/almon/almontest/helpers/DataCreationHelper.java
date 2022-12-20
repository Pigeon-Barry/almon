package com.capgemini.bedwards.almon.almontest.helpers;

import com.capgemini.bedwards.almon.almondatastore.models.auth.Authority;
import com.capgemini.bedwards.almon.almondatastore.models.auth.Role;
import com.capgemini.bedwards.almon.almondatastore.models.auth.User;

import java.util.*;

public class DataCreationHelper {

    public static User createUser(String email) {
        return createUsers(email).toArray(new User[0])[0];
    }

    public static Set<User> createUsers(String... emails) {
        return new HashSet<User>() {{
            for (String email : emails) {
                add(User.builder().id(UUID.randomUUID()).email(email).build());
            }
        }};
    }

    public static Role createRole(String name) {
        return createRoles(name).toArray(new Role[0])[0];
    }

    public static HashSet<Role> createRoles(String... names) {
        return new HashSet<Role>() {{
            for (String name : names) {
                add(Role.builder().name(name).build());
            }
        }};
    }

    public static List<Authority> createAuthorities(String... authorities) {
        return new ArrayList<Authority>() {{
            for (String authority : authorities) {
                add(Authority.builder().authority(authority).build());
            }
        }};
    }
}
