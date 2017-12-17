package com.mierro.main.service.impl;


import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.mierro.common.common.StringBuilderUtils;
import com.mierro.main.service.SMSService;
import com.mierro.main.service.SystemNoticeService;
import com.mierro.main.util.SMSUtils;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

/**
 * Created by 黄晓滨 simba on 2017/8/14.
 * Remarks：
 */
@Service
public class SMSServiceImpl implements SMSService {

    private SystemNoticeService systemNoticeService;


    @Override
    public String send(String telephone,SMSUtils.SendType sendType) throws UnsupportedEncodingException {
        String code = StringBuilderUtils.getRandomNumberString(6);
        SendSmsResponse back;
        try {
            back = SMSUtils.SendMessage(telephone,code,sendType);
        } catch (ClientException e) {
            systemNoticeService.add("系统异常通知(短信发送异常)",
                    ""+e.getMessage());
            return "发送失败";
        }
        if (back == null){
            return "发送失败，距离上次发送没有超过1分钟时间";
        }
        if(back.getCode().equals("OK")){
            return "发送成功";
        }else{
            return "发送失败";
        }
    }
}
