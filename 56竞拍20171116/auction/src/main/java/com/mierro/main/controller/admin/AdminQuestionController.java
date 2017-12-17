package com.mierro.main.controller.admin;

import com.mierro.common.common.DataCheck;
import com.mierro.common.common.ResponseCode;
import com.mierro.common.common.ResultMessage;
import com.mierro.main.common.QuestionCategory;
import com.mierro.main.entity.CommonQuestionBean;
import com.mierro.main.service.CommonQuestionService;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * Created by 黄晓滨 simba on 2017/8/15.
 * Remarks：
 */
@RestController
@RequestMapping("/admin")
public class AdminQuestionController {

    @Resource
    public CommonQuestionService commonQuestionService;

    /**
     * 查询所有常见帮助
     * @param title 标题
     * @param questionCategory 常见问题类别
     * @param pageNo 页码
     * @param pageSize 每页数量
     * @return 常见问题分页对象
     */
    @GetMapping("/questions")
    public ResultMessage findAll(@RequestParam(value = "title",required = false) String title,
                                 @RequestParam(value = "questionCategory",required = false) QuestionCategory questionCategory ,
                                 @RequestParam(value = "pageNo") Integer pageNo,
                                 @RequestParam(value = "pageSize") Integer pageSize){
        Page page = commonQuestionService.findAll(title,questionCategory,pageNo,pageSize);
        return new ResultMessage(ResponseCode.OK, "操作成功").putParam("resource",page);
    }

    /**
     * 查询单个常见帮助
     * @param questionId 常见问题id
     * @return 常见问题对象
     */
    @GetMapping("/question")
    public ResultMessage findOne(@RequestParam("questionId") Long questionId){
        CommonQuestionBean commonQuestion = commonQuestionService.findOne(questionId);
        return new ResultMessage(ResponseCode.OK, "操作成功").putParam("resource",commonQuestion);
    }

    /**
     * 修改常见帮助
     * @param commonQuestion 常见问题对象
     * @return 操作码
     */
    @PutMapping("/question")
    public ResultMessage update(@ModelAttribute("commonQuestion") CommonQuestionBean commonQuestion){
        commonQuestionService.update(commonQuestion);
        return new ResultMessage(ResponseCode.OK, "操作成功");
    }

    /**
     * 删除一个常见帮助
     * @param questionId 常见问题id
     * @return 操作码
     */
    @DeleteMapping("/question")
    public ResultMessage delete(@RequestParam("questionId") Long questionId){
        commonQuestionService.delete(questionId);
        return new ResultMessage(ResponseCode.OK, "操作成功");
    }

    /**
     * 添加一个常见帮助
     * @param commonQuestion 常见问题对象
     * @return 操作码
     */
    @PostMapping("/question")
    public ResultMessage add(@ModelAttribute("commonQuestion") @Valid CommonQuestionBean commonQuestion,
                             BindingResult result){
        DataCheck.returnError(result);
        commonQuestionService.add(commonQuestion);
        return new ResultMessage(ResponseCode.OK, "操作成功");
    }

    /**
     * 更新帮助禁用状态
     * @param questionId 商品id
     * @return 操作码
     */
    @PutMapping("/question/state")
    public ResultMessage updateState(@RequestParam("questionId") Long questionId){
        commonQuestionService.updateState(questionId);
        return new ResultMessage(ResponseCode.OK,"操作成功");
    }
}
