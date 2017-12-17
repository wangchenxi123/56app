package com.mierro.main.dao;

import com.mierro.main.common.OrderState;
import com.mierro.main.entity.SealedBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * Created by 黄晓滨 simba on 2017/8/15.
 * Remarks：
 */
public interface SealedDao extends JpaRepository<SealedBean,Long> {


    @Query("select seal from SealedBean seal where seal.itemName like ?1 and seal.isSealed = true")
    Page<SealedBean> findAll(String item_name, Pageable pageable);

    @Query("select seal from SealedBean seal where seal.itemName like ?1 and seal.robot =?2 and seal.isSealed = true")
    Page<SealedBean> findAll(String item_name, Boolean robot, Pageable pageable);

    @Query("select seal from SealedBean seal where seal.itemName like ?1 and seal.time >= ?2 and seal.time <= ?3 " +
            "and seal.isSealed = true")
    Page<SealedBean> findAll(String item_name, Date stateTime, Date endTime, Pageable pageable);

    @Query("select seal from SealedBean seal where seal.itemName like ?1 and seal.robot =?2 and seal.isSealed = true " +
            "and seal.time >= ?2 and seal.time <= ?3")
    Page<SealedBean> findAll(String item_name, Boolean robot, Date stateTime, Date endTime, Pageable pageable);

    @Query("select seal from SealedBean seal inner join OrderBean ord on ord.userId = ?1 where ord.id = seal.id ")
    Page<SealedBean> findAll(Long userId, Pageable pageable);

    @Query("select seal from SealedBean seal inner join OrderBean ord on ord.userId = ?1 " +
            "inner join BiddersBean bidder on bidder.userId = ?1 " +
            "where ord.id = seal.id or bidder.sealedId = seal.id group by seal.id")
    Page<SealedBean> findAll_all(Long userId, Pageable pageable);

    @Query("select seal from SealedBean seal " +
            "left join OrderBean ord on ord.userId = ?1 " +
            "left join BiddersBean bidder on bidder.userId = ?1 " +
            "where ord.id = seal.id or bidder.sealedId = seal.id " +
            "or (seal.itemId IN ?2 and seal.isSealed = false) " +
            "group by seal.id")
    Page<SealedBean> findAll(Long userId, List<Long> items, Pageable pageable);

    @Query("select seal from SealedBean seal where seal.itemId in ?1 and seal.isSealed = ?2")
    Page<SealedBean> findAll(List<Long> itemId, Boolean isSealed, Pageable pageable);

    @Query("SELECT seal FROM SealedBean seal " +
            "INNER join BiddersBean bidder on bidder.userId = ?1 " +
            "WHERE bidder.sealedId = seal.id and seal.isSealed = ?2 and seal.userId <> ?1  GROUP BY seal.id")
    Page<SealedBean> findAllByUserIdOnNoWinning(Long userId, Boolean isSealed, Pageable pageable);

    @Query("select seal from SealedBean seal where seal.itemId = ?1 and seal.isSealed = true")
    Page<SealedBean> findItemId(Long itemId, Pageable pageable);

    @Query("select seal from SealedBean seal where seal.itemId = ?1 and seal.isSealed = false ")
    SealedBean findItemId(Long itemId);

    @Query("select seal from SealedBean seal inner join OrderBean ord on ord.userId = ?1 and ord.order_state = ?2 " +
            "where ord.id = seal.id ")
    Page<SealedBean> findAll(Long userId, OrderState orderState, Pageable pageable);

    @Query("select seal from SealedBean seal where seal.isSealed = false")
    List<SealedBean> findAllCache();

    @Query("select sum(seal.profit) from SealedBean seal where seal.isSealed = true")
    Object getTotal();

    @Query("SELECT new SealedBean(sealed,COUNT(bidderOne.id)) from BiddersBean bidderOne " +
            "inner join SealedBean sealed on bidderOne.sealedId = sealed.id WHERE bidderOne.userId = ?1 " +
            "and bidderOne.sealedId in(select bidder.sealedId FROM BiddersBean bidder WHERE bidder.userId = ?1 " +
            "GROUP BY bidder.sealedId) GROUP BY bidderOne.sealedId")
    Page<SealedBean> findSealAndNumberCountByUserId(Long userId, Pageable pageable);

    @Query("select sealed.userId from SealedBean sealed where sealed.isSealed = true ")
    List<Long> findWinningUsers();
}
