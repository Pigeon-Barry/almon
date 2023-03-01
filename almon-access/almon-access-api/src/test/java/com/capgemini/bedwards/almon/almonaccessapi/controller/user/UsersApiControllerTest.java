package com.capgemini.bedwards.almon.almonaccessapi.controller.user;


import com.capgemini.bedwards.almon.almonaccessapi.controller.APIControllerTest;
import com.capgemini.bedwards.almon.almondatastore.models.auth.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;
import java.util.stream.Stream;

import static com.capgemini.bedwards.almon.almontest.helpers.TestResourceUtil.readResource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = UsersApiController.class)
@ContextConfiguration(
        classes = {
                UsersApiController.class
        }
)
public class UsersApiControllerTest extends APIControllerTest {
        private static final String CONTROLLER_BASEURL = "/api/users";
        private static final String RESOURCE_PREFIX = "/testData/usersApiController";


        @MockBean
        private Page<User> userPage;

        @Override
        protected Stream<StandardTestsArguments> getStandardTestsArgumentProvider() {
                Runnable setup = () -> {
                };
                return Stream.of(
                        StandardTestsArguments.of(
                                "Get Users",
                                setup,
                                get(CONTROLLER_BASEURL),
                                Collections.singletonList("VIEW_ALL_USERS")
                        ));
        }

        @Test
        public void positive_get_user() throws Exception {
                addAuthorities("VIEW_ALL_USERS");
                User user1 = setupValidUser(UUID.fromString("4d7b0d54-cdae-4253-bbd6-d95f6ae69113"));
                User user2 = setupValidUser(UUID.fromString("4d7b0d54-cdae-4253-bbd6-d95f6ae69114"));
                User user3 = setupValidUser(UUID.fromString("4d7b0d54-cdae-4253-bbd6-d95f6ae69115"));

                when(userPage.getContent()).thenReturn(new ArrayList<User>() {{
                        add(user1);
                        add(user2);
                        add(user3);
                }});
                when(userService.findPaginatedWithFilter(anyInt(), anyInt(), any())).thenReturn(userPage);


                UUID correlationId = UUID.randomUUID();
                this.mockMvc
                        .perform(
                                get(CONTROLLER_BASEURL)
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
