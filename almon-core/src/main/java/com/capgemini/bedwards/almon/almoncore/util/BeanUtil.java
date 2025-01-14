package com.capgemini.bedwards.almon.almoncore.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class BeanUtil {

    @Autowired
    private static ApplicationContext applicationContext;

    public static void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (BeanUtil.applicationContext == null)
            BeanUtil.applicationContext = applicationContext;
    }

    public static <T> T getBeanOfClass(Class<T> _class) {
        return applicationContext.getBean(_class);
    }

    public static <T> T getBeanOfName(String beanName, Class<T> _class) {
        return applicationContext.getBean(beanName, _class);
    }
}
