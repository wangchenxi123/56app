package com.mierro.robot.entity;

import com.mierro.common.common.IValueObject;
import com.mierro.robot.entity.e.RobotConfig;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * 机器人配置信息
 * Created by tlseek on 2017/8/18.
 */
@Entity
@Table(name = "t_robot_config")
public class RobotConfigBean implements IValueObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(unique = true)
    private RobotConfig name;

    @NotNull
    @Column(columnDefinition="TEXT")
    private String value;

    private String message;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RobotConfig getName() {
        return name;
    }

    public void setName(RobotConfig key) {
        this.name = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
