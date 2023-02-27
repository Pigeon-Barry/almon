package com.capgemini.bedwards.almon.almoncore.services.user;

import com.capgemini.bedwards.almon.almondatastore.models.auth.Authority;
import com.capgemini.bedwards.almon.almondatastore.models.auth.Role;
import com.capgemini.bedwards.almon.almondatastore.models.auth.UpdateType;
import com.capgemini.bedwards.almon.almondatastore.models.auth.User;
import com.capgemini.bedwards.almon.almondatastore.models.service.Service;
import com.capgemini.bedwards.almon.almondatastore.repository.auth.RoleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(
        classes = {
                RoleServiceImpl.class
        }
)
public class RoleServiceImplTest {

    @MockBean
    private RoleRepository roleRepository;
    @MockBean
    private UserService userService;

    @Autowired
    private RoleServiceImpl roleService;


    @Test
    public void positive_removeRole_typical() {
        Role role = Role.builder().name("ROLE").build();
        Role role2 = Role.builder().name("ROLE2").build();
        User user = User.builder().roles(new HashSet<Role>() {{
            add(role);
            add(role2);
        }}).build();

        assertEquals(2, user.getRoles().size());
        assertTrue(roleService.removeRole(user, role));
        assertEquals(1, user.getRoles().size());
        assertTrue(user.getRoles().contains(role2));
        assertTrue(roleService.removeRole(user, role2));
        assertEquals(0, user.getRoles().size());
    }

    @Test
    public void negative_removeRole_user_does_not_have_role() {
        Role role = Role.builder().name("ROLE").build();
        Role role2 = Role.builder().name("ROLE2").build();
        User user = User.builder().roles(new HashSet<Role>() {{
            add(role);
        }}).build();

        assertEquals(1, user.getRoles().size());
        assertFalse(roleService.removeRole(user, role2));
        assertEquals(1, user.getRoles().size());
        assertTrue(user.getRoles().contains(role));
    }

    @Test
    public void positive_getAllRoles_typical() {
        Role role = Role.builder().name("ROLE").build();
        Role role2 = Role.builder().name("ROLE2").build();
        List<Role> roleList = new ArrayList<Role>() {{
            add(role);
            add(role2);
        }};
        when(roleRepository.findAll()).thenReturn(roleList);

        assertEquals(roleList, roleService.getAllRoles());
        verify(roleRepository, times(1)).findAll();
    }

    @Test
    public void positive_updateRoles_only_grant() {
        Role role = Role.builder().name("ROLE").build();
        Role role2 = Role.builder().name("ROLE2").build();
        List<Role> roleList = new ArrayList<Role>() {{
            add(role);
            add(role2);
        }};
        User user = User.builder().roles(new HashSet<Role>() {{
            add(role);
        }}).build();

        when(roleRepository.findAll()).thenReturn(roleList);

        Map<String, UpdateType> updateTypeMap = new HashMap<String, UpdateType>() {{
            put(role2.getName(), UpdateType.GRANT);
        }};

        roleService.updateRoles(user, updateTypeMap);
        verify(roleRepository, times(1)).findAll();
        verify(userService, times(1)).save(eq(user));
        assertEquals(2, user.getRoles().size());
        assertTrue(user.getRoles().contains(role2));
    }

    @Test
    public void positive_updateRoles_only_remove() {
        Role role = Role.builder().name("ROLE").build();
        Role role2 = Role.builder().name("ROLE2").build();
        List<Role> roleList = new ArrayList<Role>() {{
            add(role);
            add(role2);
        }};
        User user = User.builder().roles(new HashSet<Role>() {{
            add(role);
            add(role2);
        }}).build();

        when(roleRepository.findAll()).thenReturn(roleList);

        Map<String, UpdateType> updateTypeMap = new HashMap<String, UpdateType>() {{
            put(role2.getName(), UpdateType.REMOVE);
        }};

        roleService.updateRoles(user, updateTypeMap);
        verify(roleRepository, times(1)).findAll();
        verify(userService, times(1)).save(eq(user));
        assertEquals(1, user.getRoles().size());
        assertTrue(user.getRoles().contains(role));
    }

    @Test
    public void positive_updateRoles_only_both() {
        Role role = Role.builder().name("ROLE").build();
        Role role2 = Role.builder().name("ROLE2").build();
        Role role3 = Role.builder().name("ROLE3").build();
        Role role4 = Role.builder().name("ROLE4").build();
        List<Role> roleList = new ArrayList<Role>() {{
            add(role);
            add(role2);
            add(role3);
            add(role4);
        }};
        User user = User.builder().roles(new HashSet<Role>() {{
            add(role);
            add(role2);
        }}).build();

        when(roleRepository.findAll()).thenReturn(roleList);

        Map<String, UpdateType> updateTypeMap = new HashMap<String, UpdateType>() {{
            put(role2.getName(), UpdateType.REMOVE);
            put(role3.getName(), UpdateType.GRANT);
            put(role4.getName(), UpdateType.GRANT);
        }};

        roleService.updateRoles(user, updateTypeMap);
        verify(roleRepository, times(1)).findAll();
        verify(userService, times(1)).save(eq(user));
        assertEquals(3, user.getRoles().size());
        assertTrue(user.getRoles().contains(role));
        assertTrue(user.getRoles().contains(role3));
        assertTrue(user.getRoles().contains(role4));
    }

    @Test
    public void positive_assignRole_single() {
        Role role = Role.builder().name("ROLE").build();
        Role role2 = Role.builder().name("ROLE2").build();
        Role role3 = Role.builder().name("ROLE3").build();
        Role role4 = Role.builder().name("ROLE4").build();
        List<Role> roleList = new ArrayList<Role>() {{
            add(role);
            add(role2);
            add(role3);
            add(role4);
        }};
        User user = User.builder().roles(new HashSet<Role>() {{
            add(role);
        }}).build();

        when(roleRepository.findAll()).thenReturn(roleList);


        roleService.assignRole(user, role2);
        verify(userService, times(1)).save(eq(user));
        assertEquals(2, user.getRoles().size());
        assertTrue(user.getRoles().contains(role));
        assertTrue(user.getRoles().contains(role2));
    }

    @Test
    public void positive_assignRole_multiple() {
        Role role = Role.builder().name("ROLE").build();
        Role role2 = Role.builder().name("ROLE2").build();
        Role role3 = Role.builder().name("ROLE3").build();
        Role role4 = Role.builder().name("ROLE4").build();
        User user = User.builder().roles(new HashSet<Role>() {{
            add(role);
        }}).build();

        roleService.assignRole(user, role2, role3, role4);
        verify(userService, times(1)).save(eq(user));
        assertEquals(4, user.getRoles().size());
        assertTrue(user.getRoles().contains(role));
        assertTrue(user.getRoles().contains(role2));
        assertTrue(user.getRoles().contains(role3));
        assertTrue(user.getRoles().contains(role4));
    }

    @Test
    public void positive_assignRole_str_multiple() {
        Role role = Role.builder().name("ROLE").build();
        Role role2 = Role.builder().name("ROLE2").build();
        Role role3 = Role.builder().name("ROLE3").build();
        Role role4 = Role.builder().name("ROLE4").build();
        List<Role> roleList = new ArrayList<Role>() {{
            add(role);
            add(role2);
            add(role3);
            add(role4);
        }};
        User user = User.builder().roles(new HashSet<Role>() {{
            add(role);
        }}).build();

        when(roleRepository.findAll()).thenReturn(roleList);


        roleService.assignRole(user, role2.getName(), role3.getName(), role4.getName());
        verify(roleRepository, times(1)).findAll();
        verify(userService, times(1)).save(eq(user));
        assertEquals(4, user.getRoles().size());
        assertTrue(user.getRoles().contains(role));
        assertTrue(user.getRoles().contains(role2));
        assertTrue(user.getRoles().contains(role3));
        assertTrue(user.getRoles().contains(role4));
    }

    @Test
    public void positive_assignRoleToUsers_single() {
        Role role = Role.builder().name("ROLE").build();
        User user = User.builder().id(UUID.randomUUID()).roles(new HashSet<>()).build();

        roleService.assignRoleToUsers(role, new HashSet<User>() {{
            add(user);
        }});
        verify(userService, times(1)).save(eq(user));
        assertTrue(user.getRoles().contains(role));
    }

    @Test
    public void positive_assignRoleToUsers_multiple() {
        Role role = Role.builder().name("ROLE").build();
        User user = User.builder().id(UUID.randomUUID()).roles(new HashSet<>()).build();
        User user2 = User.builder().id(UUID.randomUUID()).roles(new HashSet<>()).build();
        User user3 = User.builder().id(UUID.randomUUID()).roles(new HashSet<>()).build();
        roleService.assignRoleToUsers(role, new HashSet<User>() {{
            add(user);
            add(user2);
            add(user3);
        }});
        verify(userService, times(1)).save(eq(user));
        verify(userService, times(1)).save(eq(user2));
        verify(userService, times(1)).save(eq(user3));
        assertTrue(user.getRoles().contains(role));
        assertTrue(user2.getRoles().contains(role));
        assertTrue(user3.getRoles().contains(role));
    }

    @Test
    public void positive_createRole() {
        String name = "ROLE_NAME";
        String description = "ROLE_DESC";
        Set<Authority> authorities = new HashSet<Authority>() {{
            add(Authority.builder().authority("AUTH_1").build());
            add(Authority.builder().authority("AUTH_2").build());
            add(Authority.builder().authority("AUTH_3").build());
        }};
        Role expectedRole = Role.builder().name(name).build();
        when(roleRepository.save(any())).thenReturn(expectedRole);

        assertEquals(expectedRole, roleService.createRole(name, description, authorities));
        ArgumentCaptor<Role> roleArgumentCaptor = ArgumentCaptor.forClass(Role.class);
        verify(roleRepository, times(1)).save(roleArgumentCaptor.capture());
        assertEquals(name, roleArgumentCaptor.getValue().getName());
        assertEquals(description, roleArgumentCaptor.getValue().getDescription());
        assertEquals(authorities, roleArgumentCaptor.getValue().getAuthorities());
    }

    @ParameterizedTest
    @ValueSource(strings = {"Name1", "name2", ""})
    public void positive_getRoleFromName(String name) {
        Role role = Role.builder().name(name).build();
        Optional<Role> roleOptional = Optional.of(role);
        when(roleRepository.findById(eq(name))).thenReturn(roleOptional);
        assertEquals(roleOptional, roleService.getRoleFromName(name));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Name1", "name2", ""})
    public void negative_getRoleFromName_not_found(String name) {
        Optional<Role> roleOptional = Optional.empty();
        when(roleRepository.findById(eq(name))).thenReturn(roleOptional);
        assertEquals(roleOptional, roleService.getRoleFromName(name));
    }

    @Test
    public void positive_findOrCreate_exists() {
        String name = "ROLENAME";
        String description = "ROLEDESC";
        Role role = Role.builder().name(name).description(description).build();
        Optional<Role> roleOptional = Optional.of(role);
        when(roleRepository.findById(eq(name))).thenReturn(roleOptional);
        assertEquals(role, roleService.findOrCreate(name, description));
        verify(roleRepository, times(0)).save(any());
    }

    @Test
    public void positive_findOrCreate_create() {
        String name = "ROLENAME";
        String description = "ROLEDESC";

        Role role = Role.builder().name(name).description(description).build();
        when(roleRepository.save(any())).thenReturn(role);
        Optional<Role> roleOptional = Optional.empty();
        when(roleRepository.findById(eq(name))).thenReturn(roleOptional);

        assertEquals(role, roleService.findOrCreate(name, description));
        verify(roleRepository, times(1)).save(any());
    }

    @Test
    public void positive_deleteServiceRoles() {
        Service service = Service.builder().build();
        roleService.deleteServiceRoles(service);
        verify(roleRepository, times(1)).deleteServiceRoles(eq(service));
    }

}
