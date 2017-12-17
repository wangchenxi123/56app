package com.mierro.main.controller.admin;

import com.mierro.common.common.DataCheck;
import com.mierro.common.common.ResponseCode;
import com.mierro.common.common.ResultMessage;
import com.mierro.common.common.VerifyException;
import com.mierro.main.entity.CarouselBean;
import com.mierro.main.service.CarouselService;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * Created by 黄晓滨 simba on 2017/5/27.
 * Remarks：轮播图接口
 */
@RestController
@RequestMapping("/admin")
public class AdminCarouselController {

    @Resource
    private CarouselService carouselService;

    /**
     * 添加轮播图
     * @param carouselBean 轮播图bean对象
     * @return 轮播图bean对象
     */
    @PostMapping("/carousel")
    public ResultMessage AddCarouselFigure(@ModelAttribute("carouselFigureBean") @Valid CarouselBean carouselBean,
                                           BindingResult result){
        DataCheck.returnError(result);
        carouselService.addCarouselFigure(carouselBean);
        return new ResultMessage(ResponseCode.OK,"操作成功");
    }

    /**
     * 修改轮播图
     * @param carouselBean 轮播图bean对象
     * @return 操作状态
     */
    @PutMapping("/carousel")
    public ResultMessage updatecarousel(@ModelAttribute("carouselFigureBean") CarouselBean carouselBean){
        if (carouselBean == null || carouselBean.getId() == null){
            throw new VerifyException(ResponseCode.BAD_REQUEST,"请传入id值");
        }
        carouselBean.setDisable(null);
        carouselService.updateCarouselFigure(carouselBean);
        return new ResultMessage(ResponseCode.OK,"操作成功");
    }

    /**
     * 修改轮播图状态
     * @param carouselId 轮播图bean对象
     * @return 操作状态
     */
    @PutMapping("/carousel/start")
    public ResultMessage updateCarouselStart(Long carouselId){
        CarouselBean carouselBean = new CarouselBean();
        carouselBean.setId(carouselId);
        carouselBean.setDisable(false);
        carouselService.updateCarouselFigure(carouselBean);
        return new ResultMessage(ResponseCode.OK,"操作成功");
    }

    /**
     * 删除轮播图
     * @param id 轮播图id
     * @return 操作状态
     */
    @DeleteMapping("/carousel")
    public ResultMessage delete(@RequestParam("id") Long id){
        carouselService.deleteCarouselFigure(id);
        return new ResultMessage(ResponseCode.OK,"操作成功");
    }

    /**
     * 分页查询轮播图
     * @param pageNo 页码
     * @param pageSize 每页数量
     * @return 分页对象
     */
    @GetMapping("/carousel")
    public ResultMessage carouselFigure(@RequestParam("pageNo") Integer pageNo,
                                        @RequestParam("pageSize") Integer pageSize){
        Page<CarouselBean> page = carouselService.findAll(pageNo,pageSize);
        return new ResultMessage(ResponseCode.OK,"操作成功").putParam("resource",page);
    }
}
