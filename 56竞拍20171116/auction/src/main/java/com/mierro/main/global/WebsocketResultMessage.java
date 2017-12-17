package com.mierro.main.global;

import com.mierro.main.common.WebsocketMessageType;

import java.util.Date;

/**
 * Created by 黄晓滨 simba on 2017/8/10.
 * Remarks：
 */
public class WebsocketResultMessage {

    /**
     * 推送的信息类型
     */
    private WebsocketMessageType message_type;

    /**
     * 状态
     */
    private Integer code;

    /**
     * 商品id
     */
    private String itemId;

    /**
     * 商品id
     */
    private String itemName;

    /**
     * 所属用户id
     */
    private String userId;

    /**
     * 用户名称
     */
    private String username;

    /**
     * 用户头像
     */
    private String head_pic;

    /**
     * 地址
     */
    private String address;

    /**
     * 价格
     */
    private String price;

    /**
     * 推送时间
     */
    private Date time;

    /**
     * 剩余次数
     */
    private Integer number;

    /**
     * 备注信息
     */
    private String message;

    public WebsocketMessageType getMessage_type() {
        return message_type;
    }

    public void setMessage_type(WebsocketMessageType message_type) {
        this.message_type = message_type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHead_pic() {
        return head_pic;
    }

    public void setHead_pic(String head_pic) {
        this.head_pic = head_pic;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
}
