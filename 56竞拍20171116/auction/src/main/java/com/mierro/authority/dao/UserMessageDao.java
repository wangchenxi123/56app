package com.mierro.authority.dao;

import com.mierro.authority.entity.UserMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by 黄晓滨 simba on 2017/7/20.
 * Remarks：
 */
public interface UserMessageDao extends JpaRepository<UserMessage,Long>{


    @Query("select new UserMessage (um,ur.finalLogin,ur.disable) from UserMessage um " +
            "inner join User ur on ur.id = um.id where um.id = ?1")
    UserMessage findOne(Long userId);

    @Query("select new UserMessage (um,u.finalLogin,u.disable) from UserMessage um " +
            "inner join User u on u.id = um.id where um.username like ?1 and um.iphone like ?2 " +
            "and u.disable = ?3 and um.admin = false and um.robot = false ")
    Page<UserMessage> findAllMembers(String name, String phone, Boolean disable, Pageable pageable);

    @Query("select new UserMessage (um,u.finalLogin,u.disable) from UserMessage um " +
            "inner join User u on u.id = um.id " +
            "where um.username like ?1 and um.iphone like ?2 and um.admin = false and um.robot = false ")
    Page<UserMessage> findAllMembers(String name, String phone, Pageable pageable);

    @Query("select new UserMessage (um,u.finalLogin,u.disable) from UserMessage um " +
            "inner join User u on u.id = um.id " +
            "where um.id = ?1")
    Page<UserMessage> findAllById(Long userId, Pageable pageable);

    @Query("select count(user.id) from UserMessage user where user.Level_one_proxy = ?1")
    Integer findNumberToLevel_one_proxy(Long userId);

    @Query("select count(user.id) from UserMessage user where user.Level_two_proxy = ?1")
    Integer findNumberToLevel_two_proxy(Long userId);

    @Modifying
    @Query("update UserMessage usermessage set usermessage.days = ?2 where usermessage.id = ?1")
    void reportedReset(Long userId, Integer day);

    @Query("select count(userMessage.id) from UserMessage userMessage where userMessage.robot = false and userMessage.admin = false")
    Integer getTotal();

    @Query("select usermessage.id from UserMessage usermessage where usermessage.id not in ?1 and usermessage.robot = false ")
    List<Long> findAllNewUsers(List<Long> winningUsers);

    @Query("select usermessage.id from UserMessage usermessage where usermessage.robot = false ")
    List<Long> findAllNewUsers();
}
