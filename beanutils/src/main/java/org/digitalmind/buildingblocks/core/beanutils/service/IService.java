package org.digitalmind.buildingblocks.core.beanutils.service;

import org.springframework.util.StringUtils;

public interface IService {

    default String getBeanServiceName() {
        String beanServiceName = this.getClass().getSimpleName();
        if (beanServiceName.endsWith("Impl")) {
            beanServiceName = beanServiceName.substring(0, beanServiceName.length() - 4);
        }
        int i = beanServiceName.indexOf("$");
        if (i > 0) {
            beanServiceName = beanServiceName.substring(0, i);
        }
        beanServiceName = StringUtils.capitalize(beanServiceName);

        return beanServiceName;
    }

}
