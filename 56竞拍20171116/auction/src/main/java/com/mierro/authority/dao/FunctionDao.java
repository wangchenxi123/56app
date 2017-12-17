package com.mierro.authority.dao;


import com.mierro.authority.entity.Authority;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

/**
 * Created by 黄晓滨 simba on 2017/2/7.
 * Remarks：
 */
public interface FunctionDao extends JpaRepository<Authority,Long>{

    /**
     * 查找所有权限url
     */
    @Query(value = "select a.urlStr from Authority a where a.disable = false ")
    Set<String> findUrl();

    /**
     * 按照条件查询权限1
     * @param identification 权限名称
     * @param description 权限描述
     * @param disable 是否禁用
     * @param pageable 分页参数
     */
    @Query(value = "SELECT f FROM Authority f WHERE f.identification LIKE ?1 AND f.description LIKE ?2 AND f.disable = ?3 ")
    Page<Authority> findAll(String identification, String description, Boolean disable, Pageable pageable);

    /**
     * 按照条件查询权限2
     * @param identification 权限名称
     * @param description 权限描述
     * @param pageable 分页参数
     */
    @Query(value = "SELECT f FROM Authority f WHERE f.identification LIKE ?1 AND f.description LIKE ?2")
    Page<Authority> findAll(String identification, String description, Pageable pageable);

    /**
     * 根据url查找权限详情
     * @param url url
     */
    @Query("select f FROM Authority f WHERE f.urlStr= ?1 ")
    Authority findByUrlStr(String url);

    /**
     * 获取所有的权限Id
     */
    @Query("SELECT f.id FROM Authority f")
    Set<Long> findAllId();

    /**
     * 查询去除传入权限id的所有权限id
     * @param ids 权限id
     */
    @Query(value = "SELECT f FROM Authority f WHERE f.id NOT IN ?1")
    Set<Long> findByAll(Set<Long> ids);
}
