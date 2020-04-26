package org.digitalmind.buildingblocks.core.configutils.service;

import org.digitalmind.buildingblocks.core.configutils.entity.Configuration;
import org.digitalmind.buildingblocks.core.jpaauditor.entity.extension.Parameter;

public interface ConfigUtilService {


    Configuration getById(Long id);

    Configuration save(Configuration configuration);

    Configuration findByModuleAndSection(String module, String section);


    Parameter getParameter(String module, String section, String name);

    <T> T getParameter(String module, String section, String name, Class<T> clazz);

    <T> T getParameter(String module, String section, String name, Class<T> clazz, T value);
}
