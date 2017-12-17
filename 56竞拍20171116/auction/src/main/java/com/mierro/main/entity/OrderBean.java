package com.mierro.main.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.mierro.common.common.IValueObject;
import com.mierro.main.common.ItemType;
import com.mierro.main.common.OrderState;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by 黄晓滨 simba on 2017/8/14.
 * Remarks：
 */
@Entity
@Table(name = "t_order")
public class OrderBean implements IValueObject {

    @Id
    @GeneratedValue(generator = "id")
    @GenericGenerator(name= "id",strategy = "com.mierro.common.common.KeyGenerator")
    private Long id;

    /**
     * 商品id
     */
    @NotNull(message = "缺少商品id")
    private Long item_id;

    /**
     * 商品名称
     */
    private String item_name;

    /**
     * 商品图片
     */
    private String item_picture;

    /**
     * 成交价格
     */
    @NotNull(message = "成交价格不能为空")
    private String price;

    /**
     * 应付金额
     */
    @NotNull(message = "应付金额不能为空")
    private String amounts;

    /**
     * 市场价
     */
    @NotNull(message = "市场价不能为空")
    private String marketPrice;

    /**
     * 奖品状态
     */
    private OrderState order_state;

    /**
     * 商品类型
     */
    @NotNull(message = "需要选择商品类型")
    private ItemType item_type;

    /**
     * 中奖时间
     */
    @Column(updatable = false)
    private Date time;

    /**
     * 封存记录id
     */
    @NotNull(message = "对应封存记录id不能为空")
    private Long record_id;

    /**
     * 收货地址id
     */
    private Long receipt_id;

    /**
     * 收货地址id
     */
    @JSONField(serialize = false)
    private String receiptString;

    /**
     * 中奖人id
     */
    @NotNull(message = "中奖人id不能为空")
    private Long userId;

    /**
     * 中奖人名称
     */
    private String name;

    /**
     * 快递编号
     */
    private String express_number;

    /**
     * 卡号
     */
    private String card;

    /**
     * 卡密
     */
    private String density;

    /**
     * 付款时间
     */
    private Date paymentTime;

    /**
     * 发货时间
     */
    private Date shipTime;

    @Transient
    private AddressBean addressBean;

    public OrderBean(){}

    public OrderBean(Date time,String name,String picture,String marketPrice,String price,Long item_id,Long record_id,String item_name){
        this.item_id = item_id;
        this.time = time;
        this.name = name;
        this.item_picture = picture;
        this.marketPrice = marketPrice;
        this.price = price;
        this.record_id = record_id;
        this.item_name = item_name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_picture() {
        return item_picture;
    }

    public void setItem_picture(String item_picture) {
        this.item_picture = item_picture;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public ItemType getItem_type() {
        return item_type;
    }

    public void setItem_type(ItemType item_type) {
        this.item_type = item_type;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Long getRecord_id() {
        return record_id;
    }

    public void setRecord_id(Long record_id) {
        this.record_id = record_id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getExpress_number() {
        return express_number;
    }

    public void setExpress_number(String express_number) {
        this.express_number = express_number;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public String getDensity() {
        return density;
    }

    public void setDensity(String density) {
        this.density = density;
    }

    public OrderState getOrder_state() {
        return order_state;
    }

    public void setOrder_state(OrderState order_state) {
        this.order_state = order_state;
    }

    public AddressBean getAddressBean() {
        return addressBean;
    }

    public void setAddressBean(AddressBean addressBean) {
        this.addressBean = addressBean;
    }

    public Long getItem_id() {
        return item_id;
    }

    public void setItem_id(Long item_id) {
        this.item_id = item_id;
    }

    public Long getReceipt_id() {
        return receipt_id;
    }

    public void setReceipt_id(Long receipt_id) {
        this.receipt_id = receipt_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(String marketPrice) {
        this.marketPrice = marketPrice;
    }

    public String getAmounts() {
        return amounts;
    }

    public void setAmounts(String amounts) {
        this.amounts = amounts;
    }

    public String getReceiptString() {
        return receiptString;
    }

    public void setReceiptString(String receiptString) {
        this.receiptString = receiptString;
    }

    public Date getShipTime() {
        return shipTime;
    }

    public void setShipTime(Date shipTime) {
        this.shipTime = shipTime;
    }

    public Date getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(Date paymentTime) {
        this.paymentTime = paymentTime;
    }
}
