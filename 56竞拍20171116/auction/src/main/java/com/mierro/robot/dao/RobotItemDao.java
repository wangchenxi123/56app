package com.mierro.robot.dao;

import com.mierro.main.entity.ItemBean;
import com.mierro.robot.entity.RobotItemBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by tlseek on 2017/8/12.
 */
public interface RobotItemDao extends JpaRepository<RobotItemBean, Long> {

    @Query("SELECT ri FROM RobotItemBean ri WHERE ri.itemId=?1")
    RobotItemBean findByItemId(Long itemId);

    @Query(value = "SELECT new RobotItemBean(ri, i.name, i.control_line, i.running_program,  (SELECT AVG(seal.market_price) FROM SealedBean seal WHERE seal.itemId=i.id)) " +
            " FROM RobotItemBean ri  INNER JOIN ItemBean i ON ri.itemId=i.id WHERE i.name LIKE ?1 ORDER BY ri.itemId DESC ",
    countQuery = "SELECT count(ri.id) FROM RobotItemBean ri  INNER JOIN ItemBean i ON ri.itemId=i.id WHERE i.name LIKE ?1")
    Page<RobotItemBean> findByQuery(String name, Pageable pageable);

    @Query( value = "SELECT new RobotItemBean(ri, i.name, i.control_line, i.running_program, (SELECT AVG(seal.market_price) FROM SealedBean seal WHERE seal.itemId=i.id) ) " +
            " FROM RobotItemBean ri  INNER JOIN ItemBean i ON ri.itemId=i.id  ORDER BY ri.itemId DESC ",
    countQuery = "SELECT COUNT(ri.id) FROM RobotItemBean ri  INNER JOIN ItemBean i ON ri.itemId=i.id ")
    Page<RobotItemBean> findByQuery(Pageable pageable);

    /**查询没有对应机器人商品bean的商品*/
    @Query("SELECT i FROM ItemBean i  LEFT JOIN RobotItemBean ri ON ri.itemId=i.id WHERE ri.id IS NULL ")
    List<ItemBean> findItemByRobotItemIsNull();


}
