package com.capgemini.bedwards.almon.almonaccessapi.controller.user;


import com.capgemini.bedwards.almon.almonaccessapi.controller.APIControllerTest;
import com.capgemini.bedwards.almon.almoncore.exceptions.NotFoundException;
import com.capgemini.bedwards.almon.almoncore.services.user.RoleService;
import com.capgemini.bedwards.almon.almondatastore.models.auth.Role;
import com.capgemini.bedwards.almon.almondatastore.models.auth.User;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = RoleApiController.class)
@ContextConfiguration(
        classes = {
                RoleApiController.class
        }
)
public class RoleApiControllerTest extends APIControllerTest {
    private static final String CONTROLLER_BASEURL = "/api/user/{userId}/roles/{roleName}";
    private static final String RESOURCE_PREFIX = "/testData/roleApiController";

    @MockBean
    protected RoleService roleService;

    @Override
    protected Stream<StandardTestsArguments> getStandardTestsArgumentProvider() {
        Runnable setup = () -> {
            setupValidUser(UUID.fromString("4d7b0d54-cdae-4253-bbd6-d95f6ae69113"));
            setupValidRole("SAMPLE_ROLE");
        };
        return Stream.of(
                StandardTestsArguments.of(
                        "Delete Role from user",
                        setup,
                        delete(CONTROLLER_BASEURL, "4d7b0d54-cdae-4253-bbd6-d95f6ae69113", "SAMPLE_ROLE"),
                        Collections.singletonList("ASSIGN_ROLES")
                ),
                StandardTestsArguments.of(
                        "Add Role to user",
                        setup,
                        put(CONTROLLER_BASEURL, "4d7b0d54-cdae-4253-bbd6-d95f6ae69113", "SAMPLE_ROLE"),
                        Collections.singletonList("ASSIGN_ROLES")
                ));
    }

    private static Stream<Arguments> requestBuilderProvider() {
        return Stream.of(
                Arguments.of(delete(CONTROLLER_BASEURL, "4d7b0d54-cdae-4253-bbd6-d95f6ae69113", "SAMPLE_ROLE")),
                Arguments.of(put(CONTROLLER_BASEURL, "4d7b0d54-cdae-4253-bbd6-d95f6ae69113", "SAMPLE_ROLE"))
        );
    }

    @ParameterizedTest
    @MethodSource("requestBuilderProvider")
    public void negative_user_not_found(MockHttpServletRequestBuilder requestBuilder) throws Exception {
        addAuthorities("ASSIGN_ROLES");
        setupValidRole("SAMPLE_ROLE");

        when(userIdStringConvertor.convert(any())).thenAnswer(invocation -> {
            throw new NotFoundException("User with ID: " + invocation.getArgument(0) + " not found");
        });
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
                .andExpect(content().json(readResource("USER_NOT_FOUND.json", RESOURCE_PREFIX), true));
    }

    @ParameterizedTest
    @MethodSource("requestBuilderProvider")
    public void negative_role_not_found(MockHttpServletRequestBuilder requestBuilder) throws Exception {
        addAuthorities("ASSIGN_ROLES");
        setupValidUser(UUID.fromString("4d7b0d54-cdae-4253-bbd6-d95f6ae69113"));

        when(roleConvertor.convert(any())).thenAnswer(invocation -> {
            throw new NotFoundException("Role with name '" + invocation.getArgument(0) + "' not found");
        });
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
                .andExpect(content().json(readResource("ROLE_NOT_FOUND.json", RESOURCE_PREFIX), true));
    }

    @Test
    public void negative_remove_role_user_oes_not_have_role() throws Exception {
        addAuthorities("ASSIGN_ROLES");
        User user = setupValidUser(UUID.fromString("4d7b0d54-cdae-4253-bbd6-d95f6ae69113"));
        Role role = setupValidRole("SAMPLE_ROLE");
        when(roleService.removeRole(eq(user), eq(role))).thenReturn(false);
        UUID correlationId = UUID.randomUUID();
        this.mockMvc
                .perform(
                        delete(CONTROLLER_BASEURL, "4d7b0d54-cdae-4253-bbd6-d95f6ae69113", "SAMPLE_ROLE")
                                .header("x-api-key", TEST_AUTH_TOKEN)
                                .header("CorrelationId", correlationId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()))
                .andExpect(header().string("CorrelationId", correlationId.toString()))
                .andExpect(content().json(readResource("USER_NOT_HAS_ROLE.json", RESOURCE_PREFIX), true));
        verify(roleService, times(1)).removeRole(eq(user), eq(role));
    }

    @Test
    public void positive_add_role_toUser() throws Exception {
        addAuthorities("ASSIGN_ROLES");
        User user = setupValidUser(UUID.fromString("4d7b0d54-cdae-4253-bbd6-d95f6ae69113"));
        Role role = setupValidRole("SAMPLE_ROLE");

        UUID correlationId = UUID.randomUUID();
        this.mockMvc
                .perform(
                        put(CONTROLLER_BASEURL, "4d7b0d54-cdae-4253-bbd6-d95f6ae69113", "SAMPLE_ROLE")
                                .header("x-api-key", TEST_AUTH_TOKEN)
                                .header("CorrelationId", correlationId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is(HttpStatus.ACCEPTED.value()))
                .andExpect(header().string("CorrelationId", correlationId.toString()));
        verify(roleService, times(1)).assignRole(eq(user), eq(role));
    }

    @Test
    public void positive_remove_role_toUser() throws Exception {
        addAuthorities("ASSIGN_ROLES");
        User user = setupValidUser(UUID.fromString("4d7b0d54-cdae-4253-bbd6-d95f6ae69113"));
        Role role = setupValidRole("SAMPLE_ROLE");
        when(roleService.removeRole(eq(user), eq(role))).thenReturn(true);
        UUID correlationId = UUID.randomUUID();
        this.mockMvc
                .perform(
                        delete(CONTROLLER_BASEURL, "4d7b0d54-cdae-4253-bbd6-d95f6ae69113", "SAMPLE_ROLE")
                                .header("x-api-key", TEST_AUTH_TOKEN)
                                .header("CorrelationId", correlationId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is(HttpStatus.ACCEPTED.value()))
                .andExpect(header().string("CorrelationId", correlationId.toString()));
        verify(roleService, times(1)).removeRole(eq(user), eq(role));
    }


}
