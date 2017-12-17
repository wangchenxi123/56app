package com.mierro.main.controller.admin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mierro.common.common.DataCheck;
import com.mierro.common.common.ResponseCode;
import com.mierro.common.common.ResultMessage;
import com.mierro.main.entity.LabelBean;
import com.mierro.main.service.LabelService;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.IOException;

/**
 * Created by 黄晓滨 simba on 2017/8/14.
 * Remarks：标签管理
 */
@RestController
@RequestMapping("/admin")
public class AdminLabelController {

    @Resource
    private LabelService labelService;

    /**
     * 查询多个分类
     * @param pageNo 页码
     * @param pageSize 每页数量
     * @return 分类分页对象
     */
    @GetMapping("/labels")
    public ResultMessage findAll(@RequestParam("pageNo") Integer pageNo, @RequestParam("pageSize") Integer pageSize) {
        Page<LabelBean> labelBeans = labelService.findAll(pageNo, pageSize);
        return new ResultMessage(ResponseCode.OK, "操作成功").putParam("resource", labelBeans);
    }

    /**
     * 查询单个分类(会返回分类所属Item)
     * @param labelId 分类id
     * @return 分类对象
     * @throws IOException json转换错误
     */
    @GetMapping("/label")
    public ResultMessage findOne(@RequestParam("labelId") Long labelId) throws IOException {
        LabelBean labelBean = labelService.findOne(labelId);
        return new ResultMessage(ResponseCode.OK, "操作成功").putParam("resource", labelBean);
    }

    /**
     * 添加一个分类
     * @param labelBean 分类对象id
     * @return 操作码
     * @throws JsonProcessingException json转换错误
     */
    @PostMapping("/label")
    public ResultMessage addSort(@ModelAttribute("labelBean") @Valid LabelBean labelBean,
                                 BindingResult result) throws JsonProcessingException {
        DataCheck.returnError(result);
        labelService.addLabel(labelBean);
        return new ResultMessage(ResponseCode.OK, "操作成功");
    }

    /**
     * 修改一个分类
     * @param labelBean 分类对象id
     * @return 操作码
     * @throws JsonProcessingException json转换错误
     */
    @PutMapping("/label")
    public ResultMessage updateSort(@ModelAttribute("labelBean") LabelBean labelBean) throws JsonProcessingException {
        labelService.updateLabel(labelBean);
        return new ResultMessage(ResponseCode.OK, "操作成功");
    }

    /**
     * 删除一个分类
     * @param labelId 分类id
     * @return 操作码
     */
    @DeleteMapping("/label")
    public ResultMessage delete(@RequestParam("labelId") Long labelId) {
        labelService.delete(labelId);
        return new ResultMessage(ResponseCode.OK, "操作成功");
    }

    /**
     * 更新分类禁用状态
     * @param labelId 分类id
     * @return 操作码
     */
    @PutMapping("/label/state")
    public ResultMessage updateState(@RequestParam("labelId") Long labelId){
        labelService.updateLabelState(labelId);
        return new ResultMessage(ResponseCode.OK,"操作成功");
    }
}
