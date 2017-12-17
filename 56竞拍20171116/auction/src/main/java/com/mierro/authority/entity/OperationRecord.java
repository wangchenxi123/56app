package com.mierro.authority.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by Simba on 2017/2/21.
 * 操作记录表
 */
@Entity
@Table(name = "t_operating_record")
public class OperationRecord {

    /**
     * 主键
     */
    @Id
    @GeneratedValue(generator = "id")
    @GenericGenerator(name= "id",strategy = "com.mierro.common.common.KeyGenerator")
    private Long id;

    @NotNull(message="无法获取你的userid")
    private String userId;

    private String username;

    private Date operationTime;

    private String ip;

    @Column(columnDefinition="TEXT")
    private String message;

    private String iphone;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

//    public Long getUserId() {
//        return userId;
//    }
//
//    public void setUserId(Long userId) {
//        this.userId = userId;
//    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(Date operationTime) {
        this.operationTime = operationTime;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getIphone() {
        return iphone;
    }

    public void setIphone(String iphone) {
        this.iphone = iphone;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
