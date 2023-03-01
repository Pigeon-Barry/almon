package com.capgemini.bedwards.almon.almonaccessapi.controller.user;


import com.capgemini.bedwards.almon.almonaccessapi.controller.APIControllerTest;
import com.capgemini.bedwards.almon.almoncore.exceptions.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.UUID;
import java.util.stream.Stream;

import static com.capgemini.bedwards.almon.almontest.helpers.TestResourceUtil.readResource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = UserApiController.class)
@ContextConfiguration(
        classes = {
                UserApiController.class
        }
)
public class UserApiControllerTest extends APIControllerTest {
    private static final String CONTROLLER_BASEURL = "/api/user/{userId}";
    private static final String RESOURCE_PREFIX = "/testData/userApiController";

    @Override
    protected Stream<StandardTestsArguments> getStandardTestsArgumentProvider() {
        Runnable setup = () -> {
            setupValidUser(UUID.fromString("4d7b0d54-cdae-4253-bbd6-d95f6ae69113"));
        };
        return Stream.of(
                StandardTestsArguments.of(
                        "Get User",
                        setup,
                        get(CONTROLLER_BASEURL, "4d7b0d54-cdae-4253-bbd6-d95f6ae69113"),
                        Collections.singletonList("VIEW_ALL_USERS")
                ));
    }

    @Test
    public void negative_user_not_found() throws Exception {
        addAuthorities("VIEW_ALL_USERS");
        when(userIdStringConvertor.convert(any())).thenAnswer(invocation -> {
            throw new NotFoundException("User with ID: " + invocation.getArgument(0) + " not found");
        });
        UUID correlationId = UUID.randomUUID();
        this.mockMvc
                .perform(
                        get(CONTROLLER_BASEURL, "4d7b0d54-cdae-4253-bbd6-d95f6ae69113")
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
    public void positive_get_user() throws Exception {
        addAuthorities("VIEW_ALL_USERS");
        setupValidUser(UUID.fromString("4d7b0d54-cdae-4253-bbd6-d95f6ae69113"));
        UUID correlationId = UUID.randomUUID();
        this.mockMvc
                .perform(
                        get(CONTROLLER_BASEURL, "4d7b0d54-cdae-4253-bbd6-d95f6ae69113")
                                .header("x-api-key", TEST_AUTH_TOKEN)
                                .header("CorrelationId", correlationId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(header().string("CorrelationId", correlationId.toString()))
                .andExpect(content().json(readResource("VALID_RESPONSE.json", RESOURCE_PREFIX), true));
    }
}
