package com.ronhe.romp.oauth2.core.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * <p>description goes here</p>
 *
 * @author 冷澳
 * @date 2022/8/16
 */

@Component
public class SpringContextUtils implements ApplicationContextAware {

    /**
     * 上下文对象实例
     */
    private  ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 获取applicationContext
     */
    public  ApplicationContext getApplicationContext() {
        //判断是否为null
        if (applicationContext == null) {
            throw new IllegalStateException("applicationContext未注入.");
        }
        return applicationContext;
    }

    /**
     * 通过name获取Bean
     */
    public  Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }

    /**
     * 通过class获取Bean
     */
    public  <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    /**
     * 通过name和class获取Bean
     */
    public  <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }

}
