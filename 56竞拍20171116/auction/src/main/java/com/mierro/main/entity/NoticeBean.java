package com.mierro.main.entity;

import com.mierro.common.common.IValueObject;
import com.mierro.main.common.NoticeState;
import com.mierro.main.common.NoticeType;
import com.mierro.main.entity.valid.Update;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by 黄晓滨 simba on 2017/8/14.
 * Remarks：通知
 */
@Entity
@Table(name = "t_notice")
public class NoticeBean implements IValueObject {

    @NotNull(groups = {Update.class})
    @Id
    @GeneratedValue(generator = "id")
    @GenericGenerator(name= "id",strategy = "com.mierro.common.common.KeyGenerator")
    private Long id;

    /**
     * 通知标题
     */
    @NotNull
    @Length(max = 255,min = 1,message = "标题最多255个字符，最少1个字符")
    private String title;

    /**
     * 通知内容
     */
    @NotNull(message = "通知内容是必要的")
    @Column(columnDefinition="TEXT",updatable = false)
    private String context;
    /**
     * 创建时间
     */
    @Column(updatable = false)
    private Date time;
    /**
     * 通知是否发送
     */
    private Boolean isSend;
    /**
     * 通知类型
     */
    private NoticeType type;

    // ---------------动态属性-----------------
    /**
     * 所属用户
     */
    @Transient
    private Long userId;
    /**
     * 通知状态
     */
    @Transient
    private NoticeState state;

    public NoticeBean() {
    }

    public NoticeBean(NoticeBean notice, NoticeState state, Long userId) {
        this(notice);
        this.state = state;
        this.userId = userId;
    }

    private NoticeBean(NoticeBean notice) {
        this.id = notice.id;
        this.title = notice.title;
        this.context = notice.context;
        this.time = notice.time;
        this.isSend = notice.isSend;
        this.type = notice.type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Boolean getIsSend() {
        return isSend;
    }

    public void setIsSend(Boolean send) {
        isSend = send;
    }

    public NoticeType getType() {
        return type;
    }

    public void setType(NoticeType type) {
        this.type = type;
    }

    public NoticeState getState() {
        return state;
    }

    public void setState(NoticeState state) {
        this.state = state;
    }
}
