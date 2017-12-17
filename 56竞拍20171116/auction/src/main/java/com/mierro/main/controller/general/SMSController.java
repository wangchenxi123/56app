package com.mierro.main.controller.general;

import com.mierro.common.common.ResponseCode;
import com.mierro.common.common.ResultMessage;
import com.mierro.main.service.SMSService;
import com.mierro.main.util.SMSUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;

/**
 * Created by 黄晓滨 simba on 2017/8/11.
 * Remarks：
 */
@Controller
public class SMSController {

    @Resource
    private SMSService smsService;

    /**
     * 发送验证码(登录操作可用)
     * @param phone 手机号
     * @return 操作码
     * @throws UnsupportedEncodingException
     */
    @ResponseBody
    @RequestMapping(value = "/sendSMS",method = RequestMethod.GET)
    public ResultMessage sendSMS(@RequestParam("phone") String phone) throws UnsupportedEncodingException {
        String message = smsService.send(phone, SMSUtils.SendType.LOGIN);
        return new ResultMessage(ResponseCode.OK,message);
    }

    /**
     * 发送验证码(更新资料操作可用)
     * @param phone 手机号
     * @return 操作码
     * @throws UnsupportedEncodingException
     */
    @ResponseBody
    @RequestMapping(value = "/sendSMS",method = RequestMethod.POST)
    public ResultMessage sendSMSToUpdate(@RequestParam("phone") String phone) throws UnsupportedEncodingException {
        String message = smsService.send(phone, SMSUtils.SendType.UPDATA);
        return new ResultMessage(ResponseCode.OK,message);
    }
}
