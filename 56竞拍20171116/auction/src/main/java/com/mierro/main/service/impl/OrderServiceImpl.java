package com.mierro.main.service.impl;

import com.mierro.common.common.ServiceException;
import com.mierro.common.common.VerifyException;
import com.mierro.main.common.ItemType;
import com.mierro.main.common.OrderState;
import com.mierro.main.dao.AddressDao;
import com.mierro.main.dao.ItemDao;
import com.mierro.main.dao.OrderDao;
import com.mierro.main.entity.AddressBean;
import com.mierro.main.entity.ItemBean;
import com.mierro.main.entity.OrderBean;
import com.mierro.main.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by 黄晓滨 simba on 2017/8/14.
 * Remarks：
 */
@Service("OrderService")
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderDao orderDao;
    @Resource
    private AddressDao addressDao;
    @Resource
    private ItemDao itemDao;


    @Override
    @Transactional(readOnly = true)
    public Page<OrderBean> findAll(String itemName, OrderState orderState, ItemType itemType, Boolean robot, Integer pageNo, Integer pageSize) {
        if (pageNo<1){
            throw new VerifyException("pageNo参数错误");
        }
        Sort sort = new Sort(Sort.Direction.DESC,"id")
                .and(new Sort(Sort.Direction.DESC,"order_state"));
        Pageable pageable = new PageRequest(--pageNo,pageSize,sort);
        if (itemName == null){
            itemName = "%";
        }else{
            itemName = "%"+itemName+"%";
        }
        Page<OrderBean> orderBeans;
        if (orderState == null){
            if (itemType == null){
                if (robot == null){
                    orderBeans = orderDao.findAll(itemName,pageable);
                }else{
                    orderBeans = orderDao.findAll(itemName,robot,pageable);
                }
            }else{
                if (robot == null){
                    orderBeans = orderDao.findAll(itemName,itemType,pageable);
                }else{
                    orderBeans = orderDao.findAll(itemName,itemType,robot,pageable);
                }
            }
        }else{
            if (itemType == null){
                if (robot == null){
                    orderBeans = orderDao.findAll(itemName,orderState,pageable);
                }else{
                    orderBeans = orderDao.findAll(itemName,orderState,robot,pageable);
                }
            }else{
                if (robot == null){
                    orderBeans = orderDao.findAll(itemName,orderState,itemType,pageable);
                }else{
                    orderBeans = orderDao.findAll(itemName,orderState,itemType,robot,pageable);
                }
            }
        }
        return orderBeans;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderBean> ClientFindAll(Integer pageNo, Integer pageSize) {
        if (pageNo<1){
            throw new VerifyException("pageNo参数错误");
        }
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        Pageable pageable = new PageRequest(--pageNo,pageSize,sort);
        return orderDao.ClientFindAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderBean findOne(Long orderId) {
        OrderBean orderBean = orderDao.findOne(orderId);
        if (orderBean == null){
            throw new VerifyException("找不到该订单，请检查传入订单id");
        }
        if (orderBean.getReceipt_id() != null){
            orderBean.setAddressBean(addressDao.findOne(orderBean.getReceipt_id()));
        }
        return orderBean;
    }

    @Override
    @Transactional
    public void sign_up_order(Long orderId) {
        OrderBean orderBean = orderDao.findOne(orderId);
        if (orderBean == null){
            throw new VerifyException("没有找到该订单");
        }
        if (!orderBean.getOrder_state().equals(OrderState.WAITING_RECEIPT)){
            throw new VerifyException("订单状态不予许修改");
        }
        orderBean.setOrder_state(OrderState.WAITING_SUN_ALONE);
        orderDao.save(orderBean);
    }

    @Override
    @Transactional
    public void update(Long orderId, String express_number, String card, String density) {
        OrderBean orderBean = orderDao.findOne(orderId);
        if(!orderBean.getOrder_state().equals(OrderState.WAITING_SHIP)){
            throw new VerifyException("订单状态不符合");
        }
        if (orderBean == null){
            throw new VerifyException("找不到该订单，请检查传入订单id");
        }
        if (express_number == null && card ==null && density == null){
            throw new VerifyException("快递编号或者卡号卡密必须传入其中一种");
        }
        if (express_number == null && (card ==null || density == null)){
            throw new VerifyException("卡号，卡密必须同时传入");
        }

        if (express_number != null){
            orderBean.setExpress_number(express_number);
            orderBean.setOrder_state(OrderState.WAITING_RECEIPT);
        }else{
            orderBean.setCard(card);
            orderBean.setDensity(density);
            orderBean.setOrder_state(OrderState.WAITING_SUN_ALONE);
        }
        orderBean.setShipTime(new Date());
        orderDao.save(orderBean);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderBean> clientFindAll(Long userId, String item_name, Integer pageNo, Integer pageSize) {
        if (pageNo<1){
            throw new VerifyException("pageNo参数错误");
        }
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        Pageable pageable = new PageRequest(--pageNo,pageSize,sort);
        if (item_name == null){
            item_name = "%";
        }else{
            item_name = "%"+item_name+"%";
        }
        return orderDao.findAll(userId,item_name,pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public AddressBean findAddress(Long orderId) {
        OrderBean orderBean = orderDao.findOne(orderId);
        if (orderBean == null){
            throw new VerifyException("找不到该订单，请检查传入订单id");
        }
        if (orderBean.getReceipt_id() != null){
            orderBean.setAddressBean(addressDao.findOne(orderBean.getReceipt_id()));
        }else{
            throw new VerifyException("该订单没有设置收货地址");
        }
        return orderBean.getAddressBean();
    }


}
