package com.mierro.robot.entity.view;

import com.mierro.common.common.IValueObject;
import com.mierro.robot.entity.valid.Add;
import com.mierro.robot.entity.valid.Update;

import javax.validation.constraints.NotNull;

/**
 * 添加或修改机器人bean
 * 注: 头像ID 和 头像文件名同时存在时 优先使用头像ID;
 *     头像ID 和 头像文件名都不存在时 从WEB-INF/robot/headImg/下随机选择一个作为头像
 * Created by tlseek on 2017/8/18.
 */
public class AddOrUpdateRobotUserView implements IValueObject, RobotUserHeadImgView {

    @NotNull(groups = {Update.class})
    private Long id;
    /**
     * 用户名
     */
    @NotNull(groups = {Add.class, Update.class})
    private String username;
    /**
     * 头像ID
     */
    private Long headImageFileId;
    /**
     * 头像文件名(位于WEB-INF/robot/headImg/下)
     */
    private String headImageName;
    /**
     * 地址
     */
    @NotNull(groups = {Add.class, Update.class})
    private String address;
    /**
     * 是否禁用
     */
    private Boolean disable;

    public AddOrUpdateRobotUserView() {
    }

    public AddOrUpdateRobotUserView(String username, Long headImageFileId, String headImageName, String address) {
        this.username = username;
        this.headImageFileId = headImageFileId;
        this.headImageName = headImageName;
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getHeadImageFileId() {
        return headImageFileId;
    }

    public void setHeadImageFileId(Long headImageFileId) {
        this.headImageFileId = headImageFileId;
    }

    public String getHeadImageName() {
        return headImageName;
    }

    public void setHeadImageName(String headImageName) {
        this.headImageName = headImageName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean getDisable() {
        return disable;
    }

    public void setDisable(Boolean disable) {
        this.disable = disable;
    }
}
