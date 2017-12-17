package com.mierro.main.service;

import com.mierro.main.common.NoticeState;
import com.mierro.main.entity.SystemNoticeBean;
import org.springframework.data.domain.Page;

/**
 * 系统通知接口
 * Created by tlseek on 2017/8/25.
 */
public interface SystemNoticeService {

    /**
     * 分页查看系统通知
     * @param state 状态
     * @param pageNo
     * @param pageSize
     * @return
     */
    Page<SystemNoticeBean> find(NoticeState state, Integer pageNo, Integer pageSize);

    /**
     * 添加通知
     * @param title
     * @param context
     * @return
     */
    SystemNoticeBean add(String title, String context);

    /**
     * 修改状态
     * @param noticeId 通知ID
     * @param userId 用户ID
     * @param state 状态
     * @return
     */
    SystemNoticeBean changeState(Long noticeId, Long userId, NoticeState state);

}
