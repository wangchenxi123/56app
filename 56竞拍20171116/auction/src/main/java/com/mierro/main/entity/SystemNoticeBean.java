package com.mierro.main.entity;

import com.mierro.common.common.IValueObject;
import com.mierro.main.common.NoticeState;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 系统通知, 需要管理员处理
 * Created by tlseek on 2017/8/25.
 */
@Entity
@Table(name = "t_system_notice")
public class SystemNoticeBean implements IValueObject {

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
     * 通知状态
     */
    private NoticeState state;


    /**
     * 处理者
     */
    private Long userId;
    /**
     * 处理者
     */
    private Date time;


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

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
