package com.mierro.robot.entity;

import com.mierro.common.common.IValueObject;
import com.mierro.main.common.RunningProgram;
import com.mierro.robot.entity.valid.Update;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 机器人商品信息
 * Created by tlseek on 2017/8/12.
 */
@Entity
@Table(name = "t_robot_item")
public class RobotItemBean implements IValueObject {

    /**
     * 主键
     */
    @NotNull(groups = {Update.class})
    @Id
    @GeneratedValue(generator = "id")
    @GenericGenerator(name= "id",strategy = "com.mierro.common.common.KeyGenerator")
    private Long id;

    /**商品ID*/
    @Column(unique = true)
    private Long itemId;
    /**控线最小值*/
    @Min(1)
    private Integer controlLineMin;
    /**控线最大值*/
    @Min(1)
    private Integer controlLineMax;

    /**商品越过控线后机器人竞拍概率, 默认95%*/
    @Min(1)
    @Max(100)
    private Integer crossingAuctionRate;

    // ---------------------------------------------------------------

    /**平均成交价*/
    @Transient
    private BigDecimal averageMarketPrice;
    /**商品名*/
    @Transient
    private String name;
    /**票数控线*/
    @Transient
    private Integer control_line;
    /**运行方案*/
    @Transient
    private RunningProgram running_program;

    public RobotItemBean() {
    }

    public RobotItemBean(Long itemId, Integer controlLineMin, Integer controlLineMax) {
        this.itemId = itemId;
        this.controlLineMin = controlLineMin;
        this.controlLineMax = controlLineMax;
    }

    public RobotItemBean(Long itemId, Integer controlLineMin, Integer controlLineMax, Integer crossingAuctionRate) {
        this.itemId = itemId;
        this.controlLineMin = controlLineMin;
        this.controlLineMax = controlLineMax;
        this.crossingAuctionRate = crossingAuctionRate;
    }

    public RobotItemBean(RobotItemBean robotItem, String name, Integer control_line, RunningProgram running_program, Double averageMarketPrice) {
        this(robotItem);
        this.name = name;
        this.control_line = control_line;
        this.running_program = running_program;
        if (averageMarketPrice != null) {
            this.averageMarketPrice = new BigDecimal(averageMarketPrice);
        }
    }

    private RobotItemBean(RobotItemBean robotItem) {
        this.id = robotItem.id;
        this.itemId = robotItem.itemId;
        this.controlLineMin = robotItem.controlLineMin;
        this.controlLineMax = robotItem.controlLineMax;
        this.crossingAuctionRate = robotItem.crossingAuctionRate;
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

    public Integer getControlLineMin() {
        return controlLineMin;
    }

    public void setControlLineMin(Integer controlLineMin) {
        this.controlLineMin = controlLineMin;
    }

    public Integer getControlLineMax() {
        return controlLineMax;
    }

    public void setControlLineMax(Integer controlLineMax) {
        this.controlLineMax = controlLineMax;
    }

    public Integer getCrossingAuctionRate() {
        return crossingAuctionRate;
    }

    public void setCrossingAuctionRate(Integer crossingAuctionRate) {
        this.crossingAuctionRate = crossingAuctionRate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getControl_line() {
        return control_line;
    }

    public void setControl_line(Integer control_line) {
        this.control_line = control_line;
    }

    public RunningProgram getRunning_program() {
        return running_program;
    }

    public void setRunning_program(RunningProgram running_program) {
        this.running_program = running_program;
    }

    public BigDecimal getAverageMarketPrice() {
        return averageMarketPrice;
    }

    public void setAverageMarketPrice(BigDecimal averageMarketPrice) {
        this.averageMarketPrice = averageMarketPrice;
    }
}
