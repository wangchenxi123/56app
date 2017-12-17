package com.mierro.main.service.impl;


import com.mierro.common.common.ResponseCode;
import com.mierro.common.common.Tool;
import com.mierro.common.common.VerifyException;
import com.mierro.main.dao.CarouselDao;
import com.mierro.main.entity.CarouselBean;
import com.mierro.main.service.CarouselService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Created by 黄晓滨 on 2017/3/28
 * Remarks: 轮播图
 */
@Service("CarouselFigureService")
public class CarouselServiceImpl implements CarouselService {

    @Resource
    private CarouselDao carouselDao;

    @Override
    @Transactional(readOnly = true)
    public Page<CarouselBean> findAll(Integer pageNo, Integer pageSize) {
        if (pageNo < 1){
            throw new VerifyException("pageNo参数必须大于1");
        }
        Sort sort = new Sort(Sort.Direction.DESC,"sort")
                .and(new Sort(Sort.Direction.DESC,"id"));
        Pageable pageable = new PageRequest(--pageNo,pageSize,sort);
        return carouselDao.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CarouselBean> ClientFindAll(Integer pageNo, Integer pageSize) {
        if (pageNo < 1){
            throw new VerifyException("pageNo参数必须大于1");
        }
        Sort sort = new Sort(Sort.Direction.DESC,"sort")
                .and(new Sort(Sort.Direction.DESC,"id"));
        Pageable pageable = new PageRequest(--pageNo,pageSize,sort);
        return carouselDao.clientFindAll(pageable);
    }

    @Override
    public void addCarouselFigure(CarouselBean carouselBean) {
        carouselBean.setDisable(true);
        carouselDao.save(carouselBean);
    }

    @Override
    public void updateCarouselFigure(CarouselBean carouselBean) {
        if(carouselBean.getId() == null){
            throw new VerifyException("缺少必要参数");
        }
        CarouselBean oldData =  carouselDao.findOne(carouselBean.getId());
        Boolean disable;
        if (carouselBean.getDisable() != null){
            disable = !oldData.getDisable();
        }else{
            disable = oldData.getDisable();
        }
        Tool.dataCheckout(carouselBean,oldData);
        oldData.setDisable(disable);
        carouselDao.save(oldData);
    }

    @Override
    public void deleteCarouselFigure(Long id) {
        if (id == null){
            throw new VerifyException(ResponseCode.BAD_REQUEST,"请传入id值");
        }
        carouselDao.delete(id);
    }
}
