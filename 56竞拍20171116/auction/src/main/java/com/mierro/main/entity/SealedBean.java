package com.mierro.main.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.mierro.authority.entity.UserMessage;
import com.mierro.main.common.SealType;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by 黄晓滨 simba on 2017/8/15.
 * Remarks：
 */
@Entity
@Table(name = "t_sealed")
public class SealedBean {

    @Id
    @GeneratedValue(generator = "id")
    @GenericGenerator(name= "id",strategy = "com.mierro.common.common.KeyGenerator")
    private Long id;

    /**
     * 商品缓存
     */
    @JSONField(serialize = false)
    @Column(columnDefinition="TEXT")
    private String item;

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
     * 成交价格
     */
    private String market_price;

    /**
     * 成本
     */
    private String cost;

    /**
     * 总投票数
     */
    private String total;

    /**
     * 有效投票数
     */
    private String income;

    /**
     * 利润
     */
    private String profit;

    /**
     * 中奖人信息
     */
    @JSONField(serialize = false)
    @Column(columnDefinition="TEXT")
    private String user;

    /**
     * 中奖人名称
     */
    private String name;

    /**
     * 中奖人名称
     */
    private Long userId;

    /**
     * 活动开始时间
     */
    private Date stateTime;
    /**
     * 保存时间
     */
    @Column(updatable = false)
    private Date time;

    /**
     * 是否是机器人
     */
    private Boolean robot;

    /**
     * 期数
     */
    private Integer periods;

    @Transient
    private ItemBean itemMessage;

    @Transient
    private UserMessage userMessage;

    @Transient
    private Integer number;

    @Transient
    private Boolean sunSingle;

    @Transient
    private Integer count;

    /**
     * 状态
     */
    private SealType sealType;

    /**
     * 是否封存
     */
    private Boolean isSealed;

    public SealedBean(){}

    public SealedBean(SealedBean sealedBean, Long count){
        this.count = count.intValue();
        this.setItemName(sealedBean.getItemName());
        this.setItemId(sealedBean.getItemId());
        this.setId(sealedBean.getId());
        this.setUserId(sealedBean.getUserId());
        this.isSealed = sealedBean.isSealed;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Long getPicture() {
        return picture;
    }

    public void setPicture(Long picture) {
        this.picture = picture;
    }

    public String getMarket_price() {
        return market_price;
    }

    public void setMarket_price(String market_price) {
        this.market_price = market_price;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getProfit() {
        return profit;
    }

    public void setProfit(String profit) {
        this.profit = profit;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Boolean getRobot() {
        return robot;
    }

    public void setRobot(Boolean robot) {
        this.robot = robot;
    }

    public Date getStateTime() {
        return stateTime;
    }

    public void setStateTime(Date stateTime) {
        this.stateTime = stateTime;
    }

    public ItemBean getItemMessage() {
        return itemMessage;
    }

    public void setItemMessage(ItemBean itemMessage) {
        this.itemMessage = itemMessage;
    }

    public UserMessage getUserMessage() {
        return userMessage;
    }

    public void setUserMessage(UserMessage userMessage) {
        this.userMessage = userMessage;
    }

    public Integer getPeriods() {
        return periods;
    }

    public void setPeriods(Integer periods) {
        this.periods = periods;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Boolean getSunSingle() {
        return sunSingle;
    }

    public void setSunSingle(Boolean sunSingle) {
        this.sunSingle = sunSingle;
    }

    public SealType getSealType() {
        return sealType;
    }

    public void setSealType(SealType sealType) {
        this.sealType = sealType;
    }

    public Boolean getSealed() {
        return isSealed;
    }

    public void setSealed(Boolean sealed) {
        isSealed = sealed;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
