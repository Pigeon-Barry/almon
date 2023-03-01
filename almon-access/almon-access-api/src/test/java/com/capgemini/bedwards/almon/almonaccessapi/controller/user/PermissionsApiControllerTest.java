package com.capgemini.bedwards.almon.almonaccessapi.controller.user;


import com.capgemini.bedwards.almon.almonaccessapi.controller.APIControllerTest;
import com.capgemini.bedwards.almon.almoncore.exceptions.NotFoundException;
import com.capgemini.bedwards.almon.almoncore.services.auth.AuthorityService;
import com.capgemini.bedwards.almon.almondatastore.models.auth.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.UUID;
import java.util.stream.Stream;

import static com.capgemini.bedwards.almon.almontest.helpers.TestResourceUtil.readResource;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = PermissionsApiController.class)
@ContextConfiguration(
        classes = {
                PermissionsApiController.class
        }
)
public class PermissionsApiControllerTest extends APIControllerTest {
    private static final String CONTROLLER_BASEURL = "/api/user/{userId}/permissions";
    private static final String RESOURCE_PREFIX = "/testData/permissionsApiController";

    @MockBean
    protected AuthorityService authorityService;

    @Override
    protected Stream<APIControllerTest.StandardTestsArguments> getStandardTestsArgumentProvider() {
        Runnable setup = () -> {
            setupValidUser(UUID.fromString("4d7b0d54-cdae-4253-bbd6-d95f6ae69113"));
        };
        return Stream.of(
                APIControllerTest.StandardTestsArguments.of(
                        "Update Authorities",
                        setup,
                        put(CONTROLLER_BASEURL, "4d7b0d54-cdae-4253-bbd6-d95f6ae69113")
                                .content(readResource("VALID_REQUEST.json", RESOURCE_PREFIX)),
                        Collections.singletonList("ASSIGN_PERMISSIONS")
                ));
    }


    @Test
    public void negative_user_not_found() throws Exception {
        addAuthorities("ASSIGN_PERMISSIONS");
        when(userIdStringConvertor.convert(any())).thenAnswer(invocation -> {
            throw new NotFoundException("User with ID: " + invocation.getArgument(0) + " not found");
        });
        UUID correlationId = UUID.randomUUID();
        this.mockMvc
                .perform(
                        put(CONTROLLER_BASEURL, "4d7b0d54-cdae-4253-bbd6-d95f6ae69113")
                                .content(readResource("VALID_REQUEST.json", RESOURCE_PREFIX))
                                .header("x-api-key", TEST_AUTH_TOKEN)
                                .header("CorrelationId", correlationId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()))
                .andExpect(header().string("CorrelationId", correlationId.toString()))
                .andExpect(content().json(readResource("USER_NOT_FOUND.json", RESOURCE_PREFIX), true));
        verify(authorityService, never()).updateAuthorities(any(), any());
    }

    @Test
    public void negative_bad_payload() throws Exception {
        addAuthorities("ASSIGN_PERMISSIONS");
        User user = setupValidUser(UUID.fromString("4d7b0d54-cdae-4253-bbd6-d95f6ae69113"));
        UUID correlationId = UUID.randomUUID();
        this.mockMvc
                .perform(
                        put(CONTROLLER_BASEURL, "4d7b0d54-cdae-4253-bbd6-d95f6ae69113")
                                .content(readResource("INVALID_REQUEST.json", RESOURCE_PREFIX))
                                .header("x-api-key", TEST_AUTH_TOKEN)
                                .header("CorrelationId", correlationId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(header().string("CorrelationId", correlationId.toString()))
                .andExpect(content().json(readResource("INVALID_PAYLOAD.json", RESOURCE_PREFIX), true));
        verify(authorityService, never()).updateAuthorities(any(), any());
    }

    @Test
    public void positive_add_user_permissions() throws Exception {
        addAuthorities("ASSIGN_PERMISSIONS");
        User user = setupValidUser(UUID.fromString("4d7b0d54-cdae-4253-bbd6-d95f6ae69113"));
        UUID correlationId = UUID.randomUUID();
        this.mockMvc
                .perform(
                        put(CONTROLLER_BASEURL, "4d7b0d54-cdae-4253-bbd6-d95f6ae69113")
                                .content(readResource("VALID_REQUEST.json", RESOURCE_PREFIX))
                                .header("x-api-key", TEST_AUTH_TOKEN)
                                .header("CorrelationId", correlationId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is(HttpStatus.ACCEPTED.value()))
                .andExpect(header().string("CorrelationId", correlationId.toString()));
        verify(authorityService, times(1)).updateAuthorities(eq(user), any());
    }
}
