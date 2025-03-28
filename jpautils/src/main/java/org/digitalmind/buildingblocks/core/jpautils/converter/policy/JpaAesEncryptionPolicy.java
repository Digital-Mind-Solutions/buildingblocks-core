package org.digitalmind.buildingblocks.core.jpautils.converter.policy;

import org.digitalmind.buildingblocks.core.jpautils.converter.policy.base.JpaAbstractEncryptionPolicy;
import org.digitalmind.buildingblocks.core.jpautils.converter.exception.JpaEncryptionConverterException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class JpaAesEncryptionPolicy implements JpaAbstractEncryptionPolicy {

    private static final String ALGORITHM = "AES/ECB/PKCS5Padding";
    private final String key;
    private final String algorithm;

    public JpaAesEncryptionPolicy(String key, String algorithm) {
        this.key = key;
        this.algorithm = algorithm;
    }

    @Override
    public String encrypt(String data) throws JpaEncryptionConverterException {
        if (data == null) return null;
        try {
            Key secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), algorithm);
            Cipher cipher = null;

            cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            throw new JpaEncryptionConverterException(e);
        } catch (NoSuchPaddingException e) {
            throw new JpaEncryptionConverterException(e);
        } catch (IllegalBlockSizeException e) {
            throw new JpaEncryptionConverterException(e);
        } catch (BadPaddingException e) {
            throw new JpaEncryptionConverterException(e);
        } catch (InvalidKeyException e) {
            throw new JpaEncryptionConverterException(e);
        }
    }

    @Override
    public String decrypt(String data) throws JpaEncryptionConverterException {
        if (data == null) return null;
        try {
            Key secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), algorithm);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(data)), StandardCharsets.UTF_8);
        } catch (NoSuchAlgorithmException e) {
            throw new JpaEncryptionConverterException(e);
        } catch (NoSuchPaddingException e) {
            throw new JpaEncryptionConverterException(e);
        } catch (IllegalBlockSizeException e) {
            throw new JpaEncryptionConverterException(e);
        } catch (BadPaddingException e) {
            throw new JpaEncryptionConverterException(e);
        } catch (InvalidKeyException e) {
            throw new JpaEncryptionConverterException(e);
        }
    }

}
