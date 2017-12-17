package com.mierro.authority.common;

import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

/**
 * @author 黄晓滨
 * @date 17/2/9
 */
public class PasswordUtils {

    /**
     * 随机数生成器
     */
    private RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();

    /**
     * 指定散列算法为md5
     */
    public static final String algorithmName = "md5";

    /**
     * 散列迭代次数
     */
    public static final int hashIterations = 2;

    /**
     * Description: 加密账号<br>
     * @param principal 身份(用户的唯一标识符)
     * @param password  未加密密码
     * @return {newPassword, salt}
     * @see
     */
    public String[] encryptPassword(String principal, String password) {
        if (principal == null || password == null) {
            throw new NullPointerException(String.format("principal=%s, password=%s", principal, password) );
        }
        String[] newPassword = new String[2];//{newPassword, salt}
        // 生成随机数
        newPassword[1] = randomNumberGenerator.nextBytes().toHex();
        newPassword[0] = new SimpleHash(
                algorithmName,
                password,
                // 用户的盐: 身份 + 随机数
                ByteSource.Util.bytes(principal + newPassword[1]),
                hashIterations).toHex();
        return newPassword;
    }

    /**
     * Description: 加密账号<br>
     * @param password  未加密密码
     * @return {newPassword, salt}
     * @see
     */
    public String[] encryptPassword(String password) {
        String[] newPassword = new String[2];//{newPassword, salt}
        // 生成随机数
        newPassword[1] = randomNumberGenerator.nextBytes().toHex();
        newPassword[0] = new SimpleHash(
                algorithmName,
                password,
                // 用户的盐: 身份 + 随机数
                ByteSource.Util.bytes(newPassword[1]),
                hashIterations).toHex();
        return newPassword;
    }

}
