package com.mierro.authority.dao;

import com.mierro.authority.common.LoginEquipment;
import com.mierro.authority.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * Created by 黄晓滨 simba on 2017/2/9.
 * Remarks：
 */
public interface UserDao extends JpaRepository<User,Long>{

    @Query("select u from User u where u.refreshToken = ?1 ")
    User findByRAndRefreshToken(String token);

    @Query("select count(u.id) from User u where u.createTime >= ?1 and u.createTime >= ?2")
    Integer findCountNumber(Date start, Date end);

    @Query("select count(u.id) from User u where u.loginEquipment in ?1 and u.createTime >= ?2 and u.createTime >= ?3")
    Integer findCountNumberAndLoginEquipment(List<LoginEquipment> loginEquipment, Date start, Date end);

}
