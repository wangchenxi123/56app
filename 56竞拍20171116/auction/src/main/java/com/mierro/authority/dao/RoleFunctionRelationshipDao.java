package com.mierro.authority.dao;

import com.mierro.authority.entity.RoleFunctionRelationship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by 黄晓滨 simba on 2017/2/8.
 * Remarks：
 */
public interface RoleFunctionRelationshipDao extends JpaRepository<RoleFunctionRelationship,Long> {
    @Modifying
    @Query(value = "delete from RoleFunctionRelationship where roleId = ?1 and functionId not in ?2")
    void delete(Long roleId, List<Long> ids);

    /**
     * 查询某一个角色的部分权限内容
     * @param roleId 角色id
     * @param functionId 权限id
     */
    RoleFunctionRelationship findByRoleIdAndFunctionId(Long roleId, Long functionId);
}
