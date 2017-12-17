package com.mierro.main.service.impl;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mierro.authority.entity.UserMessage;
import com.mierro.common.common.ServiceException;
import com.mierro.common.common.VerifyException;
import com.mierro.main.dao.BiddersDao;
import com.mierro.main.dao.SealedDao;
import com.mierro.main.entity.BiddersBean;
import com.mierro.main.entity.ItemBean;
import com.mierro.main.entity.SealedBean;
import com.mierro.main.service.SealedService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;

/**
 * Created by 黄晓滨 simba on 2017/8/15.
 * Remarks：
 */
@Service("SealedService")
public class SealedServiceImpl implements SealedService {

    @Resource
    private SealedDao sealedDao;
    @Resource
    private BiddersDao biddersDao;

    private ObjectMapper objectMapper = new ObjectMapper();

    private static Logger logger = LogManager.getLogger(SealedService.class.getName());

    @Override
    @Transactional(readOnly = true)
    public Page<SealedBean> findAll(String itemName, Boolean robot, Date stateTime, Date endTime, Integer pageNo, Integer pageSize) {
        if (pageNo < 1){
            throw new VerifyException("pageNo参数错误");
        }

        if (itemName == null){
            itemName = "%";
        }else{
            itemName = "%"+itemName+"%";
        }

        if (stateTime == null){
            stateTime = new Date(0);
        }
        if (endTime == null){
            endTime = new Date();
        }

        Sort sort = new Sort(Sort.Direction.DESC,"id");
        Pageable pageable = new PageRequest(--pageNo,pageSize,sort);
        Page<SealedBean> sealedBeans ;
        if (robot == null){
            sealedBeans =  sealedDao.findAll(itemName,stateTime,endTime,pageable);
        }else{
            sealedBeans =  sealedDao.findAll(itemName,robot,stateTime,endTime,pageable);
        }
        return sealedBeans;
    }

    @Override
    @Transactional(readOnly = true)
    public SealedBean findOne(Long id) {
        SealedBean sealedBean = sealedDao.findOne(id);
        if (sealedBean == null){
            throw new ServiceException("没有找到该记录");
        }
        try {
            if (sealedBean.getUser() != null){
                sealedBean.setUserMessage(objectMapper.readValue(sealedBean.getUser(), UserMessage.class));
            }
            if (sealedBean.getItem()!=null){
                sealedBean.setItemMessage(objectMapper.readValue(sealedBean.getItem(), ItemBean.class));
            }
        } catch (IOException e) {
            logger.error("发生了json转换错误");
            throw new ServiceException("发生了json转换错误");
        }



//        Integer number = biddersDao.findAllNumberBySealedId(id,sealedBean.getUserMessage().getId());
//        if (number == null){
//            sealedBean.setNumber(0);
//        }else{
//            sealedBean.setNumber(number*sealedBean.getItemMessage().getPlus_code());
//        }
        return sealedBean;
    }

    @Override
    @Transactional
    public void add(SealedBean sealedBean) throws JsonProcessingException {
        if (sealedBean.getItemMessage() != null){
            sealedBean.setItem(objectMapper.writeValueAsString(sealedBean.getItemMessage()));
        }
        if (sealedBean.getUserMessage() != null){
            sealedBean.setUser(objectMapper.writeValueAsString(sealedBean.getUserMessage()));
        }
        sealedDao.save(sealedBean);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BiddersBean> findAllBidders(Long sealedId, Integer pageNo, Integer pageSize) {
        if (pageNo < 1){
            throw new VerifyException("pageNo参数错误");
        }
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        Pageable pageable = new PageRequest(--pageNo,pageSize,sort);
        return biddersDao.findAllBySealedId(sealedId,pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SealedBean> findHistory(Long itemId, Integer pageNo, Integer pageSize) {
        if (pageNo < 1){
            throw new VerifyException("pageNo参数错误");
        }
        Sort sort = new Sort(Sort.Direction.DESC,"time");
        Pageable pageable = new PageRequest(--pageNo,pageSize,sort);
        Page<SealedBean> page = sealedDao.findItemId(itemId,pageable);
        return page.map(sealedBean -> {
            ItemBean itemBean = JSON.parseObject(sealedBean.getItem(),ItemBean.class);
            UserMessage userMessage = JSON.parseObject(sealedBean.getUser(),UserMessage.class);
            userMessage.setDisable(null);
            userMessage.setIntegral(null);
            userMessage.setFinal_login(null);
            userMessage.setJoin_date(null);
            sealedBean.setUserMessage(userMessage);
            itemBean.setCost(null);
            itemBean.setControl_line(null);
            itemBean.setRunning_program(null);
            itemBean.setPlus_code(null);
            itemBean.setIncrease_the_price(null);
            itemBean.setFront_show(null);
            itemBean.setNovice(null);
            itemBean.setLabel_str(null);
            itemBean.setBig_picture(null);
            sealedBean.setItemMessage(itemBean);
            sealedBean.setCost(null);
            sealedBean.setIncome(null);
            sealedBean.setProfit(null);
            sealedBean.setUser(null);
            sealedBean.setRobot(null);
            sealedBean.setItem(null);
            return sealedBean;
        });
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SealedBean> findSealedByBidder(Long userId, Integer pageNo, Integer pageSize) {

        if (pageNo < 1){
            throw new VerifyException("pageNo参数错误");
        }
        Sort sort = new Sort(Sort.Direction.DESC,"time");
        Pageable pageable = new PageRequest(--pageNo,pageSize,sort);
        return sealedDao.findSealAndNumberCountByUserId(userId,pageable);
    }
}
