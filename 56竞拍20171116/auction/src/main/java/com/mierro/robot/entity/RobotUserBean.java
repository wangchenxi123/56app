package com.mierro.robot.entity;

import com.mierro.authority.entity.UserMessage;
import com.mierro.common.common.IValueObject;
import com.mierro.robot.entity.e.RobotUserState;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 机器人用户对象
 * Created by tlseek on 2017/8/22.
 */
@Entity
@Table(name = "t_robot_user")
public class RobotUserBean implements IValueObject {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(generator = "id")
    @GenericGenerator(name= "id",strategy = "com.mierro.common.common.KeyGenerator")
    private Long id;

    /**机器人状态*/
    private RobotUserState state;

    // -------------------- 临时变量 --------------------

    /**在当前商品中已竞拍次数*/
    @Transient
    private int auctionCount;
    /**在当前商品中最大竞拍次数, 超过后退出当前商品竞拍*/
    @Transient
    private int maxAuctionCount;
    /**用户信息*/
    @Transient
    private UserMessage userMessage;

    public RobotUserBean() {
    }

    public RobotUserBean(Long id, RobotUserState state) {
        this.id = id;
        this.state = state;
    }

    public RobotUserBean(Long id, RobotUserState state, UserMessage userMessage) {
        this.id = id;
        this.state = state;
        this.userMessage = userMessage;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RobotUserState getState() {
        return state;
    }

    public void setState(RobotUserState state) {
        this.state = state;
    }

    public int getAuctionCount() {
        return auctionCount;
    }

    public void setAuctionCount(int auctionCount) {
        this.auctionCount = auctionCount;
    }

    public int getMaxAuctionCount() {
        return maxAuctionCount;
    }

    public void setMaxAuctionCount(int maxAuctionCount) {
        this.maxAuctionCount = maxAuctionCount;
    }

    public UserMessage getUserMessage() {
        return userMessage;
    }

    public void setUserMessage(UserMessage userMessage) {
        this.userMessage = userMessage;
    }
}
