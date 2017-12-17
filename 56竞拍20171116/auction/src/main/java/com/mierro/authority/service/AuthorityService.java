package com.mierro.authority.service;

import com.mierro.authority.common.AythorityType;
import com.mierro.authority.common.LoginType;
import com.mierro.authority.entity.AuthenticationInfo;
import com.mierro.authority.entity.Authority;
import com.mierro.authority.entity.Role;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by 黄晓滨 simba on 2017/7/20.
 * Remarks：
 */
public interface AuthorityService {

    /**
     * 分页条件查询角色
     * @param pageNo 页码
     * @param pageSize 每页数目
     * @param identification 名称
     * @param description 描述
     * @param disable 是否禁用
     * @param aythorityType 角色类型
     */
    Page<Role> findAll(String identification, String description,
                       Boolean disable, AythorityType aythorityType, Integer pageNo, Integer pageSize);

    /**
     * 查询所有权限
     */
    Set<String> selectAllAuthority();

    /**
     * 根据用户识别码和登录方式查询登录信息
     * @param identifier 用户识别码
     * @param loginType 登录方式
     */
    AuthenticationInfo selectLoginMessageByIdentifier(String identifier, LoginType loginType);

    /**
     * 判断用户是否被禁用
     * @param userId 用户id
     */
    Boolean judgeEnable(Long userId);

    /**
     * 根据用户的唯一标志和用户登录类型查找所有的没有被标志为删除的权限
     * @param userId 用户id
     * @param LoginType 登录类型
     */
    Set<String> findPermsByUserPrincipal(Long userId, AythorityType LoginType);

    /**
     * 根据角色类型和用户id校验该用户是否该类型角色
     * @param userId 用户id
     * @param aythorityType 角色类型
     */
    Boolean findAythorityTypeByUserId(Long userId, AythorityType aythorityType);

    /**
     * 更新用户最后登陆时间
     * @param userId 用户id
     */
    void updateFinalLoginByUserId(Long userId, HttpServletRequest httpServletRequest);

    /**
     * 根据用户id和登陆方式查找登陆信息
     * @param userId 用户id
     * @param loginType 登陆方式
     */
    AuthenticationInfo findByUserIdAndLongType(Long userId, LoginType loginType);

}
