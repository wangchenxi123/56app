package com.mierro.authority.dao;


import com.mierro.authority.common.AythorityType;
import com.mierro.authority.entity.UserRoleRelationship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by 黄晓滨 simba on 2017/2/8.
 * Remarks：
 */
public interface UserRoleRelationshipDao extends JpaRepository<UserRoleRelationship,Long> {


    /**
     *  根据用户id和角色id查询角色详情
     * @param userId 用户id
     * @param roleId 角色id
     * @return 角色详情
     */
    UserRoleRelationship findByUserIdAndRoleId(Long userId, Long roleId);

}
