package com.mierro.main.controller.client;

import com.mierro.authority.controller.SystemModelHandler;
import com.mierro.authority.entity.User;
import com.mierro.common.common.ResponseCode;
import com.mierro.common.common.ResultMessage;
import com.mierro.main.common.NoticeState;
import com.mierro.main.entity.NoticeBean;
import com.mierro.main.service.NoticeService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 客户端通知模块
 * Created by tlseek on 2017/8/26.
 */
@RestController
@RequestMapping("/client")
public class ClientNoticeController {

    @Resource
    NoticeService noticeService;

    /**
     * 分页查询用户公告通知信息
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GetMapping("/notice/announcement/list")
    public ResultMessage findAnnouncementNotice(
            @RequestParam Integer pageNo,
            @RequestParam Integer pageSize,
            @ModelAttribute(SystemModelHandler.CURRENT_USER)User user) {
        Page page = noticeService.findAnnouncementNotice(user.getId(), true, pageNo, pageSize);
        return new ResultMessage(ResponseCode.OK, "操作成功").putParam("resource", page);
    }

    /**
     * 分页查询用户的个人通知
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GetMapping("/notice/personal/list")
    public ResultMessage findPersonalNotice(
            @RequestParam Integer pageNo,
            @RequestParam Integer pageSize,
            @ModelAttribute(SystemModelHandler.CURRENT_USER)User user) {
        Page page = noticeService.findPersonalNotice(user.getId(), true, pageNo, pageSize);
        return new ResultMessage(ResponseCode.OK, "操作成功").putParam("resource", page);
    }

    /**
     * 查看通知
     * @param noticeId 通知ID
     * @return
     */
    @GetMapping("/notice")
    public ResultMessage find(
            @RequestParam Long noticeId,
            @ModelAttribute(SystemModelHandler.CURRENT_USER)User user) {
        NoticeBean num = user.getId() != null ? noticeService.find(noticeId, user.getId()) : noticeService.find(noticeId);
        return new ResultMessage(ResponseCode.OK, "操作成功").putParam("resource", num);
    }

    /**
     * 用户未读通知数
     * @return
     */
    @GetMapping("/notice/hasUnread")
    public ResultMessage findPersonalNotice(
            @ModelAttribute(SystemModelHandler.CURRENT_USER)User user) {
        Long num = noticeService.hasUnreadNotice(user.getId());
        return new ResultMessage(ResponseCode.OK, "操作成功").putParam("resource", num);
    }

    /**
     * 阅读通知
     * @param noticeId 通知ID
     * @return
     */
    @PostMapping("/notice/read")
    public ResultMessage read(
            @RequestParam Long noticeId,
            @ModelAttribute(SystemModelHandler.CURRENT_USER)User user) {
        NoticeBean notice = null;
        if (user.getId() != null) {
            notice = noticeService.changeState(noticeId, user.getId(), NoticeState.READ);
        }
        return new ResultMessage(ResponseCode.OK, "操作成功").putParam("resource", notice);
    }

    /**
     * 用户未读公告通知数
     * @return
     */
    @GetMapping("/notice/announcement/hasUnread")
    public ResultMessage announcementHasUnread(
            @ModelAttribute(SystemModelHandler.CURRENT_USER)User user) {
        Long num = noticeService.hasUnreadAnnouncementNotice(user.getId());
        return new ResultMessage(ResponseCode.OK, "操作成功").putParam("resource", num);
    }
    /**
     * 用户未读个人通知数
     * @return
     */
    @GetMapping("/notice/personal/hasUnread")
    public ResultMessage personalHasUnread(
            @ModelAttribute(SystemModelHandler.CURRENT_USER)User user) {
        Long num = noticeService.hasUnreadPersonalNotice(user.getId());
        return new ResultMessage(ResponseCode.OK, "操作成功").putParam("resource", num);
    }
}
