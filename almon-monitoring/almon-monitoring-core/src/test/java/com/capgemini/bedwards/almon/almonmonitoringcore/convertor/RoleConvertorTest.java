package com.capgemini.bedwards.almon.almonmonitoringcore.convertor;

import com.capgemini.bedwards.almon.almoncore.exceptions.NotFoundException;
import com.capgemini.bedwards.almon.almoncore.services.user.RoleService;
import com.capgemini.bedwards.almon.almondatastore.models.auth.Role;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(
        classes = {
                RoleConvertor.class
        }
)
public class RoleConvertorTest {
    @Autowired
    private RoleConvertor convertor;

    @MockBean
    private RoleService service;

    protected static Stream<Arguments> positiveProvider() {
        return Stream.of(
                arguments((String) null),
                arguments("PASSIVE"),
                arguments("HEALTH")
        );
    }

    @ParameterizedTest
    @MethodSource("positiveProvider")
    public void positive_convert(String source) {
        Role expected = mock(Role.class);
        Optional<Role> optional = Optional.of(expected);
        doReturn(optional).when(service).getRoleFromName(eq(source));
        assertEquals(expected, convertor.convert(source));

        verify(service, times(1)).getRoleFromName(eq(source));
    }

    @ParameterizedTest
    @MethodSource("positiveProvider")
    public void negative_convert_not_found(String source) {
        Optional<Role> optional = Optional.empty();
        doReturn(optional).when(service).getRoleFromName(eq(source));
        assertThrows(NotFoundException.class, () -> convertor.convert(source), "Role with name '" + source + "' not found");

        verify(service, times(1)).getRoleFromName(eq(source));
    }
}
