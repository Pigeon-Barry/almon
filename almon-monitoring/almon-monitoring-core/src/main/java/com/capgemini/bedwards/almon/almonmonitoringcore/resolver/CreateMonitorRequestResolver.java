package com.capgemini.bedwards.almon.almonmonitoringcore.resolver;

import com.capgemini.bedwards.almon.almonmonitoringcore.Monitors;
import com.capgemini.bedwards.almon.almonmonitoringcore.contracts.MonitorAdapter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Objects;

@Component
@Slf4j
public class CreateMonitorRequestResolver implements HandlerMethodArgumentResolver {


    private final Monitors MONITORS;

    public CreateMonitorRequestResolver(Monitors monitors) {
        this.MONITORS = monitors;
    }


    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.getParameterAnnotation(ConvertCreateMonitorRequest.class) != null;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode jsonRes = mapper.createObjectNode();

        webRequest.getParameterMap().forEach((s, strings) -> {
            String[] parts = s.split("\\.");
            ObjectNode node = jsonRes;
            for (int index = 0; index < parts.length; index++) {
                String name = parts[index];
                log.info(name + " - " + index);
                if (index < parts.length - 1) {
                    if (!node.has(name))
                        node.set(name, mapper.createObjectNode());
                    node = (ObjectNode) node.get(name);
                } else {
                    node.put(name, strings[0]);
                }
            }
        });
        if (log.isDebugEnabled())
            log.debug("Json: " + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonRes));

        MonitorAdapter monitorAdapter = MONITORS.getMonitorAdapterFromId(Objects.requireNonNull(webRequest.getParameterValues("MONITOR_TYPE"))[0]);
        return monitorAdapter.getCreateMonitorRequestBody(jsonRes);
    }
}
