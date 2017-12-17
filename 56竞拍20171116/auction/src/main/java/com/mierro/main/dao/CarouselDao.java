package com.mierro.main.dao;


import com.mierro.main.entity.CarouselBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by 黄晓滨 simba on 2017/5/27.
 * Remarks：轮播图
 */
public interface CarouselDao extends JpaRepository<CarouselBean,Long> {

    /**
     * 分页查询未禁用轮播图
     */
    @Query(value = "SELECT C FROM CarouselBean C WHERE C.disable = false ")
    Page<CarouselBean> clientFindAll(Pageable pageable);

    /**
     * 查询所有轮播图
     */
    @Query(value = "SELECT C FROM CarouselBean C ")
    Page<CarouselBean> findAll(Pageable pageable);
}
