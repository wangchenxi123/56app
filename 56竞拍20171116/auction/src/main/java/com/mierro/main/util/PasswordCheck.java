package com.mierro.main.util;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by 黄晓滨 simba on 2017/6/12.
 * Remarks：密码校验
 */
public class PasswordCheck {

    /**
     * 16进制字符
     */
    private static final String[] HEX_DIGITS = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
            "a", "b", "c", "d", "e", "f"};

    public static String check(String password,String salt){
        return new SimpleHash(
                "md5",
                password,
                ByteSource.Util.bytes(salt),
                2).toHex();
    }

    /**
     * md5加密算法.
     *
     * @param plainText 加密原文
     * @return 加密后密文
     */
    public static String md5(final String plainText) {
        byte[] secretBytes = null;
        try {
            secretBytes = MessageDigest.getInstance("md5").digest(plainText.getBytes());
        } catch (final NoSuchAlgorithmException e) {
            throw new RuntimeException("登陆出现错误！");
        }
        return byteArrayToHexString(secretBytes);
    }

    /**
     * 转换字节数组为16进制字串
     *
     * @param b 字节数组
     * @return 16进制字串
     */
    public static String byteArrayToHexString(byte[] b) {
        StringBuilder resultSb = new StringBuilder();
        for (byte aB : b) {
            resultSb.append(byteToHexString(aB));
        }
        return resultSb.toString();
    }

    /**
     * 转换byte到16进制
     *
     * @param b 要转换的byte
     * @return 16进制格式
     */
    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n = 256 + n;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return HEX_DIGITS[d1] + HEX_DIGITS[d2];
    }
}
