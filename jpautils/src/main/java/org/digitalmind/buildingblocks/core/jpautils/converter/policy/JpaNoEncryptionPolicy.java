package org.digitalmind.buildingblocks.core.jpautils.converter.policy;

import org.digitalmind.buildingblocks.core.jpautils.converter.policy.base.JpaAbstractEncryptionPolicy;
import org.digitalmind.buildingblocks.core.jpautils.converter.exception.JpaEncryptionConverterException;

public class JpaNoEncryptionPolicy implements JpaAbstractEncryptionPolicy {

    public static final JpaNoEncryptionPolicy INSTANCE = new JpaNoEncryptionPolicy();

    public JpaNoEncryptionPolicy() {
    }

    @Override
    public String encrypt(String data) throws JpaEncryptionConverterException {
        return data;
    }

    @Override
    public String decrypt(String data) throws JpaEncryptionConverterException {
        return data;
    }

}
