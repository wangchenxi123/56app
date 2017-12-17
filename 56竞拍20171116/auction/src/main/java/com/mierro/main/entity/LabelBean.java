package com.mierro.main.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.mierro.common.common.IValueObject;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by 黄晓滨 simba on 2017/8/14.
 * Remarks：
 */
@Entity
@Table(name = "t_label")
public class LabelBean implements IValueObject {

    @Id
    @GeneratedValue(generator = "id")
    @GenericGenerator(name= "id",strategy = "com.mierro.common.common.KeyGenerator")
    private Long id;

    /**
     * 标签名称
     */
    private String name;

    /**
     * 标签图片
     */
    private Long picture;

    /**
     * 转跳链接
     */
    private String url;

    /**
     * 设置链接
     */
    @JSONField(serialize = false)
    private String setting_url;

    /**
     * 是否禁用
     */
    private Boolean disable;

    /**
     * 创建时间
     */
    @Column(updatable = false)
    private Date time;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPicture() {
        return picture;
    }

    public void setPicture(Long picture) {
        this.picture = picture;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSetting_url() {
        return setting_url;
    }

    public void setSetting_url(String setting_url) {
        this.setting_url = setting_url;
    }

    public Boolean getDisable() {
        return disable;
    }

    public void setDisable(Boolean disable) {
        this.disable = disable;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
