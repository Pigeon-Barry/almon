package com.capgemini.bedwards.almon.almonmonitoringcore.convertor;

import com.capgemini.bedwards.almon.almonmonitoringcore.Monitors;
import com.capgemini.bedwards.almon.almonmonitoringcore.contracts.MonitorAdapter;
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
                MonitorAdapterConvertor.class
        }
)
public class MonitorAdapterConvertorTest {

    @Autowired
    private MonitorAdapterConvertor convertor;
    @MockBean
    private Monitors monitors;

    protected static Stream<Arguments> positiveProvider() {
        return Stream.of(
                arguments((String) null),
                arguments("HEALTH"),
                arguments("PASSIVE")
        );
    }

    @ParameterizedTest
    @MethodSource("positiveProvider")
    public void positive_convert(String source) {
        MonitorAdapter<?, ?> monitorAdapter = mock(MonitorAdapter.class);
        doReturn(monitorAdapter).when(monitors).getMonitorAdapterFromId(eq(source));
        assertEquals(monitorAdapter, convertor.convert(source));

        verify(monitors, times(1)).getMonitorAdapterFromId(eq(source));
    }

}
