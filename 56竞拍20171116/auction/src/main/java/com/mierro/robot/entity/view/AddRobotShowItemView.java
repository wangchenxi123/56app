package com.mierro.robot.entity.view;

import com.mierro.common.common.IValueObject;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 添加机器人晒单视图
 * Created by tlseek on 2017/8/24.
 */
public class AddRobotShowItemView implements IValueObject {

    /**
     * 所属中奖记录id
     */
    @NotNull
    private Long sealedId;
    /**
     * 晒单标题
     */
    @NotNull
    private String title;
    /**
     * 评论内容
     */
    @NotNull
    private String context;
    /**
     * 评分
     */
    @NotNull
    @Min(1)
    @Max(5)
    private Integer grade;
    /**
     * 评论附加图片
     */
    private List<Long> pictureList;


    public Long getSealedId() {
        return sealedId;
    }

    public void setSealedId(Long sealedId) {
        this.sealedId = sealedId;
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

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public List<Long> getPictureList() {
        return pictureList;
    }

    public void setPictureList(List<Long> pictureList) {
        this.pictureList = pictureList;
    }

}
