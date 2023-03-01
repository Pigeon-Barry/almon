package com.capgemini.bedwards.almon.almonaccessapi.controller;


import com.capgemini.bedwards.almon.almoncore.services.auth.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static com.capgemini.bedwards.almon.almontest.helpers.TestResourceUtil.readResource;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = AuthApiController.class)
@ContextConfiguration(
        classes = {
                AuthApiController.class
        }
)
public class AuthApiControllerTest extends APIControllerTest {
    private static final String CONTROLLER_BASEURL = "/api/auth";
    private static final String CONTROLLER_BASEURL_REGISTER = CONTROLLER_BASEURL + "/register";
    private static final String RESOURCE_PREFIX = "/testData/authApiController";

    @MockBean
    private AuthService authService;

    @Override
    protected Stream<StandardTestsArguments> getStandardTestsArgumentProvider() {
        Runnable setup = () -> {
            when(authService.checkUserExists("test@email.com")).thenReturn(false);
        };
        return Stream.of(
                StandardTestsArguments.of(
                        "Register User",
                        setup,
                        post(CONTROLLER_BASEURL_REGISTER).content(readResource("VALID_REQUEST.json", RESOURCE_PREFIX)),
                        Collections.emptyList()
                ));
    }

    @Override
    void negative_unauthorized(String name, Runnable preTest, MockHttpServletRequestBuilder requestBuilder, List<String> authorities) throws Exception {
        //Override to prevents default test behaviour
    }


    @Test
    public void positive_register() throws Exception {
        UUID correlationId = UUID.randomUUID();
        when(authService.checkUserExists("test@email.com")).thenReturn(false);
        this.mockMvc
                .perform(
                        post(CONTROLLER_BASEURL_REGISTER).content(readResource("VALID_REQUEST.json", RESOURCE_PREFIX))
                                .header("x-api-key", TEST_AUTH_TOKEN)
                                .header("CorrelationId", correlationId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is(HttpStatus.CREATED.value()))
                .andExpect(header().string("CorrelationId", correlationId.toString()));
        verify(authService, times(1)).register(eq("test@email.com"), eq("firstName"), eq("lastName"), eq("Password1234!"));
    }

    @Test
    public void negative_user_already_exists() throws Exception {
        UUID correlationId = UUID.randomUUID();
        when(authService.checkUserExists("test@email.com")).thenReturn(true);
        this.mockMvc
                .perform(
                        post(CONTROLLER_BASEURL_REGISTER).content(readResource("VALID_REQUEST.json", RESOURCE_PREFIX))
                                .header("x-api-key", TEST_AUTH_TOKEN)
                                .header("CorrelationId", correlationId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(header().string("CorrelationId", correlationId.toString()))
                .andExpect(content().json(readResource("USER_ALREADY_REGISTERED.json", RESOURCE_PREFIX), true));
    }
}
