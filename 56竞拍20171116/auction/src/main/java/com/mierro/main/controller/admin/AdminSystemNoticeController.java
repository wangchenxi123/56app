package com.mierro.main.controller.admin;

import com.mierro.authority.controller.SystemModelHandler;
import com.mierro.authority.entity.User;
import com.mierro.common.common.ResponseCode;
import com.mierro.common.common.ResultMessage;
import com.mierro.main.common.NoticeState;
import com.mierro.main.entity.SystemNoticeBean;
import com.mierro.main.service.SystemNoticeService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 系统通知模块控制层
 * Created by tlseek on 2017/8/28.
 */
@RestController
@RequestMapping("/admin")
public class AdminSystemNoticeController {

    @Resource
    SystemNoticeService systemNoticeService;

    /**
     * 分页查看系统通知
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GetMapping("/system/notice")
    public ResultMessage find(
            @RequestParam(required = false) NoticeState state,
            @RequestParam("pageNo") Integer pageNo,
            @RequestParam("pageSize") Integer pageSize) {
        Page page = systemNoticeService.find(state,pageNo, pageSize);
        return new ResultMessage(ResponseCode.OK, "操作成功").putParam("resource", page);
    }

    /**
     * 阅读通知
     * @param noticeId 通知ID
     * @return
     */
    @PostMapping("/system/notice/read")
    public ResultMessage read(
            @RequestParam Long noticeId,
            @ModelAttribute(SystemModelHandler.CURRENT_USER)User user) {
        SystemNoticeBean notice = systemNoticeService.changeState(noticeId, user.getId(), NoticeState.READ);
        return new ResultMessage(ResponseCode.OK, "操作成功").putParam("resource", notice);
    }
}
