package com.mierro.robot.dao;

import com.mierro.robot.entity.RobotUserBean;
import com.mierro.robot.entity.e.RobotUserState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by tlseek on 2017/8/22.
 */
public interface RobotUserDao extends JpaRepository<RobotUserBean, Long> {



    /**查询没有对应机器人用户bean的用户信息*/
    @Query("SELECT um.id FROM  UserMessage um " +
            " LEFT JOIN RobotUserBean ru ON ru.id=um.id " +
            " WHERE um.robot=true AND ru.id IS NULL ")
    List<Long> findUserIdByRobotUserIsNull();


    /**将*/
    @Query("SELECT new RobotUserBean(ru.id, ru.state, um) " +
            " FROM  RobotUserBean ru " +
            " INNER JOIN  UserMessage um ON ru.id=um.id " +
            " WHERE ru.id IN :ids ")
    List<RobotUserBean> findWithUserMessage(@Param("ids") List<Long> ids);
    /**将*/
    @Query("SELECT new RobotUserBean(ru.id, ru.state, um) " +
            " FROM  RobotUserBean ru " +
            " INNER JOIN  UserMessage um ON ru.id=um.id " +
            " WHERE ru.id = ?1 ")
    RobotUserBean findWithUserMessage(Long ids);


    /**将*/
    @Query("SELECT ru.id " +
            " FROM  RobotUserBean ru " +
            " INNER JOIN  User u ON u.id=ru.id AND u.disable=false " +
            " WHERE ru.state=?1 ")
    List<Long> findIds(RobotUserState state);

    /**统计可用的机器人用户不同状态的数目*/
    @Query("SELECT COUNT(ru.id) " +
            " FROM  RobotUserBean ru " +
            " INNER JOIN  User u ON u.id=ru.id AND u.disable=false " +
            " WHERE ru.state=?1 ")
    Long countEnableUserByState(RobotUserState state);

    /**分页查询可用的机器人用户不同状态的数目*/
    @Query("SELECT new RobotUserBean(ru.id, ru.state, um)  " +
            " FROM  RobotUserBean ru " +
            " INNER JOIN  UserMessage um ON ru.id=um.id " +
            " INNER JOIN  User u ON u.id=ru.id AND u.disable=false " +
            " WHERE ru.state=?1 ")
    Page<RobotUserBean> findEnableUserByState(RobotUserState state, Pageable pageable);
    /**统计可用的机器人用户不同状态的数目*/
    @Query("SELECT ru.id " +
            " FROM  RobotUserBean ru " +
            " INNER JOIN  User u ON u.id=ru.id AND u.disable=false " +
            " WHERE ru.state=?1 AND ru.id NOT IN ?2")
    List<Long> findIdsEnableUserByStateNotIn(RobotUserState state, List<Long> userIds);


    @Query("UPDATE RobotUserBean ru SET ru.state=?1 WHERE ru.id=?2")
    @Modifying(clearAutomatically = true)
    @Transactional
    int changeState(RobotUserState state, Long robotId);

    @Query("UPDATE RobotUserBean ru SET ru.state=:state WHERE ru.id IN :robotIds")
    @Modifying(clearAutomatically = true)
    @Transactional
    int changeState(@Param("state") RobotUserState state, @Param("robotIds") List<Long> robotIds);

    @Query("UPDATE RobotUserBean ru SET ru.state=:state WHERE ru.state<>:state")
    @Modifying(clearAutomatically = true)
    @Transactional
    int changeState(@Param("state") RobotUserState state);


}
