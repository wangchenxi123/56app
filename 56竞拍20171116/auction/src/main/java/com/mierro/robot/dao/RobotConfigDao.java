package com.mierro.robot.dao;

import com.mierro.robot.entity.RobotConfigBean;
import com.mierro.robot.entity.e.RobotConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by tlseek on 2017/8/18.
 */
public interface RobotConfigDao  extends JpaRepository<RobotConfigBean, Long> {

    @Query("SELECT value FROM RobotConfigBean WHERE name=?1")
    String get(RobotConfig key);

    @Modifying
    @Transactional
    @Query("UPDATE RobotConfigBean SET value=?2 WHERE name=?1")
    void put(RobotConfig key, String value);
}
