package com.mierro.authority.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.mierro.authority.common.BaseEntity;
import com.mierro.authority.common.LoginType;
import com.mierro.common.common.IValueObject;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by lhb on 2016/12/20.
 * 登录信息实体类
 */
@Entity
@Table(name = "t_login_info",uniqueConstraints = {@UniqueConstraint(columnNames={"voucherType","identifier"})})
public class AuthenticationInfo extends BaseEntity<Long> implements IValueObject,Serializable{

    /**
     * 主键
     */
    @Id
    @GeneratedValue(generator = "id")
    @GenericGenerator(name= "id",strategy = "com.mierro.common.common.KeyGenerator")
    private Long id;

    /**
     * 用户表Id
     */
    private Long userId;
     /**
     * 凭证来源类型
     * example: weibo wechat qq telephone email username
     */
    private LoginType voucherType;
    /**
     * 登录标识
     */
    @Column(length = 190)
    private String identifier;
    /**
     * 登录凭证
     */
    @JSONField(serialize=false)
    private String credential;
    /**
     * 盐值
     */
    @JSONField(serialize=false)
    private String salt;

    /**
     * 无参构造函数
     */
    public AuthenticationInfo() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getCredential() {
        return credential;
    }

    public void setCredential(String credential) {
        this.credential = credential;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    @Enumerated(EnumType.ORDINAL)
    public LoginType getVoucherType() {
        return voucherType;
    }

    public void setVoucherType(LoginType voucherType) {
        this.voucherType = voucherType;
    }
}
