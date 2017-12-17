package com.mierro.main.dao;

import com.mierro.main.entity.LabelBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by 黄晓滨 simba on 2017/8/14.
 * Remarks：
 */
public interface LabelDao extends JpaRepository<LabelBean,Long> {

    @Query("select label from LabelBean label where label.disable = false ")
    List<LabelBean> findAll();

    @Query("select count (label.id) from LabelBean label where label.disable = false ")
    Integer countNumber();
}
