package com.mierro.main.controller.client;

import com.mierro.common.common.ResponseCode;
import com.mierro.common.common.ResultMessage;
import com.mierro.main.entity.CarouselBean;
import com.mierro.main.service.CarouselService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by 黄晓滨 simba on 2017/5/27.
 * Remarks：轮播图接口
 */
@RestController
@RequestMapping("/client")
public class ClientCarouselController {

    @Resource
    private CarouselService carouselService;

    /**
     * 分页查询轮播图
     * @param pageNo 页码
     * @param pageSize 每页数量
     * @return 分页对象
     */
    @GetMapping("/carouselFigures")
    public ResultMessage carouselFigure(@RequestParam("pageNo") Integer pageNo,
                                        @RequestParam("pageSize") Integer pageSize){
        Page<CarouselBean> page = carouselService.ClientFindAll(pageNo,pageSize);
        return new ResultMessage(ResponseCode.OK,"操作成功").putParam("resource",page);
    }
}
