package com.mierro.main.service;

import com.mierro.main.util.SMSUtils;

import java.io.UnsupportedEncodingException;

/**
 * Created by 黄晓滨 simba on 2017/8/14.
 * Remarks：
 */
public interface SMSService {

    String send(String telephone, SMSUtils.SendType sendType) throws UnsupportedEncodingException;
}
