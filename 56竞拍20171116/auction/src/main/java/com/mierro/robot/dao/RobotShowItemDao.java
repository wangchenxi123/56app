package com.mierro.robot.dao;

import com.mierro.main.entity.ShowItemBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by tlseek on 2017/8/24.
 */
public interface RobotShowItemDao extends JpaRepository<ShowItemBean,Long> {

    @Query("SELECT count(si) FROM ShowItemBean si WHERE si.orderId=?1")
    Long countByOrderId(Long orderId);
}
