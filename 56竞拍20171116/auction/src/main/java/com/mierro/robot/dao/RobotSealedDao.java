package com.mierro.robot.dao;

import com.mierro.main.entity.SealedBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by tlseek on 2017/8/24.
 */
public interface RobotSealedDao extends JpaRepository<SealedBean,Long> {

    @Query("SELECT seal FROM SealedBean seal " +
            " WHERE seal.id NOT IN (SELECT si.orderId FROM ShowItemBean si) " +
            " AND seal.robot=true   AND seal.itemName LIKE ?1 AND seal.name LIKE ?2 " +
            " AND seal.isSealed = true ")
    Page<SealedBean> findAll(String item_name, String username, Pageable pageable);
    @Query("SELECT seal FROM SealedBean seal " +
            " INNER JOIN ItemBean item ON item.id=seal.itemId AND item.front_show=?3" +
            " WHERE seal.id NOT IN (SELECT si.orderId FROM ShowItemBean si) " +
            " AND seal.robot=true  AND seal.itemName LIKE ?1 AND seal.name LIKE ?2" +
            " AND seal.isSealed = true ")
    Page<SealedBean> findAll(String item_name, String username, Boolean front_show, Pageable pageable);

    @Query("SELECT SUM(seal.cost) FROM SealedBean seal WHERE seal.userId=?1")
    String sumCost(Long userId);
    @Query("SELECT AVG(seal.market_price) FROM SealedBean seal WHERE seal.itemId=?1")
    String averageMarketPrice(Long itemId);
}
