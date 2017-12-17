package com.mierro.main.dao;

import com.mierro.main.entity.SortBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by 黄晓滨 simba on 2017/6/1.
 * Remarks：
 */
public interface SortDao extends JpaRepository<SortBean,Long> {

    /**
     * 查询所有未禁用标签
     */
    @Query("select la from SortBean la where la.disable = false order by la.sort,la.id desc")
    Page<SortBean> clientFindAll(Pageable pageable);

    /**
     * 查询所有标签
     */
    @Query("select la from SortBean la order by la.sort,la.id desc")
    Page<SortBean> findAll(Pageable pageable);

}
