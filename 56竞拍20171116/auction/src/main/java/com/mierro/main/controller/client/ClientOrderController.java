package com.mierro.main.controller.client;

import com.mierro.authority.controller.SystemModelHandler;
import com.mierro.authority.entity.User;
import com.mierro.common.common.ResponseCode;
import com.mierro.common.common.ResultMessage;
import com.mierro.common.common.SpringTool;
import com.mierro.main.common.CoinSource;
import com.mierro.main.common.CoinType;
import com.mierro.main.common.OrderState;
import com.mierro.main.dao.CoinDao;
import com.mierro.main.dao.OrderDao;
import com.mierro.main.entity.CoinBean;
import com.mierro.main.entity.OrderBean;
import com.mierro.main.service.OrderService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by 黄晓滨 simba on 2017/8/9.
 * Remarks：
 */
@RestController
@RequestMapping("/client")
public class ClientOrderController {

    @Resource
    private OrderService orderService;

    /**
     * 查看订单详情
     * @param orderId 订单id
     * @return 订单对象
     */
    @GetMapping("/order")
    public ResultMessage findOrder(@ModelAttribute(SystemModelHandler.CURRENT_USER)User user, Long orderId){
        OrderBean orderBean = orderService.findOne(orderId);
        return new ResultMessage(ResponseCode.OK, "操作成功").putParam("resource",orderBean);
    }

    /**
     * 确认收货
     * @param orderId 订单id
     * @return 操作码
     */
    @PutMapping("/order/sign")
    public ResultMessage sign_up_order(@ModelAttribute(SystemModelHandler.CURRENT_USER)User user, Long orderId){
        orderService.sign_up_order(orderId);
        return new ResultMessage(ResponseCode.OK, "操作成功");
    }


    /**
     * 订单付款下单
     * @param orderId 订单id
     * @return 操作码
     */
    @PostMapping("/order/payment")
    public ResultMessage payment(@ModelAttribute(SystemModelHandler.CURRENT_USER)User user,
                                 @RequestParam("orderId") Long orderId){
        OrderDao orderDao = (OrderDao) SpringTool.getBeanByClass(OrderDao.class);
        OrderBean orderBean = orderDao.findOne(orderId);
        orderBean.setOrder_state(OrderState.WAITING_CHOICE_ADDRESS);
        orderDao.save(orderBean);
        return new ResultMessage(ResponseCode.OK, "操作成功，已经自动付款完成");
    }

    /**
     * 充值拍币下单
     * @param number 充值数目
     * @return 操作码
     */
    @PostMapping("/recharge")
    public ResultMessage Recharge(@ModelAttribute(SystemModelHandler.CURRENT_USER)User user,
                                  @RequestParam("number") Integer number){

        CoinDao coinDao = (CoinDao) SpringTool.getBeanByClass(CoinDao.class);
        CoinBean coinBean = new CoinBean();
        coinBean.setTime(new Date());
        coinBean.setCoinType(CoinType.RECHARGE);
        coinBean.setNumber(number);
        coinBean.setSource(CoinSource.RECHARGE);
        coinBean.setUserId(user.getId());
        coinBean.setReason("系统自动充值");
        coinBean.setPromotion(false);
        coinDao.save(coinBean);
        return new ResultMessage(ResponseCode.OK, "操作成功,自动充值100拍币");
    }
}
