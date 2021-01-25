package com.oc.hawk.common.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Objects;

public class ApplicationContextHolder implements ApplicationContextAware {

    public static final ApplicationContextHolder INSTANCE = new ApplicationContextHolder();
    public static ApplicationContext context;

    private ApplicationContextHolder() {
    }

    public static ApplicationContextHolder getInstance() {
        return INSTANCE;
    }

    public static <T> T getBean(Class <T> cls) {
        if (Objects.nonNull(context)) {
            return context.getBean(cls);
        } else {
            return null;
        }

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

}
