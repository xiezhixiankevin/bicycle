package com.shixun.bicycle.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * <Description> ApplicationContextProvider
 *
 * @author 26802
 * @version 1.0
 * @see com.shixun.bicycle.utils
 */
@Component
public class ApplicationContextProvider implements ApplicationContextAware {
    private static ApplicationContext applicationContextSpring;

    @Override
    public synchronized void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        applicationContextSpring = applicationContext;
    }

    /**
     * 通过class 获取Bean
     */
    public static <T> T getBean(Class<T> clazz) {
        return applicationContextSpring.getBean(clazz);
    }
}