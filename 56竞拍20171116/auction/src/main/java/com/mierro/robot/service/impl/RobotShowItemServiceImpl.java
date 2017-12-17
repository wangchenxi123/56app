package com.mierro.robot.service.impl;

import com.alibaba.fastjson.JSON;
import com.mierro.authority.entity.UserMessage;
import com.mierro.common.common.ResponseCode;
import com.mierro.common.common.VerifyException;
import com.mierro.main.entity.SealedBean;
import com.mierro.main.entity.ShowItemBean;
import com.mierro.robot.dao.RobotSealedDao;
import com.mierro.robot.dao.RobotShowItemDao;
import com.mierro.robot.dao.RobotUserMessageDao;
import com.mierro.robot.entity.view.AddRobotShowItemView;
import com.mierro.robot.service.RobotShowItemService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 机器人晒单模块实现
 * Created by tlseek on 2017/8/24.
 */
@Service("robotShowItemService")
public class RobotShowItemServiceImpl implements RobotShowItemService {

    @Resource
    RobotSealedDao robotSealedDao;
    @Resource
    RobotShowItemDao robotShowItemDao;
    @Resource
    RobotUserMessageDao robotUserMessageDao;

    @Override
    public Page<SealedBean> findSealedBeforeShow(String username, String itemName, Boolean front_show, Integer pageNo, Integer pageSize) {
        if (pageNo < 1){
            throw new VerifyException("pageNo参数错误");
        }
        username = username == null ? "%" : "%" + username + "%";
        itemName = itemName == null ? "%" : "%" + itemName + "%";
        Page<SealedBean>  page;
        if (front_show == null) {
            page = robotSealedDao.findAll(itemName, username, new PageRequest(pageNo - 1, pageSize,new Sort(Sort.Direction.DESC,"time")));
        } else {
            page = robotSealedDao.findAll(itemName, username, front_show, new PageRequest(pageNo - 1, pageSize,new Sort(Sort.Direction.DESC,"time")));
        }
        return page;
    }

    @Override
    public void addShowItem(AddRobotShowItemView view) {
        SealedBean sealedBean = ResponseCode.business.notNull(robotSealedDao.findOne(view.getSealedId()), "找不到中奖记录");
        UserMessage um = ResponseCode.business.notNull(robotUserMessageDao.findOne(sealedBean.getUserId()), "找不到中奖记录");
        ResponseCode.business.assertTrue(robotShowItemDao.countByOrderId(view.getSealedId())!=null, "此中奖记录已晒单");
        ShowItemBean showItemBean = new ShowItemBean();
        showItemBean.setUserId(JSON.parseObject(sealedBean.getUser()).getLong("id"));
        showItemBean.setUsername(sealedBean.getName());
        showItemBean.setItemId(sealedBean.getItemId());
        showItemBean.setOrderId(sealedBean.getId());
        showItemBean.setHead_pic(um.getHead_pic());
        showItemBean.setItemName(sealedBean.getItemName());
        showItemBean.setTitle(view.getTitle());
        showItemBean.setContext(view.getContext());
        showItemBean.setGrade(view.getGrade());
        if(view.getPictureList() != null && !view.getPictureList().isEmpty())
            showItemBean.setPictures(JSON.toJSONString(view.getPictureList()));
        showItemBean.setTime(new Date());
        robotShowItemDao.save(showItemBean);
    }
}
