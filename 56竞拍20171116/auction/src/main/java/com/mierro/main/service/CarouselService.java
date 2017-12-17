package com.mierro.main.service;

import com.mierro.main.entity.CarouselBean;
import org.springframework.data.domain.Page;

/**
 * Created by 黄晓滨 simba on 2017/6/1.
 * Remarks：
 */
public interface CarouselService {

    /**
     * 分页查看轮播图
     * @param pageNo 页码
     * @param pageSize 每页数量
     * @return 分页对象
     */
    Page<CarouselBean> findAll(Integer pageNo, Integer pageSize);

    /**
     * 分页查看轮播图
     * @param pageNo 页码
     * @param pageSize 每页数量
     * @return 分页对象
     */
    Page<CarouselBean> ClientFindAll(Integer pageNo, Integer pageSize);

    /**
     * 添加新的轮播图
     * @param carouselFigureBean 轮播图bean对象
     */
    void addCarouselFigure(CarouselBean carouselFigureBean);

    /**
     * 修改轮播图信息()
     * @param carouselFigureBean 轮播图bean对象
     */
    void updateCarouselFigure(CarouselBean carouselFigureBean);

    /**
     * 删除轮播图
     * @param id 轮播图id
     */
    void deleteCarouselFigure(Long id);

}
