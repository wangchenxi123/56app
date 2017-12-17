package com.mierro.main.service;

import com.mierro.authority.entity.UserMessage;
import com.mierro.main.common.SealType;
import com.mierro.main.entity.IntegralBean;
import org.springframework.data.domain.Page;

import java.util.Map;

/**
 * Created by 黄晓滨 simba on 2017/8/11.
 * Remarks：
 */
public interface UserMessageService {

    /**
     * 按照条件查询用户
     * @param userId 用户id
     * @param name 用户名称
     * @param phone 手机号
     * @param disable 是否禁用
     * @param pageNo 页码
     * @param pageSize 每页数量
     * @return 分页对象
     */
    Page<UserMessage> findAll(Long userId, String name, String phone, Boolean disable, Integer pageNo, Integer pageSize);

    /**
     * 按照条件查询用户
     * @param userId 用户id
     * @param pageNo 页码
     * @param pageSize 每页数量
     * @return 分页对象
     */
    Page<UserMessage> findAll(Long userId, Integer pageNo, Integer pageSize);

    /**
     * 根据用户id查询用户信息
     * @param userId 用户id
     * @return 用户信息
     */
    UserMessage findOne(Long userId);

    /**
     * 注册新用户
     * @param phone 手机号
     * @param sms 验证码
     */
    Map<String,String> addUser(String phone, String sms, String ip, String position);
    /**
     * 注册新用户
     * @param phone 手机号
     * @param sms 验证码
     */
    Map<String,String> addUser(String phone, String sms, String ip, String password, String position);

    /**
     * 注册新用户
     * @param userId 代理用户id
     * @param phone 手机号
     * @param sms 验证码
     */
    void addUser(Long userId, String phone, String sms, String ip, String position);

    /**
     * 修改密码
     * @param userId 用户id
     * @param newPassword 新密码
     */
    void updatePassword(Long adminId, Long userId, String newPassword);

    /**
     * 修改密码
     * @param userId 用户id
     * @param sms 短信验证码
     * @param newPassword 新密码
     * @param oldPassword 旧密码
     */
    void updatePassword(Long userId, String sms, String newPassword, String oldPassword);

    /**
     * 查询个人竞拍记录
     * @param userId 用户id
     * @param type 查询类型
     * @return 多个种类
     */
    Object auctionRecord(Long userId, SealType type, Integer pageNo, Integer pageSize);

    /**
     * 修改用户信息
     * @param userMessage 用户信息
     */
    void updateUserMessage(UserMessage userMessage, String oldPhoneSMS, String newPhoneSMS);

    /**
     * 邀请奖励
     * @param userId 用户id
     * @return 邀请奖励页面数据
     */
    Map<String,Object> invitationToReward(Long userId);

    /**
     * 为订单设置收获地址
     * @param orderId 订单id
     * @param addressId 收货地址id
     * @param userId 用户id
     */
    void setting_address(Long orderId, Long addressId, Long userId);

    /**
     * 为某一个用户赠送拍币
     * @param userId 用户id
     * @param number 赠送数量
     * @param reason 备注说明
     */
    void giftCoin(Long userId, Integer number, String reason);

    /**
     * 为某一个用户赠送拍币
     * @param userId 用户id
     * @param number 赠送数量
     * @param reason 备注说明
     */
    void giftRealCoin(Long userId, Integer number, String reason);

    /**
     * 为某一个用户赠送积分(可以为负数)
     * @param userId 用户id
     * @param number 赠送数量
     * @param reason 备注说明
     */
    void giftIntegral(Long userId, Integer number, String reason);

    /**
     * 获取积分流动详情
     * @return 积分流动详情记录
     */
    Page<IntegralBean> integral_flow(Long userId, Integer pageNo, Integer pageSize);

    /**
     * 禁用用户
     * @param userId 用户id
     */
    void cancelOrDisableAdminByUserId(Long userId);

    /**
     * 检测重置用户签到时间
     * @param userId 用户id
     */
    void reportedReset(Long userId);

}
