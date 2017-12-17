package com.mierro.main.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mierro.authority.dao.UserMessageDao;
import com.mierro.authority.entity.UserMessage;
import com.mierro.common.common.ServiceException;
import com.mierro.common.common.Tool;
import com.mierro.common.common.VerifyException;
import com.mierro.main.common.OrderState;
import com.mierro.main.dao.OrderDao;
import com.mierro.main.dao.ShowItemDao;
import com.mierro.main.entity.OrderBean;
import com.mierro.main.entity.ShowItemBean;
import com.mierro.main.service.ShowItemService;
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
import java.util.List;

/**
 * Created by 黄晓滨 simba on 2017/7/31.
 * Remarks：用户晒单
 */
@Service("ShowItemService")
public class ShowItemServiceImpl implements ShowItemService {

    @Resource
    private ShowItemDao showItemDao;

    @Resource
    private OrderDao orderDao;

    @Resource
    private UserMessageDao userMessageDao;

    private ObjectMapper objectMapper = new ObjectMapper();

    private static Logger logger = LogManager.getLogger(ShowItemServiceImpl.class.getName());

    /**
     * 添加一条晒单记录
     * @param showItemBean 晒单记录对象
     */
    @Override
    @Transactional
    public void addShowItem(ShowItemBean showItemBean) throws JsonProcessingException {
        OrderBean orderBean = orderDao.findOne(showItemBean.getOrderId());
        orderBean.setOrder_state(OrderState.CONSUMMATION);
        orderDao.save(orderBean);
        UserMessage userMessage = userMessageDao.findOne(showItemBean.getUserId());
        showItemBean.setItemId(orderBean.getItem_id());
        showItemBean.setHead_pic(userMessage.getHead_pic());
        showItemBean.setItemName(orderBean.getItem_name());
        showItemBean.setDisable(false);
        showItemBean.setUsername(userMessage.getUsername());
        ObjectMapper objectMapper = new ObjectMapper();
        if (!showItemBean.getPictureList().isEmpty()){
            showItemBean.setPictures(objectMapper.writeValueAsString(showItemBean.getPictureList()));
        }
        showItemBean.setTime(new Date());
        showItemDao.save(showItemBean);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ShowItemBean> findAll(Long userId, Long itemId, Long orderId, Integer pageNo, Integer pageSize) {
        if (pageNo < 1){
            throw new VerifyException("pageNo参数错误");
        }
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        Pageable pageable = new PageRequest(--pageNo,pageSize,sort);
        Page<ShowItemBean> page ;
        if (orderId == null){
            if (userId == null){
                if (itemId == null){
                    page = showItemDao.findAll(pageable);
                }else{
                    page = showItemDao.findAllByItemId(itemId,pageable);
                }
            }else{
                if (itemId == null){
                    page = showItemDao.findAll(userId,pageable);
                }else{
                    page = showItemDao.findAll(userId,itemId,pageable);
                }
            }
        }else{
            page = showItemDao.findAllByOrderId(orderId,pageable);
        }
        return page.map(showItemBean -> {
            JavaType javaType = Tool.getCollectionType(List.class,String.class);
            if (showItemBean.getPictures() != null){
                try {
                    showItemBean.setPictureList(objectMapper.readValue(showItemBean.getPictures(),javaType));
                } catch (IOException e) {
                    logger.error("json转换错误 showItemId = "+showItemBean.getId()+"\n"+e.getMessage());
                }
            }
            return showItemBean;
        });
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ShowItemBean> findAll(Integer pageNo, Integer pageSize) {
        if (pageNo < 1){
            throw new VerifyException("pageNo参数错误");
        }
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        Pageable pageable = new PageRequest(--pageNo,pageSize,sort);
        Page<ShowItemBean> page = showItemDao.findAll(pageable);

        return page.map(showItemBean -> {
            JavaType javaType = Tool.getCollectionType(List.class,String.class);
            if (showItemBean.getPictures() != null){
                try {
                    showItemBean.setPictureList(objectMapper.readValue(showItemBean.getPictures(),javaType));
                } catch (IOException e) {
                    logger.error("json转换错误 showItemId = "+showItemBean.getId()+"\n"+e.getMessage());
                }
            }
            return showItemBean;
        });
    }

    @Override
    @Transactional
    public void settingShowItemStart(Long showId) {
        ShowItemBean showItemBean = showItemDao.findOne(showId);
        if (showItemBean == null){
            throw new ServiceException("没有找到该晒单");
        }
        if (showItemBean.getDisable() == null){
            showItemBean.setDisable(true);
        }
        showItemBean.setDisable(!showItemBean.getDisable());
        showItemDao.save(showItemBean);
    }
}
