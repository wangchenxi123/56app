package com.mierro.main.entity;

import com.mierro.common.common.IValueObject;
import com.mierro.main.common.QuestionCategory;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by 黄晓滨 simba on 2017/8/15.
 * Remarks：
 */
@Entity
@Table(name = "t_question")
public class CommonQuestionBean implements IValueObject {

    @Id
    @GeneratedValue(generator = "id")
    @GenericGenerator(name= "id",strategy = "com.mierro.common.common.KeyGenerator")
    private Long id;

    /**
     * 标题
     */
    @NotNull
    private String title;

    /**
     * 内容
     */
    @NotNull
    @Column(columnDefinition="TEXT")
    private String context;

    /**
     * 时间
     */
    @Column(updatable = false)
    private Date time;

    /**
     * 类型
     */
    @NotNull
    private QuestionCategory type;

    /**
     * 是否可见
     */
    private Boolean disable;

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

    public QuestionCategory getType() {
        return type;
    }

    public void setType(QuestionCategory type) {
        this.type = type;
    }

    public Boolean getDisable() {
        return disable;
    }

    public void setDisable(Boolean disable) {
        this.disable = disable;
    }
}
