package com.mierro.authority.service.impl;

import com.mierro.authority.common.LoginType;
import com.mierro.authority.dao.AuthenticationInfoDao;
import com.mierro.authority.dao.FunctionDao;
import com.mierro.authority.dao.OperationRecordDao;
import com.mierro.authority.entity.AuthenticationInfo;
import com.mierro.authority.entity.Authority;
import com.mierro.authority.entity.OperationRecord;
import com.mierro.authority.service.OperationRecordService;
import com.mierro.authority.shiro.UrlComposingRoom;
import com.mierro.common.common.Abnormal_IP;
import com.mierro.common.common.Tool;
import com.mierro.common.common.VerifyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by 黄晓滨 simba on 2017/2/21.
 * Remarks：
 */
@Service("OperationRecordService")
public class OperationRecordServiceImpl implements OperationRecordService {

    @Resource
    private FunctionDao functionDao;
    @Resource
    private AuthenticationInfoDao authenticationInfoDao;
    @Resource
    private OperationRecordDao operationRecordDao;

    private  SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//24小时制

    @Override
    @Transactional
    public void addOperationRecord(Long userId, HttpServletRequest request ,HttpServletResponse response) {
        //获取真实ip
        String ip = Tool.getRealIp(request);
        if (userId == null) {
            //异常操作，记录违规ip
            Abnormal_IP.recordIp(ip);
        }
        //创建操作记录对象
        OperationRecord operationRecord = new OperationRecord();
        operationRecord.setUserId(userId.toString());
        operationRecord.setOperationTime(new Date());
        operationRecord.setIp(ip);
        List<AuthenticationInfo> lists = authenticationInfoDao.findUsernameAndIphoneByUserId(userId);
        for (AuthenticationInfo authenticationInfo : lists) {
            if (authenticationInfo.getVoucherType().equals(LoginType.ACCOUNT)) {
                operationRecord.setUsername(authenticationInfo.getIdentifier());
            }
            if (authenticationInfo.getVoucherType().equals(LoginType.TELEPHONE)) {
                operationRecord.setIphone(authenticationInfo.getIdentifier());
            }
        }
        if (operationRecord.getIphone() == null){
            operationRecord.setIphone("null");
        }
        //获取前端传入的所有参数
        Map<String,String> map = new HashMap<>();
        Enumeration paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = (String) paramNames.nextElement();
            String[] paramValues = request.getParameterValues(paramName);
            if (paramValues.length == 1) {
                String paramValue = paramValues[0];
                if (paramValue.length() != 0) {
                    map.put(paramName, paramValue);
                }
            }
        }
        Set<Map.Entry<String, String>> set = map.entrySet();
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry entry : set) {
            stringBuilder.append(entry.getKey()).append(":").append(entry.getValue()).append(",");
        }
        String url = request.getRequestURI();
        url = UrlComposingRoom.getUrl(request,url);
        Authority authority = functionDao.findByUrlStr(url);
        if (authority != null){
            operationRecord.setMessage("管理员 "+userId+" 操作了接口 ("+authority.getDescription()+
                    "),操作类型为 :"+authority.getRequestMethod()+",操作状态：操作成功 ,传入参数为("+stringBuilder.toString()+")");
            operationRecordDao.save(operationRecord);
        }
    }

    @Override
    @Transactional
    public void delete(List<OperationRecord> list) {
        operationRecordDao.delete(list);
    }

}
