package com.mierro.common.common;

import java.util.Random;
import java.util.UUID;

/**
 * <p>Title:StringBuilderUtils </p>
 * <p>Description:字符串生成器 </p>
 * <p>Company:Wteam </p> 
 *  @author Wteam 黄晓滨 86571705@qq.com
 *  @date 2016年4月12日 下午9:27:40
 */
public final class StringBuilderUtils {
	
	/**
	 * 生成随机字符串
	 * @param length
	 * 			随机字符串长度
	 * @return
	 * 			随机字符串
	 */
	public static final String getRandomString (int length) {
		if(length < 1) {
			return null;
		}
		StringBuffer buffer = new StringBuffer("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");  
		StringBuffer sb = new StringBuffer();   
        Random random = new Random();
        int range = buffer.length();   
        for (int i = 0; i < length; i ++) {   
            sb.append(buffer.charAt(random.nextInt(range)));   
        }
		return sb.toString();
	}
	/**
	 * 生成随机字符串
	 * @param length
	 * 			随机字符串长度
	 * @return
	 * 			随机字符串
	 */
	public static final String getRandomNumberString (int length) {
		if(length < 1) {
			return null;
		}
		StringBuffer buffer = new StringBuffer("0123456789");  
		StringBuffer sb = new StringBuffer();   
       Random random = new Random();   
       int range = buffer.length();   
       for (int i = 0; i < length; i ++) {   
           sb.append(buffer.charAt(random.nextInt(range)));   
       }
		return sb.toString();
	}
	
	/**
	 * 生成UUID字符串
	 * @return
	 * 			UUID字符串
	 */
	public static final String getUUIDString() {
		return UUID.randomUUID().toString();
	}
}
