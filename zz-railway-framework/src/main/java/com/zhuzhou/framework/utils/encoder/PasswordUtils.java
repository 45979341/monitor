package com.zhuzhou.framework.utils.encoder;

import org.apache.commons.lang3.RandomStringUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 密码加盐工具
 */
public class PasswordUtils {

    private static final char[] DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * 生成盐值
     * @return
     */
    public static String generateSalt(){
        return RandomStringUtils.randomAlphanumeric(32);
    }

    /**
     * 盐值加密
     * @param source
     * @param salt
     * @return
     */
    public static String encrypt(String source, String salt){
        byte[] hash = hash(source.getBytes(), salt.getBytes(), 2);
        if (hash != null) {
            return encode(hash);
        }
        return "";
    }

    /**
     * MD5加密
     * @param source
     * @param salt
     * @param hashIterations
     * @return
     */
    private static byte[] hash(byte[] source, byte[] salt, int hashIterations) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        if (digest != null) {
            digest.reset();
            digest.update(salt);
            byte[] hashed = digest.digest(source);
            int iterations = hashIterations - 1;
            for (int i = 0; i < iterations; i++) {
                digest.reset();
                hashed = digest.digest(hashed);
            }
            return hashed;
        }
        return null;
    }

    /**
     * 字符串编码
     * @param data
     * @return
     */
    private static String encode(byte[] data) {
        int l = data.length;
        char[] out = new char[l << 1];
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = DIGITS[(0xF0 & data[i]) >>> 4];
            out[j++] = DIGITS[0x0F & data[i]];
        }
        return new String(out);
    }
}
