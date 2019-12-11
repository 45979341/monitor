package com.zhuzhou.framework.utils.stomp;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES加解密算法
 * 加密用的Key 可以用26个字母和数字组成，最好不要用保留字符
 * 此处使用AES-128-CBC加密模式，key需要为16位
 */
@Slf4j
public class AESUtils {

    /**
     * 加密密钥
     */
    private static final String AES_KEY = "zydfgFyd1d1r1gbo";

    /**
     * 加密
     * @param content
     * @return
     * @throws Exception
     */
    public static String encrypt(String content){
        try{
            SecretKeySpec skeySpec = new SecretKeySpec(AES_KEY.getBytes(),"AES");
            //"算法/模式/补码方式"
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            //使用CBC模式，需要一个向量iv，可增加加密算法的强度
            IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            byte[] encrypted = cipher.doFinal(content.getBytes());
            //此处使用BAES64做转码功能，同时能起到2次加密的作用。
            return new Base64().encodeAsString(encrypted);
        }catch (Exception e){
            log.error(e.getMessage());
            return "";
        }
    }

    /**
     * 解密
     * @param content
     * @return
     * @throws Exception
     */
    public static String decrypt(String content){
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(AES_KEY.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes());
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            //先用bAES64解密
			byte[] encrypted1 = Base64.decodeBase64(content);
            byte[] original = cipher.doFinal(encrypted1);
            return new String(original);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return "";
        }
    }
}
