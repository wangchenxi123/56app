package com.mierro.main.service;

import com.mierro.main.common.NoticeState;
import com.mierro.main.entity.NoticeBean;
import org.springframework.data.domain.Page;

/**
 * 通知业务接口
 * Created by tlseek on 2017/8/25.
 */
public interface NoticeService {

    /**
     * 分页查询公告通知
     * @param pageNo
     * @param pageSize
     * @return
     */
    Page<NoticeBean> findAnnouncementNotice(Integer pageNo, Integer pageSize);

    /**
     * 分页查询用户公告通知信息
     * @param isSend 是否发送
     * @param pageNo
     * @param pageSize
     * @return
     */
    Page<NoticeBean> findAnnouncementNotice(Long userId, Boolean isSend, Integer pageNo, Integer pageSize);

    /**
     * 分页查询用户的个人通知
     * @param userId 用户ID
     * @param isSend 是否发送
     * @param pageNo
     * @param pageSize
     * @return
     */
    Page<NoticeBean> findPersonalNotice(Long userId, Boolean isSend, Integer pageNo, Integer pageSize);

    /**
     * 获取通知
     * @param noticeId
     */
    NoticeBean find(Long noticeId);

    /**
     * 获取通知和对应用户的状态
     * @param noticeId
     */
    NoticeBean find(Long noticeId, Long userId);

    /**
     * 添加公告通知
     * @param title 标题
     * @param context 内容
     * @return
     */
    NoticeBean addAnnouncement(String title, String context);

    /**
     * 添加个人通知
     * @param title 标题
     * @param context 内容
     * @param userId 用户ID
     * @return
     */
    NoticeBean addPersonal(String title, String context, Long userId);

    /**
     * 编辑通知
     * @param notice
     * @return
     */
    NoticeBean edit(NoticeBean notice);

    /**
     * 修改通知状态
     * @param noticeId 通知ID
     * @param userId
     * @param state
     */
    NoticeBean changeState(Long noticeId, Long userId, NoticeState state);

    /**
     * 发送通知
     * @param noticeId 通知ID
     */
    NoticeBean sendNotice(Long noticeId);

    /**
     * 用户未读通知数
     * @param userId
     * @return 未读通知个数
     */
    Long hasUnreadNotice(Long userId);

    /**
     * 删除通知
     * @param noticeId 通知ID
     */
    void delete(Long noticeId);

    /**
     * 用户未读公告通知数
     * @param userId
     * @return 未读通知个数
     */
    Long hasUnreadAnnouncementNotice(Long userId);

    /**
     * 用户未读通知数
     * @param userId
     * @return 未读通知个数
     */
    Long hasUnreadPersonalNotice(Long userId);
}
