package com.mierro.main.entity;

import com.mierro.common.common.IValueObject;
import com.mierro.main.common.CoinSource;
import com.mierro.main.common.CoinType;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by 黄晓滨 simba on 2017/7/29.
 * Remarks：钱币实体类
 */
@Entity
@Table(name = "t_coin")
public class CoinBean implements IValueObject {

    /**
     * 钱币id
     */
    @Id
    @GeneratedValue(generator = "id")
    @GenericGenerator(name= "id",strategy = "com.mierro.common.common.KeyGenerator")
    private Long id;

    /**
     * 所属用户
     */
    @NotNull(message = "必须填写所属用户")
    private Long userId;

    /**
     * 数值(分正负)
     */
    @NotNull(message = "缺少数值")
    private Integer number;

    /**
     * 操作原因
     */
    @NotNull(message = "缺少操作原因")
    @Column(length = 1024)
    private String reason;

    /**
     * 操作原因识别码
     */
    @NotNull(message = "必须输入拍币来源")
    private CoinSource source;

    /**
     * 钱币类型
     */
    @NotNull(message = "必须选择钱币类型")
    private CoinType coinType;

    /**
     * 是否为推广收入
     */
    private Boolean promotion;

    /**
     * 生成时间
     */
    @Column(updatable = false)
    private Date time;

    /**
     * 资金去处(一般为投入商品)
     */
    private Long place;

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

    public CoinType getCoinType() {
        return coinType;
    }

    public void setCoinType(CoinType coinType) {
        this.coinType = coinType;
    }

    public Boolean getPromotion() {
        return promotion;
    }

    public void setPromotion(Boolean promotion) {
        this.promotion = promotion;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Long getPlace() {
        return place;
    }

    public void setPlace(Long place) {
        this.place = place;
    }

    public CoinSource getSource() {
        return source;
    }

    public void setSource(CoinSource source) {
        this.source = source;
    }
}
