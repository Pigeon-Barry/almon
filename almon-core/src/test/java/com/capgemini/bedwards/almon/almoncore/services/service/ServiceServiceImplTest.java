package com.capgemini.bedwards.almon.almoncore.services.service;

import com.capgemini.bedwards.almon.almoncore.exceptions.NotFoundException;
import com.capgemini.bedwards.almon.almoncore.services.auth.AuthorityService;
import com.capgemini.bedwards.almon.almoncore.services.user.RoleService;
import com.capgemini.bedwards.almon.almondatastore.models.auth.Role;
import com.capgemini.bedwards.almon.almondatastore.models.auth.User;
import com.capgemini.bedwards.almon.almondatastore.models.monitor.Monitor;
import com.capgemini.bedwards.almon.almondatastore.models.schedule.ScheduledMonitor;
import com.capgemini.bedwards.almon.almondatastore.models.schedule.ScheduledTask;
import com.capgemini.bedwards.almon.almondatastore.models.schedule.Scheduler;
import com.capgemini.bedwards.almon.almondatastore.models.service.Service;
import com.capgemini.bedwards.almon.almondatastore.repository.ServiceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(
        classes = {
                ServiceServiceImpl.class
        }
)
public class ServiceServiceImplTest {

    @MockBean
    private ServiceRepository serviceRepository;
    @MockBean
    private AuthorityService authorityService;
    @MockBean
    private RoleService roleService;
    @MockBean
    private Scheduler scheduler;

    @Autowired
    private ServiceServiceImpl serviceImpl;

    @Test
    public void positive_findPaginated_typical() {
        Page<Service> page = mock(Page.class);
        Pageable pageable = PageRequest.of(0, 1);
        when(serviceRepository.findAll(eq(pageable))).thenReturn(page);
        assertEquals(page, serviceImpl.findPaginated(1, 1));
        verify(serviceRepository, times(1)).findAll(eq(pageable));
    }

    @Test
    public void positive_findPaginated_typical_2() {
        Page<Service> page = mock(Page.class);
        Pageable pageable = PageRequest.of(9, 100);
        when(serviceRepository.findAll(eq(pageable))).thenReturn(page);
        assertEquals(page, serviceImpl.findPaginated(10, 100));
        verify(serviceRepository, times(1)).findAll(eq(pageable));
    }

    @Test
    public void positive_findPaginatedFromUser_typical() {
        Pageable pageable = PageRequest.of(0, 1);
        Page<Service> page = mock(Page.class);
        User user = User.builder().id(UUID.randomUUID()).build();
        when(serviceRepository.findServicesUsersCanView(eq(pageable), eq(user.getId().toString()))).thenReturn(page);

        assertEquals(page, serviceImpl.findPaginatedFromUser(1, 1, user));
        verify(serviceRepository, times(1)).findServicesUsersCanView(eq(pageable), eq(user.getId().toString()));
    }

    @Test
    public void positive_findPaginatedFromUser_typical_2() {
        Pageable pageable = PageRequest.of(9, 100);
        Page<Service> page = mock(Page.class);
        User user = User.builder().id(UUID.randomUUID()).build();
        when(serviceRepository.findServicesUsersCanView(eq(pageable), eq(user.getId().toString()))).thenReturn(page);

        assertEquals(page, serviceImpl.findPaginatedFromUser(10, 100, user));
        verify(serviceRepository, times(1)).findServicesUsersCanView(eq(pageable), eq(user.getId().toString()));
    }

    @Test
    public void positive_findServicesFromUser_typical() {
        User user = User.builder().id(UUID.randomUUID()).build();
        List<Service> serviceList = new ArrayList<>();
        when(serviceRepository.findServicesUsersCanView(eq(user.getId().toString()))).thenReturn(serviceList);
        assertEquals(serviceList, serviceImpl.findServicesFromUser(user));
        verify(serviceRepository, times(1)).findServicesUsersCanView(eq(user.getId().toString()));
    }

    @Test
    public void positive_findServiceById_typical() {
        Service service = Service.builder().id("myId").build();
        Optional<Service> serviceOptional = Optional.of(service);

        when(serviceRepository.findById(eq(service.getId()))).thenReturn(serviceOptional);

        assertEquals(service, serviceImpl.findServiceById(service.getId()));
        verify(serviceRepository, times(1)).findById(eq(service.getId()));
    }

    @ParameterizedTest
    @ValueSource(strings = {"myId", "myOtherId"})
    public void negative_findServiceById_not_found(String id) {
        Optional<Service> serviceOptional = Optional.empty();

        when(serviceRepository.findById(eq(id))).thenReturn(serviceOptional);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> serviceImpl.findServiceById(id));
        assertEquals("Service with Key: '" + id + "' not found", exception.getMessage());
        verify(serviceRepository, times(1)).findById(eq(id));
    }


    public static Stream<Arguments> enableDisableServiceSupportProvider() {
        return Stream.of(
                Arguments.of(Service.builder().build(), true),
                Arguments.of(Service.builder().build(), false),
                Arguments.of(Service.builder()
                        .monitors(new HashSet<Monitor>() {{
                            add(mock(Monitor.class));
                        }}).build(), true),
                Arguments.of(Service.builder()
                        .monitors(new HashSet<Monitor>() {{
                            add(mock(Monitor.class));
                        }}).build(), false),
                Arguments.of(Service.builder()
                        .monitors(new HashSet<Monitor>() {{
                            add(mock(ScheduledMonitor.class));
                        }}).build(), true),
                Arguments.of(Service.builder()
                        .monitors(new HashSet<Monitor>() {{
                            add(mock(ScheduledMonitor.class));
                        }}).build(), false),
                Arguments.of(Service.builder()
                        .monitors(new HashSet<Monitor>() {{
                            add(mock(Monitor.class));
                            add(mock(ScheduledMonitor.class));
                            add(mock(Monitor.class));
                            add(mock(Monitor.class));
                            add(mock(ScheduledMonitor.class));
                            add(mock(ScheduledMonitor.class));
                        }}).build(), true),
                Arguments.of(Service.builder()
                        .monitors(new HashSet<Monitor>() {{
                            add(mock(Monitor.class));
                            add(mock(ScheduledMonitor.class));
                            add(mock(Monitor.class));
                            add(mock(Monitor.class));
                            add(mock(ScheduledMonitor.class));
                            add(mock(ScheduledMonitor.class));
                        }}).build(), false)
        );
    }

    @ParameterizedTest
    @MethodSource("enableDisableServiceSupportProvider")
    public void enableDisableServiceSupport(Service service, boolean enabled) {
        //Prep
        ScheduledTask<?> scheduledTask = mock(ScheduledTask.class);
        if (service.getMonitors() != null) {
            service.getMonitors().forEach(monitor -> {
                if (monitor instanceof ScheduledMonitor) {
                    when(((ScheduledMonitor) monitor).getScheduledTask()).thenReturn(scheduledTask);
                    when(((ScheduledMonitor) monitor).getTaskId()).thenReturn("TaskId");
                }
            });
        }
        //Execute
        if (enabled) {
            serviceImpl.enableService(service);
        } else {
            serviceImpl.disableService(service);
        }
        //Validate
        assertEquals(enabled, service.isEnabled());
        verify(serviceRepository, times(1)).save(eq(service));
        long scheduledCount = service.getMonitors() == null ? 0 : service.getMonitors().stream().filter(monitor -> monitor instanceof ScheduledMonitor).count();
        if (enabled) {
            verify(scheduler, times((int) scheduledCount)).scheduleTask(eq(scheduledTask));
        } else {
            verify(scheduler, times((int) scheduledCount)).removeScheduledTask(eq("TaskId"));
        }
    }

    @Test
    public void positive_save_typical() {
        Service service = Service.builder().build();
        when(serviceRepository.save(eq(service))).thenReturn(service);

        assertEquals(service, serviceImpl.save(service));
        verify(serviceRepository, times(1)).save(eq(service));
    }

    @ParameterizedTest
    @ValueSource(strings = {"myId", "myOtherId"})
    public void positive_findById_typical(String id) {
        Service service = Service.builder().id(id).build();
        Optional<Service> serviceOptional = Optional.of(service);
        when(serviceRepository.findById(eq(id))).thenReturn(serviceOptional);

        assertEquals(serviceOptional, serviceImpl.findById(id));
        verify(serviceRepository, times(1)).findById(eq(id));
    }

    @Test
    public void negative_findById_not_found() {
        String id = "myId";
        Optional<Service> serviceOptional = Optional.empty();
        when(serviceRepository.findById(eq(id))).thenReturn(serviceOptional);
        assertEquals(serviceOptional, serviceImpl.findById(id));
        verify(serviceRepository, times(1)).findById(eq(id));
    }

    @ParameterizedTest
    @ValueSource(strings = {"myId", "myOtherId"})
    public void positive_checkKeyExists_typical(String id) {
        when(serviceRepository.existsById(eq(id))).thenReturn(true);
        assertTrue(serviceImpl.checkKeyExists(id));
        verify(serviceRepository, times(1)).existsById(eq(id));
    }

    @ParameterizedTest
    @ValueSource(strings = {"myId", "myOtherId"})
    public void negative_checkKeyExists_not_found(String id) {
        when(serviceRepository.existsById(eq(id))).thenReturn(false);
        assertFalse(serviceImpl.checkKeyExists(id));
        verify(serviceRepository, times(1)).existsById(eq(id));
    }

    public static Stream<Arguments> updateServiceProvider() {
        return Stream.of(
                Arguments.of("Name", "Description"),
                Arguments.of(null, "Description"),
                Arguments.of("Name", null),
                Arguments.of(null, null),
                Arguments.of("Unique", "Unique desc")
        );
    }

    @ParameterizedTest
    @MethodSource("updateServiceProvider")
    public void positive_updateService_typical(String name, String description) {
        Service service = Service.builder().build();
        assertNull(service.getName());
        assertNull(service.getDescription());

        when(serviceRepository.save(eq(service))).thenReturn(service);
        Service newService = serviceImpl.updateService(service, name, description);
        assertEquals(name, newService.getName());
        assertEquals(description, newService.getDescription());
        verify(serviceRepository, times(1)).save(eq(service));
    }

    @ParameterizedTest
    @ValueSource(strings = {"myId", "myOtherId"})
    public void positive_getOrCreateAdminRole(String id) {
        Service service = Service.builder().id(id).build();
        Role role = Role.builder().name("Role-name").build();

        when(roleService.findOrCreate(eq("SERVICE_" + service.getId() + "_ADMIN"), eq("Standard Admin permissions"))).thenReturn(role);
        assertEquals(role, serviceImpl.getOrCreateAdminRole(service));
        verify(roleService, times(1)).findOrCreate(eq("SERVICE_" + service.getId() + "_ADMIN"), eq("Standard Admin permissions"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"myId", "myOtherId"})
    public void positive_getOrCreateUserRole(String id) {
        Service service = Service.builder().id(id).build();
        Role role = Role.builder().name("Role-name").build();

        when(roleService.findOrCreate(eq("SERVICE_" + service.getId() + "_USER"), eq("Standard User permissions"))).thenReturn(role);
        assertEquals(role, serviceImpl.getOrCreateUserRole(service));
        verify(roleService, times(1)).findOrCreate(eq("SERVICE_" + service.getId() + "_USER"), eq("Standard User permissions"));
    }

    @Test
    public void positive_getUsersByServiceRole_both() {
        Service service = Service.builder().id("serviceId").build();
        Role standardRole = Role.builder().name("Role-std").build();
        Role adminRole = Role.builder().name("Role-admin").build();
        when(roleService.findOrCreate(eq("SERVICE_" + service.getId() + "_USER"), eq("Standard User permissions"))).thenReturn(standardRole);
        when(roleService.findOrCreate(eq("SERVICE_" + service.getId() + "_ADMIN"), eq("Standard Admin permissions"))).thenReturn(adminRole);

        Set<User> standardUsers = new HashSet<User>() {{
            add(User.builder().id(UUID.randomUUID()).build());
            add(User.builder().id(UUID.randomUUID()).build());
            add(User.builder().id(UUID.randomUUID()).build());
        }};
        Set<User> adminUsers = new HashSet<User>() {{
            add(User.builder().id(UUID.randomUUID()).build());
            add(User.builder().id(UUID.randomUUID()).build());
        }};

        standardRole.setUsers(standardUsers);
        adminRole.setUsers(adminUsers);

        HashMap<String, Set<User>> expectedResult = new HashMap<>();
        expectedResult.put("ADMIN", adminUsers);
        expectedResult.put("STANDARD", standardUsers);
        assertEquals(expectedResult, serviceImpl.getUsersByServiceRole(service));
    }

    @Test
    public void positive_getUsersByServiceRole_std() {
        Service service = Service.builder().id("serviceId").build();
        Role standardRole = Role.builder().name("Role-std").build();
        Role adminRole = Role.builder().name("Role-admin").build();
        when(roleService.findOrCreate(eq("SERVICE_" + service.getId() + "_USER"), eq("Standard User permissions"))).thenReturn(standardRole);
        when(roleService.findOrCreate(eq("SERVICE_" + service.getId() + "_ADMIN"), eq("Standard Admin permissions"))).thenReturn(adminRole);

        Set<User> standardUsers = new HashSet<User>() {{
            add(User.builder().id(UUID.randomUUID()).build());
            add(User.builder().id(UUID.randomUUID()).build());
            add(User.builder().id(UUID.randomUUID()).build());
        }};
        Set<User> adminUsers = new HashSet<>();

        standardRole.setUsers(standardUsers);
        adminRole.setUsers(adminUsers);

        HashMap<String, Set<User>> expectedResult = new HashMap<>();
        expectedResult.put("ADMIN", adminUsers);
        expectedResult.put("STANDARD", standardUsers);
        assertEquals(expectedResult, serviceImpl.getUsersByServiceRole(service));
    }

    @Test
    public void positive_getUsersByServiceRole_admin() {
        Service service = Service.builder().id("serviceId").build();
        Role standardRole = Role.builder().name("Role-std").build();
        Role adminRole = Role.builder().name("Role-admin").build();
        when(roleService.findOrCreate(eq("SERVICE_" + service.getId() + "_USER"), eq("Standard User permissions"))).thenReturn(standardRole);
        when(roleService.findOrCreate(eq("SERVICE_" + service.getId() + "_ADMIN"), eq("Standard Admin permissions"))).thenReturn(adminRole);

        Set<User> standardUsers = new HashSet<User>();
        Set<User> adminUsers = new HashSet<User>() {{
            add(User.builder().id(UUID.randomUUID()).build());
            add(User.builder().id(UUID.randomUUID()).build());
        }};

        standardRole.setUsers(standardUsers);
        adminRole.setUsers(adminUsers);

        HashMap<String, Set<User>> expectedResult = new HashMap<>();
        expectedResult.put("ADMIN", adminUsers);
        expectedResult.put("STANDARD", standardUsers);
        assertEquals(expectedResult, serviceImpl.getUsersByServiceRole(service));
    }

    @Test
    public void positive_getUsersByServiceRole_admin_in_stnd() {
        Service service = Service.builder().id("serviceId").build();
        Role standardRole = Role.builder().name("Role-std").build();
        Role adminRole = Role.builder().name("Role-admin").build();
        when(roleService.findOrCreate(eq("SERVICE_" + service.getId() + "_USER"), eq("Standard User permissions"))).thenReturn(standardRole);
        when(roleService.findOrCreate(eq("SERVICE_" + service.getId() + "_ADMIN"), eq("Standard Admin permissions"))).thenReturn(adminRole);


        User dupUser = User.builder().id(UUID.randomUUID()).build();
        Set<User> standardUsers = new HashSet<User>() {{
            add(dupUser);
            add(User.builder().id(UUID.randomUUID()).build());
            add(User.builder().id(UUID.randomUUID()).build());
        }};
        Set<User> adminUsers = new HashSet<User>() {{
            add(dupUser);
            add(User.builder().id(UUID.randomUUID()).build());
        }};

        standardRole.setUsers(standardUsers);
        adminRole.setUsers(adminUsers);

        HashMap<String, Set<User>> expectedResult = new HashMap<>();
        expectedResult.put("ADMIN", adminUsers);
        expectedResult.put("STANDARD", standardUsers);
        Map<String, Set<User>> actualResult = serviceImpl.getUsersByServiceRole(service);

        standardUsers.remove(dupUser);

        assertEquals(expectedResult, actualResult);
    }


    @Test
    public void positive_removeUser_both() {
        Service service = Service.builder().id("myId").build();
        User user = User.builder().build();
        Role standardRole = Role.builder().name("Role-std").build();
        Role adminRole = Role.builder().name("Role-admin").build();
        when(roleService.findOrCreate(eq("SERVICE_" + service.getId() + "_USER"), eq("Standard User permissions"))).thenReturn(standardRole);
        when(roleService.findOrCreate(eq("SERVICE_" + service.getId() + "_ADMIN"), eq("Standard Admin permissions"))).thenReturn(adminRole);

        when(roleService.removeRole(eq(user), eq(standardRole))).thenReturn(true);
        when(roleService.removeRole(eq(user), eq(adminRole))).thenReturn(true);
        assertTrue(serviceImpl.removeUser(service, user));

        verify(roleService, times(1)).removeRole(eq(user), eq(standardRole));
        verify(roleService, times(1)).removeRole(eq(user), eq(adminRole));
    }

    @Test
    public void positive_removeUser_standard_only() {
        Service service = Service.builder().id("myId").build();
        User user = User.builder().build();
        Role standardRole = Role.builder().name("Role-std").build();
        Role adminRole = Role.builder().name("Role-admin").build();
        when(roleService.findOrCreate(eq("SERVICE_" + service.getId() + "_USER"), eq("Standard User permissions"))).thenReturn(standardRole);
        when(roleService.findOrCreate(eq("SERVICE_" + service.getId() + "_ADMIN"), eq("Standard Admin permissions"))).thenReturn(adminRole);

        when(roleService.removeRole(eq(user), eq(standardRole))).thenReturn(true);
        when(roleService.removeRole(eq(user), eq(adminRole))).thenReturn(false);
        assertTrue(serviceImpl.removeUser(service, user));

        verify(roleService, times(1)).removeRole(eq(user), eq(standardRole));
        verify(roleService, times(1)).removeRole(eq(user), eq(adminRole));
    }

    @Test
    public void positive_removeUser_admin_only() {
        Service service = Service.builder().id("myId").build();
        User user = User.builder().build();
        Role standardRole = Role.builder().name("Role-std").build();
        Role adminRole = Role.builder().name("Role-admin").build();
        when(roleService.findOrCreate(eq("SERVICE_" + service.getId() + "_USER"), eq("Standard User permissions"))).thenReturn(standardRole);
        when(roleService.findOrCreate(eq("SERVICE_" + service.getId() + "_ADMIN"), eq("Standard Admin permissions"))).thenReturn(adminRole);

        when(roleService.removeRole(eq(user), eq(standardRole))).thenReturn(false);
        when(roleService.removeRole(eq(user), eq(adminRole))).thenReturn(true);
        assertTrue(serviceImpl.removeUser(service, user));

        verify(roleService, times(1)).removeRole(eq(user), eq(standardRole));
        verify(roleService, times(1)).removeRole(eq(user), eq(adminRole));
    }

    @Test
    public void negative_removeUser_none() {
        Service service = Service.builder().id("myId").build();
        User user = User.builder().build();
        Role standardRole = Role.builder().name("Role-std").build();
        Role adminRole = Role.builder().name("Role-admin").build();
        when(roleService.findOrCreate(eq("SERVICE_" + service.getId() + "_USER"), eq("Standard User permissions"))).thenReturn(standardRole);
        when(roleService.findOrCreate(eq("SERVICE_" + service.getId() + "_ADMIN"), eq("Standard Admin permissions"))).thenReturn(adminRole);

        when(roleService.removeRole(eq(user), eq(standardRole))).thenReturn(false);
        when(roleService.removeRole(eq(user), eq(adminRole))).thenReturn(false);
        assertFalse(serviceImpl.removeUser(service, user));

        verify(roleService, times(1)).removeRole(eq(user), eq(standardRole));
        verify(roleService, times(1)).removeRole(eq(user), eq(adminRole));
    }

    @Test
    public void positive_assignAdminRole_typical() {
        Service service = Service.builder().id("myId").build();
        Role standardRole = Role.builder().name("Role-std").build();
        Role adminRole = Role.builder().name("Role-admin").build();
        Set<User> users = new HashSet<User>() {{
            add(User.builder().id(UUID.randomUUID()).build());
            add(User.builder().id(UUID.randomUUID()).build());
            add(User.builder().id(UUID.randomUUID()).build());
        }};
        when(roleService.findOrCreate(eq("SERVICE_" + service.getId() + "_USER"), eq("Standard User permissions"))).thenReturn(standardRole);
        when(roleService.findOrCreate(eq("SERVICE_" + service.getId() + "_ADMIN"), eq("Standard Admin permissions"))).thenReturn(adminRole);

        serviceImpl.assignAdminRole(service, users);

        verify(roleService, times(1)).assignRoleToUsers(eq(standardRole), eq(users));
        verify(roleService, times(1)).assignRoleToUsers(eq(adminRole), eq(users));
    }

    @Test
    public void positive_assignUserRole_typical() {
        Service service = Service.builder().id("myId").build();
        Role standardRole = Role.builder().name("Role-std").build();
        Role adminRole = Role.builder().name("Role-admin").build();
        Set<User> users = new HashSet<User>() {{
            add(User.builder().id(UUID.randomUUID()).build());
            add(User.builder().id(UUID.randomUUID()).build());
            add(User.builder().id(UUID.randomUUID()).build());
        }};
        when(roleService.findOrCreate(eq("SERVICE_" + service.getId() + "_USER"), eq("Standard User permissions"))).thenReturn(standardRole);
        when(roleService.findOrCreate(eq("SERVICE_" + service.getId() + "_ADMIN"), eq("Standard Admin permissions"))).thenReturn(adminRole);

        serviceImpl.assignUserRole(service, users);

        verify(roleService, times(1)).assignRoleToUsers(eq(standardRole), eq(users));
        verify(roleService, times(0)).assignRoleToUsers(eq(adminRole), eq(users));
    }


    public static Stream<Arguments> createServiceProvider() {
        return Stream.of(
                Arguments.of("id", "Name", "Description"),
                Arguments.of("newId", "newName", "newDescription")
        );
    }

    @ParameterizedTest
    @MethodSource("createServiceProvider")
    public void positive_createService(String id, String name, String description) {
        Service service = Service.builder().id(id).name(name).description(description).build();
        when(serviceRepository.save(eq(service))).thenReturn(service);

        Role standardRole = Role.builder().name("Role-std").build();
        Set<Role> standardRoleSet = new HashSet<Role>() {{
            add(standardRole);
        }};
        Role adminRole = Role.builder().name("Role-admin").build();
        Set<Role> adminRoleSet = new HashSet<Role>() {{
            add(adminRole);
        }};
        when(roleService.findOrCreate(eq("SERVICE_" + service.getId() + "_USER"), eq("Standard User permissions"))).thenReturn(standardRole);
        when(roleService.findOrCreate(eq("SERVICE_" + service.getId() + "_ADMIN"), eq("Standard Admin permissions"))).thenReturn(adminRole);


        assertEquals(service, serviceImpl.createService(id, name, description));


        verify(authorityService, times(1)).createAuthority(
                eq("SERVICE_" + id + "_CAN_VIEW"),
                eq("Grants the ability to view this service"),
                eq(null),
                eq(standardRoleSet)
        );
        verify(authorityService, times(1)).createAuthority(
                eq("SERVICE_" + id + "_CAN_EDIT"),
                eq("Grants the ability to update core details about this service"),
                eq(null),
                eq(adminRoleSet)
        );
        verify(authorityService, times(1)).createAuthority(
                eq("SERVICE_" + id + "_CAN_DELETE"),
                eq("Grants the ability to delete this service"),
                eq(null),
                eq(adminRoleSet)
        );
        verify(authorityService, times(1)).createAuthority(
                eq("SERVICE_" + id + "_CAN_UPDATE"),
                eq("Grants the ability to update this service"),
                eq(null),
                eq(adminRoleSet)
        );
        verify(authorityService, times(1)).createAuthority(
                eq("SERVICE_" + id + "_CAN_CREATE_MONITORS"),
                eq("Grants the ability to create new monitors for this service"),
                eq(null),
                eq(adminRoleSet)
        );
        verify(authorityService, times(1)).createAuthority(
                eq("SERVICE_" + id + "_CAN_ENABLE_DISABLE"),
                eq("Grants the ability to create alerts for this service"),
                eq(null),
                eq(adminRoleSet)
        );
        verify(authorityService, times(1)).createAuthority(
                eq("SERVICE_" + id + "_CAN_ASSIGN_ROLES"),
                eq("Grants the ability to assign roles to other users"),
                eq(null),
                eq(adminRoleSet)
        );
    }


    @ParameterizedTest
    @MethodSource("createServiceProvider")
    public void positive_createService_with_owner(String id, String name, String description) {
        User owner = User.builder().id(UUID.randomUUID()).build();
        Service service = Service.builder().id(id).name(name).description(description).build();
        when(serviceRepository.save(eq(service))).thenReturn(service);

        Role standardRole = Role.builder().name("Role-std").build();
        Set<Role> standardRoleSet = new HashSet<Role>() {{
            add(standardRole);
        }};
        Role adminRole = Role.builder().name("Role-admin").build();
        Set<Role> adminRoleSet = new HashSet<Role>() {{
            add(adminRole);
        }};
        when(roleService.findOrCreate(eq("SERVICE_" + service.getId() + "_USER"), eq("Standard User permissions"))).thenReturn(standardRole);
        when(roleService.findOrCreate(eq("SERVICE_" + service.getId() + "_ADMIN"), eq("Standard Admin permissions"))).thenReturn(adminRole);


        assertEquals(service, serviceImpl.createService(owner, id, name, description));

        verify(roleService, times(1)).assignRole(
                eq(owner),
                eq(adminRole)
        );
        verify(roleService, times(1)).assignRole(
                eq(owner),
                eq(standardRole)
        );

        verify(authorityService, times(1)).createAuthority(
                eq("SERVICE_" + id + "_CAN_VIEW"),
                eq("Grants the ability to view this service"),
                eq(null),
                eq(standardRoleSet)
        );
        verify(authorityService, times(1)).createAuthority(
                eq("SERVICE_" + id + "_CAN_EDIT"),
                eq("Grants the ability to update core details about this service"),
                eq(null),
                eq(adminRoleSet)
        );
        verify(authorityService, times(1)).createAuthority(
                eq("SERVICE_" + id + "_CAN_DELETE"),
                eq("Grants the ability to delete this service"),
                eq(null),
                eq(adminRoleSet)
        );
        verify(authorityService, times(1)).createAuthority(
                eq("SERVICE_" + id + "_CAN_UPDATE"),
                eq("Grants the ability to update this service"),
                eq(null),
                eq(adminRoleSet)
        );
        verify(authorityService, times(1)).createAuthority(
                eq("SERVICE_" + id + "_CAN_CREATE_MONITORS"),
                eq("Grants the ability to create new monitors for this service"),
                eq(null),
                eq(adminRoleSet)
        );
        verify(authorityService, times(1)).createAuthority(
                eq("SERVICE_" + id + "_CAN_ENABLE_DISABLE"),
                eq("Grants the ability to create alerts for this service"),
                eq(null),
                eq(adminRoleSet)
        );
        verify(authorityService, times(1)).createAuthority(
                eq("SERVICE_" + id + "_CAN_ASSIGN_ROLES"),
                eq("Grants the ability to assign roles to other users"),
                eq(null),
                eq(adminRoleSet)
        );
    }

    @Test
    public void positive_deleteService_typical() {
        Service service = Service.builder().id("myId").build();

        serviceImpl.deleteService(service);

        verify(authorityService, times(1)).deleteServiceAuthorities(eq(service));
        verify(roleService, times(1)).deleteServiceRoles(eq(service));
        verify(serviceRepository, times(1)).delete(eq(service));
    }

}
