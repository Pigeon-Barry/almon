package com.capgemini.bedwards.almon.almonaccessapi.controller.services;


import com.capgemini.bedwards.almon.almonaccessapi.controller.APIControllerTest;
import com.capgemini.bedwards.almon.almoncore.exceptions.NotFoundException;
import com.capgemini.bedwards.almon.almondatastore.models.auth.User;
import com.capgemini.bedwards.almon.almondatastore.models.service.Service;
import com.capgemini.bedwards.almon.almonmonitoringcore.Monitors;
import com.capgemini.bedwards.almon.notificationcore.service.NotificationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Collections;
import java.util.UUID;
import java.util.stream.Stream;

import static com.capgemini.bedwards.almon.almontest.helpers.TestResourceUtil.readResource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = ServiceAPIController.class)
@ContextConfiguration(
        classes = {
                ServiceAPIController.class
        }
)
public class ServiceAPIControllerTest extends APIControllerTest {


    private static final String CONTROLLER_BASEURL = "/api/service/{serviceId}";

    private static final String CONTROLLER_BASEURL_REMOVE_USER = CONTROLLER_BASEURL + "/users/{userId}";


    private static final String CONTROLLER_BASEURL_UPDATE_USER = CONTROLLER_BASEURL + "/users";
    private static final String CONTROLLER_BASEURL_ENABLE = CONTROLLER_BASEURL + "/enable";
    private static final String CONTROLLER_BASEURL_DISABLE = CONTROLLER_BASEURL + "/disable";
    private static final String CONTROLLER_BASEURL_GET = CONTROLLER_BASEURL;
    private static final String CONTROLLER_BASEURL_UPDATE = CONTROLLER_BASEURL + "/update";

    private static final String RESOURCE_PREFIX = "/testData/serviceAPIController";

    @MockBean
    private Monitors monitors;
    @MockBean
    private NotificationServiceImpl notificationService;

    private static Stream<Arguments> requestBuilderProvider() {
        return Stream.of(
                Arguments.of(delete(CONTROLLER_BASEURL_REMOVE_USER, "myServiceId", "4d7b0d54-cdae-4253-bbd6-d95f6ae69113")),
                Arguments.of(put(CONTROLLER_BASEURL_UPDATE_USER, "myServiceId").content(readResource("UPDATE_USERS_REQUEST.json", RESOURCE_PREFIX))),
                Arguments.of(delete(CONTROLLER_BASEURL, "myServiceId")),
                Arguments.of(put(CONTROLLER_BASEURL_ENABLE, "myServiceId")),
                Arguments.of(put(CONTROLLER_BASEURL_DISABLE, "myServiceId")),
                Arguments.of(get(CONTROLLER_BASEURL_GET, "myServiceId")),
                Arguments.of(put(CONTROLLER_BASEURL_UPDATE, "myServiceId").content(readResource("UPDATE_SERVICE_REQUEST.json", RESOURCE_PREFIX)))
        );
    }

    @Override
    protected Stream<StandardTestsArguments> getStandardTestsArgumentProvider() {
        Runnable setup = () -> {
            setupValidService("myServiceId");
            setupValidUser(UUID.fromString("4d7b0d54-cdae-4253-bbd6-d95f6ae69113"));
        };

        return Stream.of(
                StandardTestsArguments.of(
                        "Remove User froms service",
                        setup,
                        delete(CONTROLLER_BASEURL_REMOVE_USER, "myServiceId", UUID.fromString("4d7b0d54-cdae-4253-bbd6-d95f6ae69113")),
                        Collections.singletonList("ASSIGN_ROLES")
                ), StandardTestsArguments.of(
                        "Update users on service",
                        setup,
                        put(CONTROLLER_BASEURL_UPDATE_USER, "myServiceId").content(readResource("UPDATE_USERS_REQUEST.json", RESOURCE_PREFIX)),
                        Collections.singletonList("ASSIGN_ROLES")
                ), StandardTestsArguments.of(
                        "Delete Service",
                        setup,
                        delete(CONTROLLER_BASEURL, "myServiceId"),
                        Collections.singletonList("DELETE_SERVICES")
                ), StandardTestsArguments.of(
                        "Enable Service",
                        setup,
                        put(CONTROLLER_BASEURL_ENABLE, "myServiceId"),
                        Collections.singletonList("ENABLE_DISABLE_SERVICES")
                ), StandardTestsArguments.of(
                        "Disable Service",
                        setup,
                        put(CONTROLLER_BASEURL_DISABLE, "myServiceId"),
                        Collections.singletonList("ENABLE_DISABLE_SERVICES")
                ), StandardTestsArguments.of(
                        "Get Service",
                        setup,
                        get(CONTROLLER_BASEURL_GET, "myServiceId"),
                        Collections.singletonList("VIEW_ALL_SERVICES")
                ), StandardTestsArguments.of(
                        "Update Service",
                        setup,
                        put(CONTROLLER_BASEURL_UPDATE, "myServiceId")
                                .content(readResource("UPDATE_SERVICE_REQUEST.json", RESOURCE_PREFIX)),
                        Collections.singletonList("UPDATE_SERVICE")
                )
        );
    }

    @ParameterizedTest
    @MethodSource("requestBuilderProvider")
    public void negative_service_not_found(MockHttpServletRequestBuilder requestBuilder) throws Exception {
        addAuthorities("ASSIGN_ROLES", "DELETE_SERVICES", "ENABLE_DISABLE_SERVICES", "VIEW_ALL_SERVICES", "UPDATE_SERVICE");
        when(serviceConvertor.convert(any())).thenAnswer(invocation -> {
            throw new NotFoundException("Service with Key: '" + invocation.getArgument(0) + "' not found");
        });

        setupValidMonitor(Service.builder().id("myServiceId").build(), "myMonitorId");
        UUID correlationId = UUID.randomUUID();
        this.mockMvc
                .perform(
                        requestBuilder
                                .header("x-api-key", TEST_AUTH_TOKEN)
                                .header("CorrelationId", correlationId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()))
                .andExpect(header().string("CorrelationId", correlationId.toString()))
                .andExpect(content().json(readResource("SERVICE_NOT_FOUND.json", RESOURCE_PREFIX), true));
    }

    @Test
    public void negative_user_not_found() throws Exception {
        addAuthorities("ASSIGN_ROLES");
        when(userIdStringConvertor.convert(any())).thenAnswer(invocation -> {
            throw new NotFoundException("User with ID: " + invocation.getArgument(0) + " not found");
        });
        setupValidService("myServiceId");

        setupValidMonitor(Service.builder().id("myServiceId").build(), "myMonitorId");
        UUID correlationId = UUID.randomUUID();
        this.mockMvc
                .perform(
                        delete(CONTROLLER_BASEURL_REMOVE_USER, "myServiceId", "4d7b0d54-cdae-4253-bbd6-d95f6ae69113")
                                .header("x-api-key", TEST_AUTH_TOKEN)
                                .header("CorrelationId", correlationId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()))
                .andExpect(header().string("CorrelationId", correlationId.toString()))
                .andExpect(content().json(readResource("USER_NOT_FOUND.json", RESOURCE_PREFIX), true));
    }

    @Test
    public void negative_remove_user_user_not_in_service() throws Exception {
        addAuthorities("ASSIGN_ROLES");
        Service service = setupValidService("myServiceId");
        User user = setupValidUser(UUID.fromString("4d7b0d54-cdae-4253-bbd6-d95f6ae69113"));
        UUID correlationId = UUID.randomUUID();
        when(serviceService.removeUser(eq(service), eq(user))).thenReturn(false);
        this.mockMvc
                .perform(
                        delete(CONTROLLER_BASEURL_REMOVE_USER, "myServiceId", "4d7b0d54-cdae-4253-bbd6-d95f6ae69113")
                                .header("x-api-key", TEST_AUTH_TOKEN)
                                .header("CorrelationId", correlationId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()))
                .andExpect(header().string("CorrelationId", correlationId.toString()))
                .andExpect(content().json(readResource("USER_NOT_FOUND_IN_SERVICE.json", RESOURCE_PREFIX), true));
        verify(serviceService, times(1)).removeUser(eq(service), eq(user));
    }

    @Test
    public void positive_remove_user() throws Exception {
        addAuthorities("ASSIGN_ROLES");
        Service service = setupValidService("myServiceId");
        User user = setupValidUser(UUID.fromString("4d7b0d54-cdae-4253-bbd6-d95f6ae69113"));
        UUID correlationId = UUID.randomUUID();
        when(serviceService.removeUser(eq(service), eq(user))).thenReturn(true);
        this.mockMvc
                .perform(
                        delete(CONTROLLER_BASEURL_REMOVE_USER, "myServiceId", "4d7b0d54-cdae-4253-bbd6-d95f6ae69113")
                                .header("x-api-key", TEST_AUTH_TOKEN)
                                .header("CorrelationId", correlationId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(header().string("CorrelationId", correlationId.toString()));
        verify(serviceService, times(1)).removeUser(eq(service), eq(user));
    }

    @Test
    public void positive_update_user_standard() throws Exception {
        addAuthorities("ASSIGN_ROLES");
        Service service = setupValidService("myServiceId");
        User user = setupValidUser(UUID.fromString("4d7b0d54-cdae-4253-bbd6-d95f6ae69113"));
        UUID correlationId = UUID.randomUUID();
        when(serviceService.removeUser(eq(service), eq(user))).thenReturn(true);
        this.mockMvc
                .perform(
                        put(CONTROLLER_BASEURL_UPDATE_USER, "myServiceId")
                                .content(readResource("UPDATE_USERS_REQUEST_STANDARD.json", RESOURCE_PREFIX))
                                .header("x-api-key", TEST_AUTH_TOKEN)
                                .header("CorrelationId", correlationId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(header().string("CorrelationId", correlationId.toString()));
        verify(serviceService, times(1)).assignUserRole(eq(service), any());
        verify(serviceService, never()).assignAdminRole(eq(service), any());
    }

    @Test
    public void positive_update_user_admin() throws Exception {
        addAuthorities("ASSIGN_ROLES");
        Service service = setupValidService("myServiceId");
        User user = setupValidUser(UUID.fromString("4d7b0d54-cdae-4253-bbd6-d95f6ae69113"));
        UUID correlationId = UUID.randomUUID();
        when(serviceService.removeUser(eq(service), eq(user))).thenReturn(true);
        this.mockMvc
                .perform(
                        put(CONTROLLER_BASEURL_UPDATE_USER, "myServiceId")
                                .content(readResource("UPDATE_USERS_REQUEST_ADMIN.json", RESOURCE_PREFIX))
                                .header("x-api-key", TEST_AUTH_TOKEN)
                                .header("CorrelationId", correlationId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(header().string("CorrelationId", correlationId.toString()));
        verify(serviceService, never()).assignUserRole(eq(service), any());
        verify(serviceService, times(1)).assignAdminRole(eq(service), any());
    }

    @Test
    public void positive_update_user_both() throws Exception {
        addAuthorities("ASSIGN_ROLES");
        Service service = setupValidService("myServiceId");
        User user = setupValidUser(UUID.fromString("4d7b0d54-cdae-4253-bbd6-d95f6ae69113"));
        UUID correlationId = UUID.randomUUID();
        when(serviceService.removeUser(eq(service), eq(user))).thenReturn(true);
        this.mockMvc
                .perform(
                        put(CONTROLLER_BASEURL_UPDATE_USER, "myServiceId")
                                .content(readResource("UPDATE_USERS_REQUEST_BOTH.json", RESOURCE_PREFIX))
                                .header("x-api-key", TEST_AUTH_TOKEN)
                                .header("CorrelationId", correlationId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(header().string("CorrelationId", correlationId.toString()));
        verify(serviceService, times(1)).assignUserRole(eq(service), any());
        verify(serviceService, times(1)).assignAdminRole(eq(service), any());
    }

    @Test
    public void positive_delete_service() throws Exception {
        addAuthorities("DELETE_SERVICES");
        Service service = setupValidService("myServiceId");
        UUID correlationId = UUID.randomUUID();
        this.mockMvc
                .perform(
                        delete(CONTROLLER_BASEURL, "myServiceId")
                                .header("x-api-key", TEST_AUTH_TOKEN)
                                .header("CorrelationId", correlationId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(header().string("CorrelationId", correlationId.toString()));
        verify(serviceService, times(1)).deleteService(eq(service));
    }

    @Test
    public void positive_enable_service() throws Exception {
        addAuthorities("ENABLE_DISABLE_SERVICES");
        Service service = setupValidService("myServiceId");
        UUID correlationId = UUID.randomUUID();
        this.mockMvc
                .perform(
                        put(CONTROLLER_BASEURL_ENABLE, "myServiceId")
                                .header("x-api-key", TEST_AUTH_TOKEN)
                                .header("CorrelationId", correlationId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is(HttpStatus.ACCEPTED.value()))
                .andExpect(header().string("CorrelationId", correlationId.toString()));
        verify(serviceService, times(1)).enableService(eq(service));
    }

    @Test
    public void positive_disable_service() throws Exception {
        addAuthorities("ENABLE_DISABLE_SERVICES");
        Service service = setupValidService("myServiceId");
        UUID correlationId = UUID.randomUUID();
        this.mockMvc
                .perform(
                        put(CONTROLLER_BASEURL_DISABLE, "myServiceId")
                                .header("x-api-key", TEST_AUTH_TOKEN)
                                .header("CorrelationId", correlationId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is(HttpStatus.ACCEPTED.value()))
                .andExpect(header().string("CorrelationId", correlationId.toString()));
        verify(serviceService, times(1)).disableService(eq(service));
    }

    @Test
    public void positive_get_service() throws Exception {
        addAuthorities("VIEW_ALL_SERVICES");
        setupValidService("myServiceId");
        UUID correlationId = UUID.randomUUID();
        this.mockMvc
                .perform(
                        get(CONTROLLER_BASEURL_GET, "myServiceId")
                                .header("x-api-key", TEST_AUTH_TOKEN)
                                .header("CorrelationId", correlationId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(header().string("CorrelationId", correlationId.toString()))
                .andExpect(content().json(readResource("GET_SERVICE_RESPONSE.json", RESOURCE_PREFIX), true));
    }

    @Test
    public void positive_update_service() throws Exception {
        addAuthorities("UPDATE_SERVICE");
        Service service = setupValidService("myServiceId");
        UUID correlationId = UUID.randomUUID();
        this.mockMvc
                .perform(
                        put(CONTROLLER_BASEURL_UPDATE, "myServiceId")
                                .content(readResource("UPDATE_SERVICE_REQUEST.json", RESOURCE_PREFIX))
                                .header("x-api-key", TEST_AUTH_TOKEN)
                                .header("CorrelationId", correlationId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(header().string("CorrelationId", correlationId.toString()));
        verify(serviceService, times(1)).updateService(eq(service), any(), any());
    }
}