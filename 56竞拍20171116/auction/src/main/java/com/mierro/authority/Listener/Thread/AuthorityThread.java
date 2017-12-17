package com.mierro.authority.Listener.Thread;


import com.mierro.authority.Listener.AuthorityInitSerivce.AuthorityInitSerivce;
import com.mierro.common.common.ServiceException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by 黄晓滨(Simba) on 2017/4/1.
 *
 * @message: 维护线程，保证管理员拥有所有权限
 */
public class AuthorityThread extends Thread{

    /**
     * 默认睡眠时间
     */
    private Integer sleepTime = 3600000;
    /**
     * 标识位
     */
    private Integer sleep;

    private AuthorityInitSerivce authorityInitSerivce;

    public AuthorityThread(AuthorityInitSerivce authorityInitSerivce) {
        this.authorityInitSerivce = authorityInitSerivce;
    }

    public void run() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        //计算睡眠时间
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, +1);
        String dateString = simpleDateFormat.format(calendar.getTime());
        Date date;
        try {
            date = simpleDateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new ServiceException("时间转换失败");
        }
        //获取当前时间距离当天24点整毫秒数
        long time = date.getTime() - new Date().getTime();
        this.sleep = (int) time;
        while (true) {
            //检测是否有基础权限控制权限记录
            if (authorityInitSerivce.detectionPermissionSet()){
                //添加基础权限控制记录
                authorityInitSerivce.addAuthority();
            }
            //判断管理员是否存在
            if (!authorityInitSerivce.exits()) {
                //重新创建超级管理员
                authorityInitSerivce.addSuperAdmin();
            } else {
                //刷新管理员权限
                authorityInitSerivce.addSuperAuthority();
            }

            //进入睡眠
            try {
                if (sleep == null) {
                    Thread.sleep(sleepTime);
                } else {
                    Thread.sleep(sleep);
                    sleep = null;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
