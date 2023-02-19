package com.capgemini.bedwards.almon.almonaccessapi.controller.services;

import com.capgemini.bedwards.almon.almonaccessapi.controller.APIControllerTest;
import com.capgemini.bedwards.almon.almondatastore.models.service.Service;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = ServicesAPIController.class)
@ContextConfiguration(
        classes = {
                ServicesAPIController.class
        }
)
public class ServicesAPIControllerTest extends APIControllerTest {


    private static final String CONTROLLER_BASEURL = "/api/services";
    private static final String CONTROLLER_BASEURL_CREATE = CONTROLLER_BASEURL + "/create";
    private static final String RESOURCE_PREFIX = "/testData/servicesAPIController";


    @Override
    protected Stream<StandardTestsArguments> getStandardTestsArgumentProvider() {
        Runnable setup = () -> {
        };
        return Stream.of(
                StandardTestsArguments.of(
                        "Create Service",
                        setup,
                        post(CONTROLLER_BASEURL_CREATE).content(readResource("VALID_REQUEST.json", RESOURCE_PREFIX)),
                        Collections.singletonList("CREATE_SERVICE")
                ));
    }

    @Test
    public void positive_create_service() throws Exception {
        addAuthorities("CREATE_SERVICE");
        UUID correlationId = UUID.randomUUID();
        when(serviceService.createService(any(), any(), any())).thenReturn(Service.builder().id("KEY").name("name").description("description").build());
        this.mockMvc
                .perform(
                        post(CONTROLLER_BASEURL_CREATE)
                                .content(readResource("VALID_REQUEST.json", RESOURCE_PREFIX))
                                .header("x-api-key", TEST_AUTH_TOKEN)
                                .header("CorrelationId", correlationId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is(HttpStatus.CREATED.value()))
                .andExpect(header().string("CorrelationId", correlationId.toString()))
                .andExpect(content().json(readResource("VALID_RESPONSE.json", RESOURCE_PREFIX), true));
        verify(serviceService, times(1)).createService(eq("KEY"), eq("name"), eq("description"));
    }
}
