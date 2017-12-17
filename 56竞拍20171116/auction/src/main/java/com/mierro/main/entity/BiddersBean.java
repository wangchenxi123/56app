package com.mierro.main.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.mierro.common.common.IValueObject;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by 黄晓滨 simba on 2017/8/9.
 * Remarks：
 */
@Entity
@Table(name = "t_bidders")
public class BiddersBean implements IValueObject {

    @Id
    @GeneratedValue(generator = "id")
    @GenericGenerator(name= "id",strategy = "com.mierro.common.common.KeyGenerator")
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户头像
     */
    private Long avatar;

    /**
     * 用户名
     */
    private String name;

    /**
     * 地址
     */
    private String address;

    /**
     * 当前价格
     */
    private String price;

    /**
     * 操作时间
     */
    @Column(updatable = false)
    private Date time;

    /**
     * 商品id
     */
    private Long itemId;

    /**
     * 封存记录id
     */
    @JSONField(serialize = false)
    private Long sealedId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getAvatar() {
        return avatar;
    }

    public void setAvatar(Long avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Long getSealedId() {
        return sealedId;
    }

    public void setSealedId(Long sealedId) {
        this.sealedId = sealedId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }
}
