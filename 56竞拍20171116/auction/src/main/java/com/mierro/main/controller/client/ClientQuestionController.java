package com.mierro.main.controller.client;

import com.mierro.common.common.ResponseCode;
import com.mierro.common.common.ResultMessage;
import com.mierro.main.common.QuestionCategory;
import com.mierro.main.entity.CommonQuestionBean;
import com.mierro.main.service.CommonQuestionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by 黄晓滨 simba on 2017/8/15.
 * Remarks：
 */
@RestController
@RequestMapping("/client")
public class ClientQuestionController {

    @Resource
    public CommonQuestionService commonQuestionService;

    /**
     * 查询所有常见帮助
     * @return 常见问题对象
     */
    @GetMapping("/questions")
    public ResultMessage findAll(@RequestParam(value = "questionCategory",required = false) QuestionCategory questionCategory ){
        List page = commonQuestionService.findAll(questionCategory);
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
     * 查询常见帮助类型
     * @return 常见问题对象
     */
    @GetMapping("/question/state")
    public ResultMessage findQuestionType(){
        List list = QuestionCategory.getQuestionType();
        return new ResultMessage(ResponseCode.OK, "操作成功").putParam("resource",list);
    }
}
