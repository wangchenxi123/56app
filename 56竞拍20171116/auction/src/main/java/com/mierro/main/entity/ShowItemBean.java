package com.mierro.main.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.mierro.common.common.IValueObject;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * Created by 黄晓滨 simba on 2017/7/31.
 * Remarks：用户晒单
 */
@Entity
@Table(name = "t_show_item")
public class ShowItemBean implements IValueObject {

    @Id
    @GeneratedValue(generator = "id")
    @GenericGenerator(name= "id",strategy = "com.mierro.common.common.KeyGenerator")
    private Long id;

    /**
     * 所属用户id
     */

    private Long userId;

    /**
     * 所属用户名称
     */
    private String username;

    /**
     * 用户头像
     */
    private Long head_pic;

    /**
     * 晒单标题
     */
    @NotNull
    private String title;

    /**
     * 商品id
     */
    private Long itemId;

    /**
     * 商品名称
     */
    private String itemName;

    /**
     * 评论内容
     */
    @NotNull
    @Column(length = 625)
    @Length(max = 625,message = "您的评论过长")
    private String context;

    /**
     * 评论附加图片
     */
    @Transient
    @JSONField(name = "pictureList")
    private List<Long> pictureList;

    /**
     * 评论附加图片Json
     */
    @JSONField(serialize = false)
    @Column(length = 75)
//    @Length(min = 18,max = 70,message = "至少需要一个图片，最大只能3张图片")
    private String pictures;

    /**
     * 活动快捷入口
     */
    private String fastUrl;

    /**
     * 评分
     */
    @NotNull
    @Min(value = 1,message = "最小值为1")
    @Max(value = 5,message = "最大值为5")
    private Integer grade;

    /**
     * 评论时间
     */
    @Column(columnDefinition="TIMESTAMP",updatable = false)
    private Date time;

    /**
     * 所属订单id
     */
    @NotNull
    private Long orderId;

    /**
     * 是否前台展示
     */
    private Boolean disable;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public List<Long> getPictureList() {
        return pictureList;
    }

    public void setPictureList(List<Long> pictureList) {
        this.pictureList = pictureList;
    }

    public String getPictures() {
        return pictures;
    }

    public void setPictures(String pictures) {
        this.pictures = pictures;
    }

    public String getFastUrl() {
        return fastUrl;
    }

    public void setFastUrl(String fastUrl) {
        this.fastUrl = fastUrl;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Long getHead_pic() {
        return head_pic;
    }

    public void setHead_pic(Long head_pic) {
        this.head_pic = head_pic;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Boolean getDisable() {
        return disable;
    }

    public void setDisable(Boolean disable) {
        this.disable = disable;
    }
}
