package org.digitalmind.buildingblocks.core.beanutils.service;

public interface SpringBeanUtil {

    void registerBean(Object bean, String name);

    void registerBean(Object bean, String name, String... aliases);

    Object getBean(String name);

    //Object getBean(Class clazz);
    <T> T getBean(Class<T> type);

    <T> T getBean(String name, Class<T> type);

}
