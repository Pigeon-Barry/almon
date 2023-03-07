package com.capgemini.bedwards.almon.almonmonitoringcore.convertor;

import com.capgemini.bedwards.almon.almondatastore.models.alert.Alert;
import com.capgemini.bedwards.almon.almonmonitoringcore.service.alert.AlertServiceImpl;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(
        classes = {
                AlertConvertor.class
        }
)
public class AlertConvertorTest {

    @Autowired
    private AlertConvertor convertor;

    @MockBean(name = "alertServiceImpl")
    private AlertServiceImpl alertServiceImpl;

    protected static Stream<Arguments> positiveProvider() {
        return Stream.of(
                arguments((UUID) null),
                arguments(UUID.randomUUID()),
                arguments(UUID.randomUUID())
        );
    }

    @ParameterizedTest
    @MethodSource("positiveProvider")
    public void positive_convert(UUID source) {
        Alert<?> alert = mock(Alert.class);
        doReturn(alert).when(alertServiceImpl).getAlertFromId(eq(source));
        assertEquals(alert, convertor.convert(source));

        verify(alertServiceImpl, times(1)).getAlertFromId(eq(source));
    }

}