package com.mierro.authority.dao;


import com.mierro.authority.common.AythorityType;
import com.mierro.authority.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by 黄晓滨 simba on 2017/2/8.
 * Remarks：
 */
public interface RoleDao extends JpaRepository<Role,Long>{

    @Query(value = "select r from Role r left join UserRoleRelationship ur on ur.userId = ?1 where ur.roleId = r.id")
    List<Role> findRolesByUserId(Long userId);

    @Query(value = "SELECT f FROM Role f WHERE f.identification LIKE ?1 and f.description LIKE ?2")
    Page<Role> findRolesByCondition(String identification, String description, Pageable pageable);

    @Query(value = "SELECT f FROM Role f WHERE f.identification LIKE ?1 and f.description LIKE ?2 and f.type = ?3")
    Page<Role> findRolesByCondition(String identification, String description, AythorityType aythorityType,
                                    Pageable pageable);

    @Query(value = "SELECT f FROM Role f WHERE f.identification LIKE ?1 and f.description LIKE ?2 and f.disable = ?3")
    Page<Role> findRolesByCondition(String identification, String description, Boolean enable, Pageable pageable);

    @Query(value = "SELECT f FROM Role f WHERE f.identification LIKE ?1 and f.description LIKE ?2 " +
            "and f.disable = ?3 and f.type = ?4")
    Page<Role> findRolesByCondition(String identification, String description, Boolean enable, AythorityType
            aythorityType, Pageable pageable);

    @Query("select r.id from Role r inner join UserRoleRelationship ur on ur.userId = ?1 " +
            "where ur.roleId = r.id and r.disable = false and r.type = ?2")
    List<Long> findAythorityTypeByUserId(Long userId, AythorityType aythorityType);
}
