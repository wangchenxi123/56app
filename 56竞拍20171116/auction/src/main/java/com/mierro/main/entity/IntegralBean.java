package com.mierro.main.entity;

import com.mierro.common.common.IValueObject;
import com.mierro.main.common.OperationalType;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by 黄晓滨 simba on 2017/7/29.
 * Remarks：积分实体类
 */
@Entity
@Table(name = "t_integral")
public class IntegralBean implements IValueObject {

    /**
     * 积分id
     */
    @Id
    @GeneratedValue(generator = "id")
    @GenericGenerator(name= "id",strategy = "com.mierro.common.common.KeyGenerator")
    private Long id;

    /**
     * 所属用户id
     */
    @NotNull(message = "请传入所属用户id")
    private Long userId;

    /**
     * 数值(分正负)
     */
    @NotNull(message = "请传入数值")
    private Integer number;

    /**
     * 生成时间
     */
    @Column(updatable = false,columnDefinition = "TIMESTAMP")
    private Date time;

    /**
     * 操作原因
     */
    private String reason;

    /**
     * 操作类型
     */
    private OperationalType operationalType;

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

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public OperationalType getOperationalType() {
        return operationalType;
    }

    public void setOperationalType(OperationalType operationalType) {
        this.operationalType = operationalType;
    }
}
