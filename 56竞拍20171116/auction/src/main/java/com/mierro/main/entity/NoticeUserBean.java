package com.mierro.main.entity;

import com.mierro.main.common.NoticeState;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * 用户有关通知的信息
 * Created by tlseek on 2017/8/25.
 */
@Entity
@Table(name = "t_notice_user")
public class NoticeUserBean {


    @Id
    @GeneratedValue(generator = "id")
    @GenericGenerator(name= "id",strategy = "com.mierro.common.common.KeyGenerator")
    private Long id;
    /**
     * 通知状态
     */
    private NoticeState state;
    /**
     * 所属用户
     */
    @NotNull
    private Long userId;
    /**
     * 所属用户
     */
    @NotNull
    private Long noticeId;

    public NoticeUserBean() {
    }

    public NoticeUserBean(NoticeState state, Long userId, Long noticeId) {
        this.state = state;
        this.userId = userId;
        this.noticeId = noticeId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public NoticeState getState() {
        return state;
    }

    public void setState(NoticeState state) {
        this.state = state;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(Long noticeId) {
        this.noticeId = noticeId;
    }
}
