package com.mierro.authority.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.mierro.authority.common.BaseEntity;
import com.mierro.authority.common.LoginEquipment;
import com.mierro.authority.common.LoginType;
import com.mierro.common.common.IValueObject;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by lhb on 2017/1/16.
 * 用户实体类
 */
@Entity
@Table(name = "t_user",uniqueConstraints = {@UniqueConstraint(columnNames={"refreshToken"})})
public class User extends BaseEntity<Long> implements IValueObject {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(generator = "id")
    @GenericGenerator(name= "id",strategy = "com.mierro.common.common.KeyGenerator")
    private Long id;

    /**
     * 最后登录时间
     */
    private Date finalLogin;

    /**
     * 禁用标志位
     */
    private Boolean disable;

    /**
     * 用来刷新session的token
     */
    private String refreshToken;

    /**
     * token过期时间
     */
    private Date expirationTime;

    /**
     * 登录设备
     */
    private LoginEquipment loginEquipment;

    /**
     * 加密token的盐值
     */
    @JSONField(serialize=false)
    private String salt;

    @Transient
    private String username;

    @Transient
    private String phone;

    /**
     * 无参构造函数
     */
    public User() {
    }

    public User(User user, Object temp) {

        id = user.getId();
        finalLogin = user.getFinalLogin();
        disable = user.getDisable();

        System.out.println();
        if (temp.getClass().equals(AuthenticationInfo.class)){
            AuthenticationInfo authenticationInfo = (AuthenticationInfo) temp;
            if (user.getId().equals(authenticationInfo.getUserId())){
                if (authenticationInfo.getVoucherType().equals(LoginType.TELEPHONE)){
                    phone =authenticationInfo.getIdentifier();
                }else if(authenticationInfo.getVoucherType().equals(LoginType.ACCOUNT)){
                    username= authenticationInfo.getIdentifier();
                }
            }
        }else{
            Object[] object = (Object[]) temp;
            AuthenticationInfo authenticationInfo = (AuthenticationInfo) object[0];
            if (user.getId().equals(authenticationInfo.getUserId())){
                if (authenticationInfo.getVoucherType().equals(LoginType.TELEPHONE)){
                    phone =authenticationInfo.getIdentifier();
                }else if(authenticationInfo.getVoucherType().equals(LoginType.ACCOUNT)){
                    username= authenticationInfo.getIdentifier();
                }
            }
            if (object.length > 1){
                authenticationInfo = (AuthenticationInfo) object[1];
                if (user.getId().equals(authenticationInfo.getUserId())){
                    if (authenticationInfo.getVoucherType().equals(LoginType.TELEPHONE)){
                        phone =authenticationInfo.getIdentifier();
                    }else if(authenticationInfo.getVoucherType().equals(LoginType.ACCOUNT)){
                        username= authenticationInfo.getIdentifier();
                    }
                }
            }
        }
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(Date expirationTime) {
        this.expirationTime = expirationTime;
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

    public Date getFinalLogin() {
        return finalLogin;
    }

    public void setFinalLogin(Date finalLogin) {
        this.finalLogin = finalLogin;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Boolean getDisable() {
        return disable;
    }

    public void setDisable(Boolean disable) {
        this.disable = disable;
    }

    public LoginEquipment getLoginEquipment() {
        return loginEquipment;
    }

    public void setLoginEquipment(LoginEquipment loginEquipment) {
        this.loginEquipment = loginEquipment;
    }
}
