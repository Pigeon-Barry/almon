package com.capgemini.bedwards.almon.almonmonitoringcore.convertor;

import com.capgemini.bedwards.almon.almondatastore.models.schedule.ScheduledMonitor;
import com.capgemini.bedwards.almon.almonmonitoringcore.service.monitor.ScheduledMonitorServiceImpl;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(
        classes = {
                ScheduledMonitorConvertor.class
        }
)
public class ScheduledMonitorConvertorTest {
    @Autowired
    private ScheduledMonitorConvertor convertor;

    @MockBean(name = "scheduledMonitorServiceImpl")
    private ScheduledMonitorServiceImpl service;

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
        ScheduledMonitor expected = mock(ScheduledMonitor.class);
        doReturn(expected).when(service).getMonitorFromCombinedId(eq(source));
        assertEquals(expected, convertor.convert(source));

        verify(service, times(1)).getMonitorFromCombinedId(eq(source));
    }
}
