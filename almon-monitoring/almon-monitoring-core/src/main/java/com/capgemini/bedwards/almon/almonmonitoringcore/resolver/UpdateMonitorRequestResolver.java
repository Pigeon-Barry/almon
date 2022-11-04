package com.capgemini.bedwards.almon.almonmonitoringcore.resolver;

import com.capgemini.bedwards.almon.almonmonitoringcore.FormUtil;
import com.capgemini.bedwards.almon.almonmonitoringcore.Monitors;
import com.capgemini.bedwards.almon.almonmonitoringcore.contracts.MonitorAdapter;
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
public class UpdateMonitorRequestResolver implements HandlerMethodArgumentResolver {
    private final Monitors MONITORS;

    public UpdateMonitorRequestResolver(Monitors monitors) {
        this.MONITORS = monitors;
    }


    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.getParameterAnnotation(ConvertUpdateMonitorRequest.class) != null;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {

        ObjectNode jsonRes = FormUtil.getObjectNodeFromRequest(webRequest.getParameterMap());

        MonitorAdapter monitorAdapter = MONITORS.getMonitorAdapterFromId(Objects.requireNonNull(webRequest.getParameterValues("MONITOR_TYPE"))[0]);
        return monitorAdapter.getUpdateMonitorRequestBody(jsonRes);
    }
}
