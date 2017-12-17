package com.mierro.main.dao;

import com.mierro.main.common.ItemType;
import com.mierro.main.common.OrderState;
import com.mierro.main.entity.OrderBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;


/**
 * Created by 黄晓滨 simba on 2017/8/14.
 * Remarks：
 */
public interface OrderDao extends JpaRepository<OrderBean,Long> {

    @Query("select ord from OrderBean ord where ord.id in ?1")
    List<OrderBean> findAll(List<Long> orderId);

    @Query("select ord from OrderBean ord where ord.item_name like ?1")
    Page<OrderBean> findAll(String itemName, Pageable pageable);

    @Query("select ord from OrderBean ord inner join UserMessage usermessage on ord.userId = usermessage.id " +
            "where ord.item_name like ?1 and usermessage.robot = ?2" )
    Page<OrderBean> findAll(String itemName, Boolean robot, Pageable pageable);

    @Query("select ord from OrderBean ord where ord.item_name like ?1 and ord.item_type = ?2")
    Page<OrderBean> findAll(String itemName, ItemType itemType, Pageable pageable);

    @Query("select ord from OrderBean ord inner join UserMessage usermessage on ord.userId = usermessage.id " +
            "where ord.item_name like ?1 and ord.item_type = ?2 and usermessage.robot = ?3")
    Page<OrderBean> findAll(String itemName, ItemType itemType, Boolean robot, Pageable pageable);

    @Query("select ord from OrderBean ord inner join UserMessage usermessage on ord.userId = usermessage.id " +
            "where ord.item_name like ?1 and ord.order_state = ?2 and usermessage.robot = ?3")
    Page<OrderBean> findAll(String itemName, OrderState orderState, Boolean robot, Pageable pageable);

    @Query("select ord from OrderBean ord where ord.item_name like ?1 and ord.order_state = ?2")
    Page<OrderBean> findAll(String itemName, OrderState orderState, Pageable pageable);

    @Query("select ord from OrderBean ord where ord.item_name like ?1 and ord.order_state = ?2 and ord.item_type = ?3")
    Page<OrderBean> findAll(String itemName, OrderState orderState, ItemType itemType, Pageable pageable);

    @Query("select ord from OrderBean ord inner join UserMessage usermessage on ord.userId = usermessage.id " +
            "where ord.item_name like ?1 and ord.order_state = ?2 and ord.item_type = ?3 and usermessage.robot = ?4")
    Page<OrderBean> findAll(String itemName, OrderState orderState, ItemType itemType, Boolean robot, Pageable pageable);

    @Query("select ord from OrderBean ord where ord.userId = ?1 and ord.item_name like ?2")
    Page<OrderBean> findAll(Long userId, String itemName, Pageable pageable);

    @Query("select new OrderBean(ord.time,ord.name,ord.item_picture,ord.marketPrice,ord.price,ord.item_id,ord.record_id,ord.item_name) " +
            "from OrderBean ord ")
    Page<OrderBean> ClientFindAll(Pageable pageable);

    @Query("select sum(orders.amounts) from OrderBean orders WHERE orders.paymentTime >= ?1 and orders.paymentTime < ?2")
    Integer findAll(Date start, Date end);

    @Query("select sum(sealed.cost) from OrderBean orders inner join SealedBean sealed on sealed.id = orders.id " +
            "WHERE orders.shipTime >= ?1 and orders.shipTime < ?2")
    Integer findAllTotalShip(Date start, Date end);

}
