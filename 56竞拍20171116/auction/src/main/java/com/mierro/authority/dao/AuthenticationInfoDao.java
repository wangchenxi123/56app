package com.mierro.authority.dao;


import com.mierro.authority.common.AythorityType;
import com.mierro.authority.common.LoginType;
import com.mierro.authority.entity.AuthenticationInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

/**
 * Created by 黄晓滨 simba on 2017/2/11.
 * Remarks：
 */
public interface AuthenticationInfoDao extends JpaRepository<AuthenticationInfo,Long> {

    /**
     * 根据标识符和登陆方式查询登陆信息表
     * @param identifier 标识符
     * @param loginType 登陆方式
     * @return 登陆信息
     */
    @Query(value = "select f FROM AuthenticationInfo f where f.identifier = ?1 and f.voucherType = ?2")
    AuthenticationInfo findByIdentifierAndVoucherType(String identifier, LoginType loginType);

    /**
     * 根据用户id和登陆方式查询登陆信息表
     * @param userId 用户id
     * @param loginType 登陆方式
     * @return 登陆信息
     */
    AuthenticationInfo findByUserIdAndVoucherType(Long userId, LoginType loginType);

    /**
     * 根据用户id查询登陆信息表
     * @param userId 用户id
     * @return 登陆信息
     */
    List<AuthenticationInfo> findByUserId(Long userId);

    /**
     * 查看用户禁用情况
     * @param userId 用户id
     */
    @Query("select u.disable from User u where u.id = ?1")
    Boolean judgeEnable(Long userId);

    /**
     * 根据用户id和角色类型查找拥有权限
     * @param userId 登录方式
     * @param type 角色类型
     */
    @Query(value = "select f.urlStr from Authority f inner join RoleFunctionRelationship rf ON rf.functionId = f.id " +
            "inner join Role r ON rf.roleId = r.id and r.disable = false and r.type= ?2 " +
            "INNER JOIN UserRoleRelationship ur ON ur.roleId = r.id " +
            "AND ur.userId = ?1 AND f.disable = false ")
    Set<String> findAythoritiesByUserId(Long userId, AythorityType type);

    /**
     * 根据用户id查找所有登录方式
     * @param userId 用户id
     */
    @Query(value = "SELECT f FROM AuthenticationInfo f where f.userId =?1 and (f.voucherType=2 OR f.voucherType=5) ")
    List<AuthenticationInfo> findUsernameAndIphoneByUserId(Long userId);


}
