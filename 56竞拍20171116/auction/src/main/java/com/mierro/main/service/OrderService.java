package com.mierro.main.service;

import com.mierro.main.common.ItemType;
import com.mierro.main.common.OrderState;
import com.mierro.main.entity.AddressBean;
import com.mierro.main.entity.OrderBean;
import org.springframework.data.domain.Page;

/**
 * Created by 黄晓滨 simba on 2017/8/14.
 * Remarks：
 */

public interface OrderService {

    /**
     * 根据条件查询中奖人
     * @param itemName 商品名称
     * @param orderType 订单状态
     * @param itemType 商品类型
     * @param robot 是否机器人
     * @param pageNo 页码
     * @param pageSize 每页数量
     * @return 分页对象
     */
    Page<OrderBean> findAll(String itemName, OrderState orderType, ItemType itemType, Boolean robot, Integer pageNo, Integer pageSize);

    /**
     * 前段查询最近拍中
     * @param pageNo 页码
     * @param pageSize 每页数量
     * @return 分页对象
     */
    Page<OrderBean> ClientFindAll(Integer pageNo, Integer pageSize);

    /**
     * 查询某订单详情
     * @param orderId 订单id
     * @return 订单对象
     */
    OrderBean findOne(Long orderId);

    /**
     * 确认收货
     * @param orderId 订单id
     */
    void sign_up_order(Long orderId);

    /**
     * 添加订单发货详情
     * @param orderId 订单id
     * @param express_number 快递编号
     * @param card 卡号
     * @param density 卡密
     */
    void update(Long orderId, String express_number, String card, String density);

    /**
     * 客户端查找订单
     * @param userId 用户id
     * @param item_name 订单名称
     * @param pageNo 页码
     * @param pageSize 每页数量
     * @return 订单分页对象
     */
    Page<OrderBean> clientFindAll(Long userId, String item_name, Integer pageNo, Integer pageSize);

    /**
     * 查找发货地址
     * @param orderId 订单id
     * @return 地址对象
     */
    AddressBean findAddress(Long orderId);
}
