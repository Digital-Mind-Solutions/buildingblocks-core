package org.digitalmind.buildingblocks.core.jpautils.converter.policy.base;

import org.digitalmind.buildingblocks.core.jpautils.converter.exception.JpaEncryptionConverterException;

public interface JpaAbstractEncryptionPolicy {
    String encrypt(String data) throws JpaEncryptionConverterException;

    String decrypt(String data) throws JpaEncryptionConverterException;

}
