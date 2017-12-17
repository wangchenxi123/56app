package com.mierro.main.controller.admin;

import com.mierro.common.common.DataCheck;
import com.mierro.common.common.ResponseCode;
import com.mierro.common.common.ResultMessage;
import com.mierro.main.entity.NoticeBean;
import com.mierro.main.entity.valid.Update;
import com.mierro.main.service.NoticeService;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 管理端通知模块控制层
 * Created by tlseek on 2017/8/26.
 */
@RestController
@RequestMapping("/admin")
public class AdminNoticeController {

    @Resource
    NoticeService noticeService;

    /**
     * 分页查看公告通知列表
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GetMapping("/notice/announcement/list")
    public ResultMessage findAnnouncementNotice(
            @RequestParam("pageNo") Integer pageNo,
            @RequestParam("pageSize") Integer pageSize) {
        Page page = noticeService.findAnnouncementNotice(pageNo, pageSize);
        return new ResultMessage(ResponseCode.OK, "操作成功").putParam("resource", page);
    }

    /**
     * 添加公告通知
     * @param title 标题
     * @param context 内容
     * @return
     */
    @PostMapping("/notice/announcement")
    public ResultMessage addAnnouncement(
            @RequestParam String title,
            @RequestParam String context) {
        NoticeBean bean = noticeService.addAnnouncement(title, context);
        return new ResultMessage(ResponseCode.OK, "操作成功").putParam("resource", bean);
    }


    /**
     * 分页查询用户的个人通知
     * @param userId 用户ID
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GetMapping("/notice/personal/list")
    public ResultMessage findPersonalNotice(
            @RequestParam Long userId,
            @RequestParam Integer pageNo,
            @RequestParam Integer pageSize) {
        Page page = noticeService.findPersonalNotice(userId, null, pageNo, pageSize);
        return new ResultMessage(ResponseCode.OK, "操作成功").putParam("resource", page);
    }

    /**
     * 添加个人通知
     * @param title
     * @param context
     * @param userId
     * @return
     */
    @PostMapping("/notice")
    public ResultMessage add(
            @RequestParam String title,
            @RequestParam String context,
            @RequestParam Long userId) {
        NoticeBean bean = noticeService.addPersonal(title, context, userId);
        return new ResultMessage(ResponseCode.OK, "操作成功").putParam("resource", bean);
    }

    /**
     * 编辑通知
     * @param notice 通知对象
     * @return
     */
    @PutMapping("/notice")
    public ResultMessage edit(
            /*@RequestBody*/ @ModelAttribute("notice") @Validated({Update.class}) NoticeBean notice, BindingResult result) {
        DataCheck.returnError(result);
        NoticeBean bean = noticeService.edit(notice);
        return new ResultMessage(ResponseCode.OK, "操作成功").putParam("resource", bean);
    }

    /**
     * 发送通知
     * @param noticeId 通知ID
     * @return
     */
    @PostMapping("/notice/send")
    public ResultMessage send(
            @RequestParam Long noticeId) {
        NoticeBean bean = noticeService.sendNotice(noticeId);
        return new ResultMessage(ResponseCode.OK, "操作成功").putParam("resource", bean);
    }

    /**
     * 删除通知
     * @param noticeId 通知ID
     * @return
     */
    @DeleteMapping("/notice")
    public ResultMessage delete(@RequestParam Long noticeId) {
        noticeService.delete(noticeId);
        return new ResultMessage(ResponseCode.OK, "操作成功");
    }

}
