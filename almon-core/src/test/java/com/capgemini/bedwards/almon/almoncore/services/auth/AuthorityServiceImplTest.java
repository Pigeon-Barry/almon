package com.capgemini.bedwards.almon.almoncore.services.auth;

import com.capgemini.bedwards.almon.almoncore.services.user.UserService;
import com.capgemini.bedwards.almon.almondatastore.models.auth.Authority;
import com.capgemini.bedwards.almon.almondatastore.models.auth.Role;
import com.capgemini.bedwards.almon.almondatastore.models.auth.UpdateType;
import com.capgemini.bedwards.almon.almondatastore.models.auth.User;
import com.capgemini.bedwards.almon.almondatastore.models.monitor.Monitor;
import com.capgemini.bedwards.almon.almondatastore.models.service.Service;
import com.capgemini.bedwards.almon.almondatastore.repository.auth.AuthorityRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import javax.persistence.EntityManager;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Stream;

import static com.capgemini.bedwards.almon.almontest.helpers.DataCreationHelper.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthorityServiceImplTest {

    @Mock
    private AuthorityRepository authorityRepository;

    @Mock
    private UserService userService;

    @Mock
    private EntityManager entityManager;

    public static Stream<Arguments> createAuthorityArgumentProvider() {
        return Stream.of(
                Arguments.of("Auth 1", "Desc 1", createUsers("User 1"), createRoles("Role 1")),
                Arguments.of("Auth 2", "Desc 1", createUsers("User 1", "User 2", "User 3"),
                        createRoles("Role 1", "Role 2", "Role 3")),
                Arguments.of("_-)9123£&^*(", "_-)9123£&^*(444", createUsers("User 1"),
                        createRoles("Role 1")),
                Arguments.of("Auth 1", null, createUsers("User 1"), createRoles("Role 1")),
                Arguments.of("Auth 1", "Desc 1", null, createRoles("Role 1")),
                Arguments.of("Auth 1", "Desc 1", createUsers("User 1"), null),
                Arguments.of("Auth 1", null, null, null)
        );
    }


    private AuthorityServiceImpl getAuthorityService() {
        AuthorityServiceImpl authorityService = new AuthorityServiceImpl(authorityRepository, userService);
        ReflectionTestUtils.setField(authorityService, "entityManager", entityManager);
        return authorityService;
    }


    @ParameterizedTest
    @MethodSource("createAuthorityArgumentProvider")
    public void positive_createAuthority_createAuthority(String authority, String description,
                                                         Set<User> defaultUsers, Set<Role> roles) {
        when(authorityRepository.saveAndFlush(any())).thenAnswer(
                invocation -> invocation.getArgument(0));

        final Authority CREATED_AUTHORITY = getAuthorityService().createAuthority(authority,
                description,
                defaultUsers, roles);

        assertEquals(authority, CREATED_AUTHORITY.getAuthority());
        assertEquals(description, CREATED_AUTHORITY.getDescription());
        //Null check required here as authority users is never null but rather an empty set
        assertIterableEquals(defaultUsers == null ? new HashSet<>() : defaultUsers, CREATED_AUTHORITY.getUsers());
        assertIterableEquals(roles, CREATED_AUTHORITY.getRoles());
    }

    @Test
    public void negative_createAuthority_nullAuthority() {
        assertThrows(IllegalArgumentException.class,
                () -> getAuthorityService().createAuthority(null, "", new HashSet<>(), new HashSet<>()),
                "Authority must not be null");
    }

    @Test
    public void positive_getAllAuthorities_valid() {
        List<Authority> authorities = createAuthorities("Auth 1", "Auth 2", "Auth 3");
        when(authorityRepository.findAll()).thenReturn(authorities);
        assertEquals(authorities, getAuthorityService().getAllAuthorities());
    }

    private Map<String, Authority> createAuthorityMap(List<Authority> authorities) {
        return new HashMap<String, Authority>() {{
            for (Authority authority : authorities) {
                put(authority.getAuthority(), authority);
            }
        }};
    }


    public static Stream<Arguments> updateAuthoritiesArgumentProvider() {
        return Stream.of(
                Arguments.of(new HashMap<String, UpdateType>() {{
                    put("Auth 1", UpdateType.GRANT);
                }}, createAuthorities("Auth 1", "Auth 2", "Auth 3", "Auth 4")),
                Arguments.of(new HashMap<String, UpdateType>() {{
                    put("Auth 6", UpdateType.GRANT);
                    put("Auth 8", UpdateType.GRANT);
                }}, createAuthorities("Auth 9", "Auth 8", "Auth 7", "Auth 6")),

                Arguments.of(new HashMap<String, UpdateType>() {{
                    put("Auth 3", UpdateType.REMOVE);
                }}, createAuthorities("Auth 1", "Auth 2", "Auth 3", "Auth 4")),
                Arguments.of(new HashMap<String, UpdateType>() {{
                    put("Auth 2", UpdateType.REMOVE);
                    put("Auth 4", UpdateType.REMOVE);
                }}, createAuthorities("Auth 1", "Auth 2", "Auth 3", "Auth 4")),

                Arguments.of(new HashMap<String, UpdateType>() {{
                    put("Auth 2", UpdateType.GRANT);
                    put("Auth 4", UpdateType.REMOVE);
                }}, createAuthorities("Auth 1", "Auth 2", "Auth 3", "Auth 4")),

                Arguments.of(new HashMap<String, UpdateType>() {{
                    put("Auth 2", null);
                }}, createAuthorities("Auth 1", "Auth 2", "Auth 3", "Auth 4"))
        );
    }

    @ParameterizedTest
    @MethodSource("updateAuthoritiesArgumentProvider")
    public void positive_updateAuthorities(Map<String, UpdateType> updatesToMake, List<Authority> authorities) {
        Map<String, Authority> authorityMap = createAuthorityMap(authorities);
        when(authorityRepository.findAll()).thenReturn(authorities);
        User user = (User) createUsers("email").toArray()[0];
        //Pre Processing
        for (Entry<String, UpdateType> entry : updatesToMake.entrySet()) {
            if (entry.getValue() == UpdateType.REMOVE) {
                Authority authority = authorityMap.get(entry.getKey());
                authority.addUser(user);
                assertTrue(authority.getUsers().contains(user));
            }
        }
        //Action
        getAuthorityService().updateAuthorities(user, updatesToMake);

        //Validation
        for (Entry<String, UpdateType> entry : updatesToMake.entrySet()) {
            Authority authority = authorityMap.get(entry.getKey());

            if (entry.getValue() == UpdateType.GRANT) {
                assertTrue(authority.getUsers().contains(user));
            } else {
                assertFalse(authority.getUsers().contains(user));
            }
        }
        for (Authority authority : authorities) {
            if (authorityMap.containsKey(authority.getAuthority()))
                continue;
            assertFalse(authority.getUsers().contains(user));
        }
    }


    @Test
    public void negative_updateAuthorities_authorityDoesNotExist() {
        List<Authority> authorities = createAuthorities("Auth 1", "Auth 2", "Auth 3", "Auth 4");
        Map<String, UpdateType> updatesToMake = new HashMap<String, UpdateType>() {{
            put("NOT_EXIST", UpdateType.GRANT);
        }};
        when(authorityRepository.findAll()).thenReturn(authorities);
        User user = (User) createUsers("email").toArray()[0];

        for (Authority authority : authorities)
            assertFalse(authority.getUsers().contains(user));
        assertEquals(4, authorities.size());
        //Action
        getAuthorityService().updateAuthorities(user, updatesToMake);

        for (Authority authority : authorities)
            assertFalse(authority.getUsers().contains(user));
        assertEquals(4, authorities.size());
    }

    @Test
    public void negative_updateAuthorities_authorityDoesNotHaveUser() {
        List<Authority> authorities = createAuthorities("Auth 1", "Auth 2", "Auth 3", "Auth 4");
        Map<String, UpdateType> updatesToMake = new HashMap<String, UpdateType>() {{
            put("Auth 1", UpdateType.REMOVE);
        }};
        when(authorityRepository.findAll()).thenReturn(authorities);
        User user = (User) createUsers("email").toArray()[0];

        for (Authority authority : authorities)
            assertFalse(authority.getUsers().contains(user));
        assertEquals(4, authorities.size());
        //Action
        getAuthorityService().updateAuthorities(user, updatesToMake);

        for (Authority authority : authorities)
            assertFalse(authority.getUsers().contains(user));
        assertEquals(4, authorities.size());
    }

    public static Stream<Arguments> addRemoveAuthoritiesArgumentProvider() {
        return Stream.of(
                Arguments.of(new HashSet<String>()),
                Arguments.of(new HashSet<String>() {{
                    add("Auth 1");
                }}),
                Arguments.of(new HashSet<String>() {{
                    add("Auth 1");
                    add("Auth 3");
                }}),
                Arguments.of(new HashSet<String>() {{
                    add("Auth 1");
                    add("Auth 2");
                    add("Auth 3");
                    add("Auth 4");
                }}),
                Arguments.of(new HashSet<String>() {{
                    add("NOT_EXIST");
                }})
        );
    }

    @ParameterizedTest
    @MethodSource("addRemoveAuthoritiesArgumentProvider")
    public void positive_addAuthorities_valid(Set<String> authoritiesToAdd) {
        //Prep
        List<Authority> authorities = createAuthorities("Auth 1", "Auth 2", "Auth 3", "Auth 4");

        when(authorityRepository.findAll()).thenReturn(authorities);
        User user = (User) createUsers("email").toArray()[0];

        //Action
        getAuthorityService().addAuthorities(user, authoritiesToAdd.toArray(new String[0]));

        //Validation
        for (Authority authority : authorities) {
            if (authoritiesToAdd.contains(authority.getAuthority()))
                assertTrue(authority.getUsers().contains(user));
            else
                assertFalse(authority.getUsers().contains(user));
        }
        assertEquals(4, authorities.size());
    }

    @ParameterizedTest
    @MethodSource("addRemoveAuthoritiesArgumentProvider")
    public void positive_removeAuthorities_valid(Set<String> authoritiesToAdd) {
        //Prep
        List<Authority> authorities = createAuthorities("Auth 1", "Auth 2", "Auth 3", "Auth 4");
        when(authorityRepository.findAll()).thenReturn(authorities);
        User user = (User) createUsers("email").toArray()[0];
        authorities.forEach(authority -> authority.addUser(user));

        //Action
        getAuthorityService().removeAuthorities(user, authoritiesToAdd.toArray(new String[0]));

        //Validation
        for (Authority authority : authorities) {
            if (authoritiesToAdd.contains(authority.getAuthority()))
                assertFalse(authority.getUsers().contains(user));
            else
                assertTrue(authority.getUsers().contains(user));
        }
        assertEquals(4, authorities.size());
    }


    public static Stream<Arguments> addRoleAuthoritiesArgumentProvider() {
        return Stream.of(
                Arguments.of(new HashSet<String>()),
                Arguments.of(new HashSet<String>() {{
                    add("Role 1");
                }}),
                Arguments.of(new HashSet<String>() {{
                    add("Role 1");
                    add("Role 3");
                }}),
                Arguments.of(new HashSet<String>() {{
                    add("Role 1");
                    add("Role 2");
                    add("Role 3");
                    add("Role 4");
                }})
        );
    }

    @ParameterizedTest
    @MethodSource("addRoleAuthoritiesArgumentProvider")
    public void positive_addRole_valid(Set<String> roleNames) {
        //Prep
        HashSet<Role> roles = createRoles(roleNames.toArray(new String[0]));

        Authority authority = createAuthorities("Auth 1").get(0);

        assertNull(authority.getRoles());
        //Action
        getAuthorityService().addRole(authority, roles);

        //Validation
        assertIterableEquals(roles, authority.getRoles());
        verify(authorityRepository, times(1)).saveAndFlush(authority);
    }

    @Test
    public void positive_addRole_existingRoles() {
        //Prep
        HashSet<Role> existingRoles = createRoles("Role 1", "Role 2");
        HashSet<Role> newRoles = createRoles("New Role 1", "New Role 2");

        Authority authority = createAuthorities("Auth 1").get(0);
        authority.setRoles(existingRoles);

        assertIterableEquals(existingRoles, authority.getRoles());
        //Action
        getAuthorityService().addRole(authority, newRoles);

        //Validation
        assertIterableEquals(new HashSet<Role>() {{
            addAll(existingRoles);
            addAll(newRoles);
        }}, authority.getRoles());
        verify(authorityRepository, times(1)).saveAndFlush(authority);
    }

    @Test
    public void positive_save_valid() {
        Authority authority = createAuthorities("Auth 1").get(0);
        getAuthorityService().save(authority);
        verify(authorityRepository, times(1)).saveAndFlush(authority);
    }

    @Test
    public void positive_deleteServiceAuthorities_valid() {
        Service service = Service.builder().build();
        getAuthorityService().deleteServiceAuthorities(service);
        verify(authorityRepository, times(1)).deleteServiceAuthorities(service);
    }

    @Test
    public void positive_deleteMonitorAuthorities_valid() {
        Monitor monitor = new TestMonitor();
        getAuthorityService().deleteMonitorAuthorities(monitor);
        verify(authorityRepository, times(1)).deleteMonitorAuthorities(monitor);
    }

    @Test
    public void positive_refreshRole_valid() {
        Role role = (Role) createRoles("Role 1").toArray()[0];
        getAuthorityService().refreshRole(role);
        verify(entityManager, times(1)).refresh(role);
    }

    @Test
    public void positive_refreshAuthority_valid() {
        Authority authority = createAuthorities("Auth 1").get(0);
        getAuthorityService().refreshAuthority(authority);
        verify(entityManager, times(1)).refresh(authority);
    }

    private class TestMonitor extends Monitor {
        @Override
        public String getMonitorType() {
            return null;
        }
    }
}


