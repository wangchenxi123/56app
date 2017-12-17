package com.mierro.main.Listener.task;

import com.mierro.authority.entity.UserMessage;
import com.mierro.common.common.SpringTool;
import com.mierro.main.dao.*;
import com.mierro.main.service.UserMessageService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Created by 黄晓滨 simba on 2017/8/16.
 * Remarks：
 */
@Component
public class Task {

    private static Logger logger = LogManager.getLogger(Task.class.getName());

    /**
     * 签到重置
     */
    public void reportedReset(){
        IntegralDao integralDao = (IntegralDao) SpringTool.getBeanByClass(IntegralDao.class);
        UserMessageService userMessageService = (UserMessageService) SpringTool.getBeanByClass(UserMessageService.class);
        List<UserMessage> userMessages = integralDao.reportedReset();
        for (UserMessage userMessage : userMessages){
            Instant instant = userMessage.getReportedTime().toInstant();
            ZoneId zone = ZoneId.systemDefault();
            LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
            LocalDate localDate = localDateTime.toLocalDate();
            Long difference = localDate.until(LocalDate.now(), ChronoUnit.DAYS);
            if (difference > 1L){
                userMessageService.reportedReset(userMessage.getId());
            }
        }
        logger.info("/------------------------"+new Date()+"执行了一次用户签到重置操作(完成)--------------------------/ ");
    }


    /**
     * 定时释放内存，防止堆栈溢出
     */
    public void gc(){
        System.gc();
    }
}
