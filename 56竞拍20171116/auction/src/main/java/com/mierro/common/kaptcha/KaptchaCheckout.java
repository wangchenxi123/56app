package com.mierro.common.kaptcha;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by 黄晓滨 simba on 2017/5/20.
 * Remarks：图片验证码校验
 */
public class KaptchaCheckout {
    public static Boolean checkout(HttpServletRequest request,String code){
        String kaptchaExpected = (String) request.getSession().getAttribute(
                com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
        return code.equals(kaptchaExpected);
    }
}
