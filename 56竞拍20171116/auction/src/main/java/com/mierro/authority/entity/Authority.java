package com.mierro.authority.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.mierro.authority.common.BaseEntity;
import com.mierro.common.common.IValueObject;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by lhb on 2016/11/24.
 * 权限实体类
 */
@Entity
@Table(name = "t_function")
public class Authority extends BaseEntity<Long> implements IValueObject {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(generator = "id")
    @GenericGenerator(name= "id",strategy = "com.mierro.common.common.KeyGenerator")
    private Long id;

    /**
     * 功能标识（英文）
     */
    @NotNull(message = "必须要有identification参数")
    @Size(max = 255,message = "identification参数过长，限制为255个字符")
    private String identification;
    /**
     * 功能描述
     */
    @NotNull(message = "必须要有description参数")
    private String description;
    /**
     * 功能对应的URL
     */
    @Column(unique = true)
    @NotNull(message = "必须要有urlStr参数")
    private String urlStr;
    /**
     * 禁用标志位
     */
    @JSONField(serialize=false)
    @Column(columnDefinition = "bit default false")
    private Boolean disable;

    /**
     * 请求方式
     */
    @NotNull(message = "必须要有requestMethod参数")
    private String requestMethod;

    /**
     * 无参构造函数
     */
    public Authority() {
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrlStr() {
        return urlStr;
    }

    public void setUrlStr(String urlStr) {
        this.urlStr = urlStr;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public Boolean getDisable() {
        return disable;
    }

    public void setDisable(Boolean disable) {
        this.disable = disable;
    }
}
