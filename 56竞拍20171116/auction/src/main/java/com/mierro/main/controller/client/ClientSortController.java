package com.mierro.main.controller.client;

import com.mierro.common.common.ResponseCode;
import com.mierro.common.common.ResultMessage;
import com.mierro.main.entity.SortBean;
import com.mierro.main.service.SortService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * Created by 黄晓滨 simba on 2017/8/12.
 * Remarks：商品分类设置
 */
@RestController
@RequestMapping("/client")
public class ClientSortController {

    @Resource
    private SortService sortService;

    /**
     * 根据条件查询所有商品分类
     * @param pageNo 页码
     * @param pageSize 每页数量
     * @return 分类分页对象
     */
    @GetMapping("/sorts")
    public ResultMessage findAll(@RequestParam("pageNo") Integer pageNo,
                                 @RequestParam("pageSize") Integer pageSize) {
        Page<SortBean> sortBeans = sortService.clientFindAll(pageNo, pageSize);
        return new ResultMessage(ResponseCode.OK, "操作成功").putParam("resource", sortBeans);
    }

    /**
     * 查询单个分类(会返回分类所属Item)
     * @param sortId 分类id
     * @return 分类对象
     * @throws IOException json转换错误
     */
    @GetMapping("/sort")
    public ResultMessage findOne(@RequestParam("sortId") Long sortId) throws IOException {
        SortBean sortBean = sortService.ClientFindOne(sortId);
        return new ResultMessage(ResponseCode.OK, "操作成功").putParam("resource", sortBean);
    }
}
