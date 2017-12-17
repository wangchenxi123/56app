package com.mierro.robot.controller;

import com.alibaba.fastjson.JSON;
import com.mierro.authority.entity.UserMessage;
import com.mierro.common.common.DataCheck;
import com.mierro.common.common.FileUtils;
import com.mierro.common.common.ResponseCode;
import com.mierro.common.common.ResultMessage;
import com.mierro.robot.entity.valid.Add;
import com.mierro.robot.entity.valid.Update;
import com.mierro.robot.entity.view.AddOrUpdateRobotUserView;
import com.mierro.robot.service.RobotUserService;
import org.hibernate.service.spi.ServiceException;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * 机器人模块用户管理
 * Created by tlseek on 2017/8/19.
 */
@RestController
@RequestMapping("/admin/robot")
public class AdminRobotUserController {

    @Resource
    RobotUserService robotUserService;


    /**
     * 分页查询机器人用户
     * @param name 用户名称
     * @param disable 是否禁用
     * @param pageNo 页码
     * @param pageSize 每页数量
     * @return 分页对象
     */
    @GetMapping("/users")
    public ResultMessage findAll(@RequestParam(value = "name",required = false) String name,
                                 @RequestParam(value = "disable",required = false) Boolean disable,
                                 @RequestParam("pageNo") Integer pageNo,
                                 @RequestParam("pageSize") Integer pageSize){
        Page page = robotUserService.findAll(name,disable,pageNo,pageSize);
        return new ResultMessage(ResponseCode.OK,"操作成功").putParam("resource",page);
    }

    /**
     * 查找用户信息
     * @param userId
     * @return
     */
    @GetMapping("/user")
    public ResultMessage findAll(@RequestParam Long userId){
        UserMessage userMessage = robotUserService.find(userId);
        return new ResultMessage(ResponseCode.OK,"操作成功").putParam("resource",userMessage);
    }

    /**
     * 获取机器人用户头像列表
     * @return
     */
    @GetMapping("/user/defaultHeadImgs")
    public ResultMessage findDefaultRobotUserHeadImg(){
        List page = robotUserService.findDefaultRobotUserHeadImg();
        return new ResultMessage(ResponseCode.OK,"操作成功").putParam("resource",page);
    }


    /**
     * 添加单个用户
     * @param userView
     * @return
     */
    @PostMapping("/user")
    public ResultMessage addUser(
            /*@RequestBody*/ @ModelAttribute("userView") @Validated({Add.class})AddOrUpdateRobotUserView userView, BindingResult result){
        DataCheck.returnError(result);
        robotUserService.addUser(userView);
        return new ResultMessage(ResponseCode.OK,"操作成功");
    }

    /**
     * 修改单个用户
     * @param userView
     * @return
     */
    @PutMapping("/user")
    public ResultMessage updateUser(
            /*@RequestBody*/ @ModelAttribute("userView") @Validated({Update.class})AddOrUpdateRobotUserView userView, BindingResult result){
        DataCheck.returnError(result);
        robotUserService.updateUser(userView);
        return new ResultMessage(ResponseCode.OK,"操作成功");
    }

    /**
     * 批量上传模板文件下载
     * @param response
     * @throws IOException
     */
    @GetMapping("/user/upload/tempFile")
    public void tempFile(HttpServletResponse response) throws IOException {
        File file = FileUtils.getConfigFile("./robot/robot-list-temp.json");
        if (file == null) {
            throw new ServiceException("文件丢失!");
        }
        response.reset();// 不加这一句的话会出现下载错误
        response.setHeader("Content-disposition",
                "attachment; filename=robot-list-temp.json");// 设定输出文件头
        response.setContentType("text/plain");// 定义输出类型
        FileUtils.inToout(new FileInputStream(file), response.getOutputStream());
    }

    /**
     * 批量上传机器人
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("/user/upload")
    public ResultMessage upload(@RequestParam("file") MultipartFile file)throws IOException {
        List<AddOrUpdateRobotUserView> userViews;
        try {
            userViews = JSON.parseArray(new String(file.getBytes(), "UTF-8"), AddOrUpdateRobotUserView.class);
        } catch (Exception e){
            return new ResultMessage(ResponseCode.BUSINESS,"JSON文件解析失败");
        }
        robotUserService.addUser(userViews);
        return new ResultMessage(ResponseCode.OK,"添加成功");
    }
}
