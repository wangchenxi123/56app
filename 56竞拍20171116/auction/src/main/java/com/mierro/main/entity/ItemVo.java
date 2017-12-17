package com.mierro.main.entity;

import com.mierro.common.common.IValueObject;
import com.mierro.main.common.SealType;

import java.util.Date;

/**
 * Created by 黄晓滨 simba on 2017/8/9.
 * Remarks：
 */
public class ItemVo implements IValueObject {

    private Long id;

    private ItemBean itemBean;

    /**
     * 期数
     */
    private Integer number_periods;

    /**
     * 当前价格
     */
    private String price;

    /**
     * 商品名称
     */
    private String itemName;

    /**
     * 商品id
     */
    private Long itemId;

    /**
     * 商品小图片
     */
    private Long picture;

    /**
     * 即时时间
     */
    private Date instant_time;

    /**
     * 状态
     */
    private SealType sealType;

    /**
     * 是否进入结算
     */
    private Boolean settlement;

    /**
     * 中奖人用户名
     */
    private String username;

    /**
     * 订单状态
     */
    private OrderBean order;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ItemBean getItemBean() {
        return itemBean;
    }

    public void setItemBean(ItemBean itemBean) {
        this.itemBean = itemBean;
    }

    public Integer getNumber_periods() {
        return number_periods;
    }

    public void setNumber_periods(Integer number_periods) {
        this.number_periods = number_periods;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Long getPicture() {
        return picture;
    }

    public void setPicture(Long picture) {
        this.picture = picture;
    }

    public Date getInstant_time() {
        return instant_time;
    }

    public void setInstant_time(Date instant_time) {
        this.instant_time = instant_time;
    }

    public SealType getSealType() {
        return sealType;
    }

    public void setSealType(SealType sealType) {
        this.sealType = sealType;
    }

    public Boolean getSettlement() {
        return settlement;
    }

    public void setSettlement(Boolean settlement) {
        this.settlement = settlement;
    }

    public OrderBean getOrder() {
        return order;
    }

    public void setOrder(OrderBean order) {
        this.order = order;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
