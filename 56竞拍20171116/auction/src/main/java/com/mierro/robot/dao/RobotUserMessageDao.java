package com.mierro.robot.dao;

import com.mierro.authority.entity.UserMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 机器人模块使用的dao操作
 * Created by tlseek on 2017/8/14.
 */
public interface RobotUserMessageDao extends JpaRepository<UserMessage,Long> {

    @Query("SELECT COUNT(um.id) FROM UserMessage um inner join User ur on ur.id = um.id " +
            " WHERE um.robot=true AND ur.disable=false ")
    Long countRobotId();

    @Query("SELECT um.id FROM UserMessage um inner join User ur on ur.id = um.id " +
            " WHERE um.robot=true AND ur.disable=false ")
    List<Long> findRobotIds();

    @Transactional
    @Modifying
    @Query("UPDATE UserMessage SET head_pic=:imgId WHERE id in :ids")
    void updateImg(@Param("imgId") Long imgId, @Param("ids") List<Long> ids);

    @Query("SELECT um.id FROM UserMessage um inner join User ur on ur.id = um.id " +
            " WHERE um.robot=true AND ur.disable=false ")
    Page<Long> findRobotId(Pageable pageable);


    @Query("select new UserMessage (um,u.finalLogin,u.disable) from UserMessage um " +
            "inner join User u on u.id = um.id where um.username like ?1 " +
            "and u.disable = ?3 and um.admin = false and um.robot = true ")
    Page<UserMessage> findAllMembers(String name, Boolean disable, Pageable pageable);

    @Query("select new UserMessage (um,u.finalLogin,u.disable) from UserMessage um " +
            "inner join User u on u.id = um.id " +
            "where um.username like ?1 and um.admin = false and um.robot = true ")
    Page<UserMessage> findAllMembers(String name, Pageable pageable);
}
