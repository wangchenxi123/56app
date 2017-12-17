package com.mierro.main.controller.client;

import com.mierro.common.common.ResponseCode;
import com.mierro.common.common.ResultMessage;
import com.mierro.main.entity.LabelBean;
import com.mierro.main.service.LabelService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by 黄晓滨 simba on 2017/8/14.
 * Remarks：标签管理
 */
@RestController
@RequestMapping("/client")
public class ClientLabelController {

    @Resource
    private LabelService labelService;

    /**
     * 查询多个分类
     * @return 分类分页对象
     */
    @GetMapping("/labels")
    public ResultMessage findAll() {
        List<LabelBean> labelBeans = labelService.findAll();
        return new ResultMessage(ResponseCode.OK, "操作成功").putParam("resource", labelBeans);
    }
}
