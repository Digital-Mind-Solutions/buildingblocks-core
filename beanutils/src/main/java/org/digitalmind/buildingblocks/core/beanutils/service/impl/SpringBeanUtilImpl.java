package org.digitalmind.buildingblocks.core.beanutils.service.impl;

import org.digitalmind.buildingblocks.core.beanutils.service.SpringBeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SpringBeanUtilImpl implements SpringBeanUtil {

    private final DefaultListableBeanFactory beanFactory;
    private final ApplicationContext context;

    public SpringBeanUtilImpl(DefaultListableBeanFactory beanFactory, ApplicationContext context) {
        this.beanFactory = beanFactory;
        this.context = context;
    }

    @Override
    public void registerBean(Object bean, String name) {
        registerBean(bean, name, (String[]) null);
    }

    @Override
    public void registerBean(Object bean, String name, String... aliases) {
        beanFactory.initializeBean(bean, name);
        beanFactory.registerSingleton(name, bean);
        if (aliases != null) {
            for (String alias : aliases) {
                beanFactory.registerAlias(name, alias);
            }
        }
    }

    @Override
    public Object getBean(String name) {
        return context.getBean(name);
    }

    @Override
    public <T> T  getBean(Class<T> type) {
        return context.getBean(type);
    }

    @Override
    public <T> T getBean(String name, Class<T> type) {
        return (T) context.getBean(name);
    }

}
